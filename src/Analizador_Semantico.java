import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Analizador_Semantico {
	static public List<Token> tokens = new ArrayList<>();
	static public List<Simbolo> simbolos = new ArrayList<>();
	static public List<Direccion> direcciones = new ArrayList<>();

	private static void error(String mensaje) {
		System.out.println((char) 27 + "[31m" + "ERROR SINTACTICO! " + mensaje);
		System.exit(1);
	}

	public static void main(String[] args) {

		try (BufferedReader br = new BufferedReader(
				new FileReader(
						"C:\\Users\\virgi\\Documents\\ITS\\Semestre 7\\Lenguajes y automatas ii\\Proyecto1\\src\\tabla de tokens.txt"))) {
			String line;
			int lineNumber = 1;
			while ((line = br.readLine()) != null) {
				Pattern pattern = Pattern.compile("(.*?),(-?\\d+),(-?\\d+),(-?\\d+)");
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					String lexema = matcher.group(1);
					int token = Integer.parseInt(matcher.group(2));
					int posTabla = Integer.parseInt(matcher.group(3));
					int noLinea = Integer.parseInt(matcher.group(4));
					Token tokenObj = new Token(lexema, token, posTabla, noLinea);
					tokens.add(tokenObj);
				} else {
					System.out.println("Error en la línea " + lineNumber + ": formato incorrecto.");
				}
				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		obtenerdirecciones(); // se cambio el orden
		obtenerSimbolos(); // se cambio el orden
		generarTablaSimbolos();
		generarTabladirecciones();
		reescribirTablaToken();
		JOptionPane.showMessageDialog(null, "Tabla de simbolos creada.");
		JOptionPane.showMessageDialog(null, "Tabla de direcciones creada.");
	}

	private static boolean estaEnTablaSimbolos(String id) {
		boolean encontrado = false;
		for (int i = 0; i < simbolos.size(); i++) {
			String lexema = simbolos.get(i).getIdentificador();
			if (id.equals(lexema)) {
				encontrado = true;
				error("Duplicate local variable " + lexema);// si ya esta en el array manda el msj
															// a consola y deja de correr el programa
				break;
			}
		}
		return encontrado;
	}

	private static boolean estaEnTabladirecciones(String id) {
		boolean encontrado = false;
		for (int i = 0; i < direcciones.size(); i++) {
			String lexema = direcciones.get(i).getIdentificador();
			if (id.equals(lexema)) {
				encontrado = true;
				error("Duplicate global variable " + lexema);// si ya esta en el array manda el msj
																// a consola y deja de correr el programa
				break;
			}
		}
		return encontrado;
	}

	private static void obtenerSimbolos() {
		String direccion = direcciones.get(0).getIdentificador();/// aqui se hizo un cambio
		for (int i = 0; i < tokens.size(); i++) {
			String lexema = tokens.get(i).getLexema();
			int token = tokens.get(i).getToken();
			boolean encontrado = estaEnTablaSimbolos(lexema);
			if (token == -51 && encontrado == false) {
				Simbolo simbolo = new Simbolo(lexema, token, 0, 0, 0, "null", direccion);
				simbolos.add(simbolo);
			} else if (token == -52 && encontrado == false) {
				Simbolo simbolo = new Simbolo(lexema, token, 0.0f, 0, 0, "null", direccion);
				simbolos.add(simbolo);
			} else if (token == -53 && encontrado == false) {
				Simbolo simbolo = new Simbolo(lexema, token, null, 0, 0, "null", direccion);
				simbolos.add(simbolo);
			} else if (token == -54 && encontrado == false) {
				Simbolo simbolo = new Simbolo(lexema, token, false, 0, 0, "null", direccion);
				simbolos.add(simbolo);
			} else if (token == -2) {// si viene el token 'begin', se dea de crear la tabla de simbolos
				break;
			}
		}
	}

	private static void obtenerdirecciones() {
		for (int i = 0; i < tokens.size(); i++) {
			String lexema = tokens.get(i).getLexema();
			int token = tokens.get(i).getToken();
			boolean encontrado = estaEnTabladirecciones(lexema);
			if (token == -55 && encontrado == false) {
				Direccion direccion = new Direccion(lexema, token, tokens.get(i).getNo_linea(), 0);
				direcciones.add(direccion);
			} else if (token == -15) {// si viene la token 'var' se deja de crear token de tio direccion
				break;
			}
		}
	}

	private static void generarTablaSimbolos() {
		try (FileWriter fw = new FileWriter(
				"C:\\Users\\virgi\\Documents\\ITS\\Semestre 7\\Lenguajes y automatas ii\\Proyecto1\\src\\tabla de simbolos.txt",
				false)) {
			for (int i = 0; i < simbolos.size(); i++) {
				int token = simbolos.get(i).getToken();
				String linea = null;
				if (token == -51) {
					linea = simbolos.get(i).getIdentificador() + "," + token + "," + simbolos.get(i).getValorInt() + ","
							+ simbolos.get(i).getD1() + "," + simbolos.get(i).getD2() + "," + simbolos.get(i).getPtr()
							+ "," + simbolos.get(i).getAmbito() + "\n";
				} else if (token == -52) {
					linea = simbolos.get(i).getIdentificador() + "," + token + "," + simbolos.get(i).getValorReal()
							+ "," + simbolos.get(i).getD1() + "," + simbolos.get(i).getD2() + ","
							+ simbolos.get(i).getPtr() + "," + simbolos.get(i).getAmbito() + "\n";
				} else if (token == -53) {
					linea = simbolos.get(i).getIdentificador() + "," + token + "," + simbolos.get(i).getValorcadena()
							+ "," + simbolos.get(i).getD1() + "," + simbolos.get(i).getD2() + ","
							+ simbolos.get(i).getPtr() + "," + simbolos.get(i).getAmbito() + "\n";
				} else if (token == -54) {
					linea = simbolos.get(i).getIdentificador() + "," + token + "," + simbolos.get(i).isValorLogico()// aqui
																													// se
																													// hizo
																													// el
																													// cambio
							+ "," + simbolos.get(i).getD1() + "," + simbolos.get(i).getD2() + ","
							+ simbolos.get(i).getPtr() + "," + simbolos.get(i).getAmbito() + "\n";
				}
				fw.write(linea);
			}
			fw.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void generarTabladirecciones() {
		try (FileWriter fw = new FileWriter(
				"C:\\Users\\virgi\\Documents\\ITS\\Semestre 7\\Lenguajes y automatas ii\\Proyecto1\\src\\tabla de direcciones.txt",
				false)) {
			for (int i = 0; i < direcciones.size(); i++) {
				int token = direcciones.get(i).getToken();
				String linea = direcciones.get(i).getIdentificador() + "," + token + ","
						+ direcciones.get(i).getNoLinea() + "," + direcciones.get(i).getVci() + "\n";
				fw.write(linea);
			}
			fw.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void reescribirTablaToken() {
		try (FileWriter fw = new FileWriter(
				"C:\\Users\\virgi\\Documents\\ITS\\Semestre 7\\Lenguajes y automatas ii\\Proyecto1\\src\\tabla de tokens.txt",
				false)) {
			for (int i = 0; i < tokens.size(); i++) {
				Token token = tokens.get(i);
				boolean encontrado = false;

				for (int j = 0; j < simbolos.size(); j++) {
					if (token.getLexema().equals(simbolos.get(j).getIdentificador())) {
						String linea = token.getLexema() + "," + token.getToken() + "," + j + "," + token.no_linea
								+ "\n";
						fw.write(linea);
						encontrado = true;
					}
				}

				for (int j = 0; j < direcciones.size(); j++) {
					if (token.getLexema().equals(direcciones.get(j).getIdentificador())) {
						String linea = token.getLexema() + "," + token.getToken() + "," + j + "," + token.no_linea
								+ "\n";
						fw.write(linea);
						encontrado = true;
					}
				}

				if (!encontrado) {
					String linea = token.getLexema() + "," + token.getToken() + "," + token.getPos_Tabla() + ","
							+ token.no_linea + "\n";
					fw.write(linea);
				}
			}
		} catch (IOException e) {
			System.out.println("Error al escribir en el archivo: " + e.getMessage());
		}
	}

}
