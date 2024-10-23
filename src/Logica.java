import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logica {
	static public List<Token> tokens = new ArrayList<>();
	static public List<Simbolo> simbolos = new ArrayList<>();
	static public List<Direccion> direcciones = new ArrayList<>();
	static public int index = 0;

	static public List<Integer> aritmeticos = Arrays.asList(-21, -22, -23, -24);
	static public List<Integer> enteros = Arrays.asList(-61, -51);
	static public List<Integer> reales = Arrays.asList(-62, -52);
	static public List<Integer> identificadores = Arrays.asList(-51, -52, -53, -54);
	static public List<Integer> relacionales = Arrays.asList(-31, -32, -33, -34, -35);

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

		empezar();
		obtenerSimbolos();
		moverYImprimirSimbolos();
	}

	public static void empezar() {
		while (tokens.get(index).getToken() != -2) {
			index++;
		}
		index++;
	}

	public static void moverYImprimirSimbolos() {
		System.out.println("Moviéndose por la tabla de tokens e imprimiendo símbolos:");

		// Avanza por la tabla de tokens y busca símbolos
		for (; index < tokens.size(); index++) {
			String lexema = tokens.get(index).getLexema();
			int token = tokens.get(index).getToken();
			Simbolo simbolo;

			switch (token) {
				case -51:
					simbolo = new Simbolo(lexema, token, 0, 0, 0, "null", "main");
					simbolos.add(simbolo);
					entero();
					break;

				case -52:
					simbolo = new Simbolo(lexema, token, 0, 0, 0, "null", "main");
					simbolos.add(simbolo);
					real();
					break;

				case -53:
					simbolo = new Simbolo(lexema, token, 0, 0, 0, "null", "main");
					simbolos.add(simbolo);
					cadena();
					break;

				case -54:
					simbolo = new Simbolo(lexema, token, 0, 0, 0, "null", "main");
					simbolos.add(simbolo);
					logico();
					break;

				default:
					break;
			}
		}
	}

	public static void entero() {
		index++;

		if (tokens.get(index).getToken() == -26) { // el token tiene que ser :=
			index++;
			System.out.println("Enteros enteros: ");
			while (tokens.get(index).getToken() != -75 && (aritmeticos.contains(tokens.get(index).getToken())
					|| enteros.contains(tokens.get(index).getToken())
					|| relacionales.contains(tokens.get(index).getToken()))) {
				System.out.println(tokens.get(index).getLexema()); // token despues de :=
				index++;

			}
			if (tokens.get(index).getToken() != -75) {
				error("variable o operador no valido");
			}
		}

		if (tokens.get(index).getToken() == -74) {
			System.out.println("Enteros parentesis: \n" + tokens.get(index).getLexema()); // token despues de variable
																							// sola
			index++;
		}

		if (relacionales.contains(tokens.get(index).getToken())) {
			System.out.println("Enteros realcionales:");
			while (tokens.get(index).getToken() != -74 && (enteros.contains(tokens.get(index).getToken())
					|| relacionales.contains(tokens.get(index).getToken()))) {
				System.out.println(tokens.get(index).getLexema()); // token despues de relacionales
				index++;
			}
		}
	}

	public static void real() {
		index++;

		if (tokens.get(index).getToken() == -26) { // el token tiene que ser :=
			index++;
			System.out.println("Reales enteros: ");
			while (tokens.get(index).getToken() != -75 && (aritmeticos.contains(tokens.get(index).getToken())
					|| reales.contains(tokens.get(index).getToken())
					|| relacionales.contains(tokens.get(index).getToken()))) {
				System.out.println(tokens.get(index).getLexema()); // token despues de :=
				index++;

			}
			if (tokens.get(index).getToken() != -75) {
				error("variable o operador no valido");
			}
		}

		if (tokens.get(index).getToken() == -74) {
			System.out.println("Reales parentesis: \n" + tokens.get(index).getLexema()); // token despues de variable
																							// sola
			index++;
		}

		if (relacionales.contains(tokens.get(index).getToken())) {
			System.out.println("Reales realcionales:");
			while (tokens.get(index).getToken() != -74 && (enteros.contains(tokens.get(index).getToken())
					|| relacionales.contains(tokens.get(index).getToken()))) {
				System.out.println(tokens.get(index).getLexema()); // token despues de relacionales
				index++;
			}
		}
	}

	public static void cadena() {
		index++;

		if (tokens.get(index).getToken() == -26) { // el token tiene que ser :=
			index++;
			System.out.println("Cadena asignacion: ");
			while (tokens.get(index).getToken() != -75 && (tokens.get(index).getToken() == -24
					|| tokens.get(index).getToken() == -53 || tokens.get(index).getToken() == -63)) {
				System.out.println(tokens.get(index).getLexema()); // token despues de :=
				index++;

			}
			if (tokens.get(index).getToken() != -75) {
				error("variable o operador no valido");
			}
		}

		if (tokens.get(index).getToken() == -74) {
			System.out.println("Cadena parentesis: \n" + tokens.get(index).getLexema()); // token despues de variable
																							// sola
			index++;
		}
	}

	public static void logico() {
		index++;

		if (tokens.get(index).getToken() == -26) { // el token tiene que ser :=
			index++;
			System.out.println("Logico asignacion: ");
			while (tokens.get(index).getToken() != -75 && (tokens.get(index).getToken() == -64
					|| tokens.get(index).getToken() == -65)) {
				System.out.println(tokens.get(index).getLexema()); // token despues de :=
				index++;

			}
			if (tokens.get(index).getToken() != -75) {
				error("variable o operador no valido");
			}
		}

		if (tokens.get(index).getToken() == -41 || tokens.get(index).getToken() == -42
				|| tokens.get(index).getToken() == -43) {
			if (identificadores.contains(tokens.get(index - 1).getToken())
					&& identificadores.contains(tokens.get(index + 1).getToken())) {
				System.out.println("Logico asignacion: ");
				System.out.println(tokens.get(index).getLexema());
				index++;
			}
		}

		if (tokens.get(index).getToken() == -74) {
			System.out.println("Logico parentesis: \n" + tokens.get(index).getLexema()); // token despues de variable
																							// sola
			index++;
		}
	}

	public static void obtenerSimbolos() {
		for (int i = 0; i < tokens.size(); i++) {
			String lexema = tokens.get(i).getLexema();
			int token = tokens.get(i).getToken();
			if (token == -51) {
				Simbolo simbolo = new Simbolo(lexema, token, 0, 0, 0, "null", "main");
				simbolos.add(simbolo);
			} else if (token == -52) {
				Simbolo simbolo = new Simbolo(lexema, token, 0.0f, 0, 0, "null", "main");
				simbolos.add(simbolo);
			} else if (token == -53) {
				Simbolo simbolo = new Simbolo(lexema, token, null, 0, 0, "null", "main");
				simbolos.add(simbolo);
			} else if (token == -54) {
				Simbolo simbolo = new Simbolo(lexema, token, false, 0, 0, "null", "main");
				simbolos.add(simbolo);
			} else if (token == -2) {// si viene el token 'begin', se dea de crear la tabla de simbolos
				break;
			}
		}
	}

	private static void error(String mensaje) {
		System.out.println((char) 27 + "[31m" + "ERROR SINTACTICO! " + mensaje);
		System.exit(1);
	}
}
