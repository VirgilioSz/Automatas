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
		for (int i = index; i < tokens.size(); i++) {
			String lexema = tokens.get(i).getLexema();
			int token = tokens.get(i).getToken();

			// Verifica si el token es uno de los que representan símbolos
			if (token == -51 || token == -52 || token == -53 || token == -54) {

					Simbolo simbolo = new Simbolo(lexema, token, 0, 0, 0, "null", "main");
					simbolos.add(simbolo);
					System.out.println("Símbolo encontrado: " + simbolo.getIdentificador() + " En linea: " + tokens.get(i).getNo_linea());
				
			}
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
