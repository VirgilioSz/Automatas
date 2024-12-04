import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ejecucion {
	static public Scanner scanner = new Scanner(System.in);
	static public List<Token> vci = new ArrayList<>();
	static public List<Simbolo> simbolos = new ArrayList<>();
	static public Stack<Token> ejecucion = new Stack<>();
	static public int indexVci = 0;
	static public int indexExe = 0;

	public static void main(String[] args) {
		obtenerVci();
		obtenerSimbolos();
		ejecutar();
		// System.out.print(vci.toString());
		// System.out.print(simbolos.toString());
	}

	// metodo para generar lista de VCI
	public static void obtenerVci() {
		try (BufferedReader br = new BufferedReader(
				new FileReader(
						"C:\\Users\\virgi\\Documents\\ITS\\Semestre 7\\Lenguajes y automatas ii\\Proyecto1\\src\\vci.txt"))) {
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
					vci.add(tokenObj);
				} else {
					System.out.println("Error en la línea " + lineNumber + ": formato incorrecto.");
				}
				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// metodo para generar lista de VCI
	public static void obtenerSimbolos() {
		try (BufferedReader br = new BufferedReader(
				new FileReader("C:\\Users\\lizet\\OneDrive\\Escritorio\\tabla de simbolos.txt"))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] partes = linea.split(","); // Dividir por comas
				String identificador = partes[0];
				int token = Integer.parseInt(partes[1]);
				int d1 = Integer.parseInt(partes[3]);
				int d2 = Integer.parseInt(partes[4]);
				String ptr = partes[5];
				String ambito = partes[6];

				// Determinar tipo de valor según el token
				Simbolo simbolo = null;
				switch (token) {
					case -51: // Integer
						int valorInt = Integer.parseInt(partes[2]);
						simbolo = new Simbolo(identificador, token, valorInt, d1, d2, ptr, ambito);
						break;
					case -52: // Real
						float valorReal = Float.parseFloat(partes[2]);
						simbolo = new Simbolo(identificador, token, valorReal, d1, d2, ptr, ambito);
						break;
					case -53: // Cadena
						String valorCadena = partes[2];
						simbolo = new Simbolo(identificador, token, valorCadena, d1, d2, ptr, ambito);
						break;
					case -54: // Booleano
						boolean valorLogico = Boolean.parseBoolean(partes[2]);
						simbolo = new Simbolo(identificador, token, valorLogico, d1, d2, ptr, ambito);
						break;
					default:
						throw new RuntimeException("Token desconocido: " + token);
				}

				// Añadir el símbolo a la lista
				simbolos.add(simbolo);
			}
		} catch (Exception e) {
			System.out.println("Error al leer la tabla de símbolos: " + e.getMessage());
		}
	}

	public static void ejecutar() {
		int token = 0;
		for (; indexVci < vci.size(); indexVci++) {
			// System.out.print(ejecucion+"\n");
			token = vci.get(indexVci).getToken();
			// System.out.print(ejecucion.toString());

			// el switch redirige a los metodos dependiendo el caso que se encuentre en base
			// al token actual
			switch (token) {
				case -4:
					read();
					break;

				case -5:
					write();
					writeconstantes();
					ejecucion.pop();
					break;

				case -6:// if
					estatutoIf();
					break;

				case -7:// else
					estatutoElse();
					break;

				case -8:// while
					estructuraWhile();
					break;

				case 1:// endwhile
					endWhile();
					break;

				case 2:// enddo
					endDo();
					break;

				case -51, -52, -53, -54, -55, -61, -62, -63, -64, -65:
					identificadoresConstantes();
					break;

				case -21, -22, -23, -24, -25, -31, -32, -33, -34, -35, -36, -41, -42:
					esOperador();
					break;

				case -26:
					auxAsiganacion();// asignacion();
					break;

				case -43:
					operadorNot();
					break;

				default:
					break;

			}
			// System.out.print(simbolos.toString());
			// System.out.println(indexVci);
		}
		// System.out.print(simbolos.toString());
	}

	public static void identificadoresConstantes() {
		ejecucion.push(vci.get(indexVci));

	}

	public static void direccion() {
		int direccion = vci.get(indexVci).getToken();
		if (direccion > 0) {
			ejecucion.push(vci.get(indexVci));
		}
	}

	public static void read() {
		System.out.print(">>");
		String valorLeido = scanner.nextLine(); // Lee una línea completa
		String variable = ejecucion.peek().getLexema();
		int token = ejecucion.peek().getToken();

		for (int i = 0; i < simbolos.size(); i++) {
			if (variable.equals(simbolos.get(i).getIdentificador())) {
				switch (token) {
					case -51:
						simbolos.get(i).setValorInt(Integer.parseInt(valorLeido));
						break;

					case -52:
						simbolos.get(i).setValorReal(Float.parseFloat(valorLeido));
						break;

					case -53:
						simbolos.get(i).setValorcadena(valorLeido);
						break;

					case -54:
						simbolos.get(i).setValorLogico(Boolean.parseBoolean(valorLeido));
						break;

					default:
						break;
				}
			}
		}
		ejecucion.pop();
	}

	public static void writeconstantes() {
		String variable = ejecucion.peek().getLexema();
		int token = ejecucion.peek().getToken();

		for (int i = 0; i < simbolos.size(); i++) {
			if (variable.equals(simbolos.get(i).getIdentificador())) {
				switch (token) {
					case -51:
						System.out.print(simbolos.get(i).getValorInt());
						break;

					case -52:
						System.out.print(simbolos.get(i).getValorReal());
						break;

					case -53:
						System.out.print(simbolos.get(i).getValorcadena());
						break;

					case -54:
						System.out.print(simbolos.get(i).getValorReal());
						break;

					default:
						break;
				}
			}
		}

	}

	public static void write() {
		String aImprimir = ejecucion.peek().getLexema();
		int token = ejecucion.peek().getToken();

		switch (token) {
			case -61:
				System.out.println(aImprimir);
				break;

			case -62:
				System.out.println(aImprimir);
				break;

			case -63:
				System.out.println(aImprimir);
				break;

			case -64, -65:
				System.out.println(aImprimir);
				break;

			default:
				break;
		}
	}

	public static void esOperador() {
		Token operando2 = ejecucion.pop();
		Token operando1 = ejecucion.pop();
		Token auxToken = null;

		int tk = vci.get(indexVci).getToken();
		switch (tk) {
			case -21:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					int resInt = int1 * int2;
					String auxResultado = String.valueOf(resInt);
					auxToken = new Token(auxResultado, -61, 0, 0);
				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					float res = real1 * real2;
					String auxResultado = String.valueOf(res);
					auxToken = new Token(auxResultado, -62, 0, 0);
				}
				break;

			case -22:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					int resInt = int1 / int2;
					String auxResultado = String.valueOf(resInt);
					auxToken = new Token(auxResultado, -61, 0, 0);
				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					float res = real1 / real2;
					String auxResultado = String.valueOf(res);
					auxToken = new Token(auxResultado, -62, 0, 0);
				}
				break;

			case -23:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					int resInt = int1 % int2;
					String auxResultado = String.valueOf(resInt);
					auxToken = new Token(auxResultado, -61, 0, 0);
				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					float res = real1 % real2;
					String auxResultado = String.valueOf(res);
					auxToken = new Token(auxResultado, -62, 0, 0);
				}
				break;

			case -24:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					int resInt = int1 + int2;
					String auxResultado = String.valueOf(resInt);
					auxToken = new Token(auxResultado, -61, 0, 0);
				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					float res = real1 + real2;
					String auxResultado = String.valueOf(res);
					auxToken = new Token(auxResultado, -62, 0, 0);
				}
				break;

			case -25:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					int resInt = int1 - int2;
					String auxResultado = String.valueOf(resInt);
					auxToken = new Token(auxResultado, -61, 0, 0);
				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					float res = real1 - real2;
					String auxResultado = String.valueOf(res);
					auxToken = new Token(auxResultado, -62, 0, 0);
				}
				break;

			case -31:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					boolean resInt = int1 < int2;
					String auxResultado = String.valueOf(resInt);
					if (resInt) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!resInt) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}

				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					boolean res = real1 < real2;
					String auxResultado = String.valueOf(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				}
				break;

			case -32:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					boolean resInt = int1 <= int2;
					String auxResultado = String.valueOf(resInt);
					if (resInt) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!resInt) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}

				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					boolean res = real1 <= real2;
					String auxResultado = String.valueOf(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				}
				break;

			case -33:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					boolean resInt = int1 > int2;
					String auxResultado = String.valueOf(resInt);
					if (resInt) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!resInt) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}

				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					boolean res = real1 > real2;
					String auxResultado = String.valueOf(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				}
				break;

			case -34:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					boolean resInt = int1 >= int2;
					String auxResultado = String.valueOf(resInt);
					if (resInt) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!resInt) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}

				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					boolean res = real1 >= real2;
					String auxResultado = String.valueOf(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				}
				break;

			case -35:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					boolean resInt = int1 == int2;
					String auxResultado = String.valueOf(resInt);
					if (resInt) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!resInt) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}

				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					boolean res = real1 == real2;
					String auxResultado = String.valueOf(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				}
				break;

			case -36:
				if ((operando2.getToken() == -51 || operando2.getToken() == -61)
						&& (operando1.getToken() == -51 || operando1.getToken() == -61)) {// enteros
					int int2 = castearEntero(operando2);
					int int1 = castearEntero(operando1);
					boolean resInt = int1 != int2;
					String auxResultado = String.valueOf(resInt);
					if (resInt) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!resInt) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}

				} else if ((operando2.getToken() == -52 || operando2.getToken() == -62)
						&& (operando1.getToken() == -52 || operando1.getToken() == -62)) {// reales
					float real2 = castearReal(operando2);
					float real1 = castearReal(operando1);
					boolean res = real1 != real2;
					String auxResultado = String.valueOf(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				} else if ((operando2.getToken() == -53 || operando2.getToken() == -63)
						&& (operando1.getToken() == -53 || operando1.getToken() == -63)) {// cadena
					String cad2 = castearCadena(operando2);
					String cad1 = castearCadena(operando1);
					boolean res = cad1 != cad2;
					String auxResultado = Boolean.toString(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				} else if ((operando2.getToken() == -54 || operando2.getToken() == -64 || operando2.getToken() == -65)
						&& (operando1.getToken() == -54 || operando1.getToken() == -64
								|| operando1.getToken() == -65)) {// logicos
					boolean log2 = castearLogico(operando2);
					boolean log1 = castearLogico(operando1);
					boolean res = log1 != log2;
					String auxResultado = Boolean.toString(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				}
				break;

			case -41:
				if ((operando2.getToken() == -54 || operando2.getToken() == -64 || operando2.getToken() == -65)
						&& (operando1.getToken() == -54 || operando1.getToken() == -64
								|| operando1.getToken() == -65)) {// logicos
					boolean log2 = castearLogico(operando2);
					boolean log1 = castearLogico(operando1);
					boolean res = log1 && log2;
					String auxResultado = Boolean.toString(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				}
				break;
			case -42:
				if ((operando2.getToken() == -54 || operando2.getToken() == -64 || operando2.getToken() == -65)
						&& (operando1.getToken() == -54 || operando1.getToken() == -64
								|| operando1.getToken() == -65)) {// logicos
					boolean log2 = castearLogico(operando2);
					boolean log1 = castearLogico(operando1);
					boolean res = log1 || log2;
					String auxResultado = Boolean.toString(res);
					if (res) {
						auxToken = new Token(auxResultado, -64, 0, 0);
					} else if (!res) {
						auxToken = new Token(auxResultado, -65, 0, 0);
					}
				}
				break;

			default:
				break;
		}
		ejecucion.push(auxToken);
	}

	public static void operadorNot() {
		Token auxToken = null;
		Token operando1 = ejecucion.pop();
		if ((operando1.getToken() == -54 || operando1.getToken() == -64 || operando1.getToken() == -65)) {// logicos
			boolean log1 = castearLogico(operando1);
			boolean res = !log1;
			String auxResultado = Boolean.toString(res);
			if (res) {
				auxToken = new Token(auxResultado, -64, 0, 0);
			} else if (!res) {
				auxToken = new Token(auxResultado, -65, 0, 0);
			}
		}
		ejecucion.push(auxToken);
	}

	public static int castearEntero(Token tk) {
		int valorTrue = 0;
		if (tk.getToken() == -51) {// es una variable de tipo int
			for (int i = 0; i < simbolos.size(); i++) {
				if (tk.getLexema().equals(simbolos.get(i).getIdentificador())) {
					valorTrue = simbolos.get(i).getValorInt();
					break;
				}
			}
		} else if (tk.getToken() == -61) {// es una constante
			valorTrue = Integer.parseInt(tk.getLexema());
		}
		return valorTrue;
	}

	public static float castearReal(Token tk) {
		float valorTrue = 0.0f;
		if (tk.getToken() == -52) {// es una variable de tipo real
			for (int i = 0; i < simbolos.size(); i++) {
				if (tk.getLexema().equals(simbolos.get(i).getIdentificador())) {
					valorTrue = simbolos.get(i).getValorReal();
					break;
				}
			}
		} else if (tk.getToken() == -62) {// es una constante
			valorTrue = Float.parseFloat(tk.getLexema());
		}
		return valorTrue;
	}

	public static String castearCadena(Token tk) {
		String valorTrue = null;
		if (tk.getToken() == -53) {// es una variable de tipo cadena
			for (int i = 0; i < simbolos.size(); i++) {
				if (tk.getLexema().equals(simbolos.get(i).getIdentificador())) {
					valorTrue = simbolos.get(i).getValorcadena();
					break;
				}
			}
		} else if (tk.getToken() == -63) {// es una constante
			valorTrue = tk.getLexema();
		}
		return valorTrue;
	}

	public static boolean castearLogico(Token tk) {
		boolean valorTrue = false;
		if (tk.getToken() == -54) {// es una variable de tipo real
			for (int i = 0; i < simbolos.size(); i++) {
				if (tk.getLexema().equals(simbolos.get(i).getIdentificador())) {
					valorTrue = simbolos.get(i).isValorLogico();
					break;
				}
			}
		} else if (tk.getToken() == -64) {// true
			valorTrue = true;
		} else if (tk.getToken() == -65) {// false
			valorTrue = false;
		}
		return valorTrue;
	}

	public static void asignacion() {
		Token valor = ejecucion.pop();
		Token variable = ejecucion.pop();
		String valorAguardar = valor.getLexema(); // Lee una línea completa
		String auxVar = variable.getLexema();
		int token = variable.getToken();

		for (int i = 0; i < simbolos.size(); i++) {
			if (auxVar.equals(simbolos.get(i).getIdentificador())) {
				switch (token) {
					case -51:
						simbolos.get(i).setValorInt(Integer.parseInt(valorAguardar));
						break;

					case -52:
						simbolos.get(i).setValorReal(Float.parseFloat(valorAguardar));
						break;

					case -53:
						simbolos.get(i).setValorcadena(valorAguardar);
						break;

					case -54:
						simbolos.get(i).setValorLogico(Boolean.parseBoolean(valorAguardar));
						break;

					default:
						break;
				}
			}
		}
	}

	public static void asignacion2() {
		Token valor = ejecucion.pop();
		Token variable = ejecucion.pop();
		int tokenVariable = variable.getToken();
		int tokenValor = valor.getToken();
		String valorAguardar = null;
		String auxVar = variable.getLexema();

		for (int i = 0; i < simbolos.size(); i++) {
			if (valor.getLexema().equals(simbolos.get(i).getIdentificador())) {
				switch (tokenValor) {
					case -51:
						int aux = simbolos.get(i).getValorInt();
						valorAguardar = String.valueOf(aux);
						break;

					case -52:
						float auxReal = simbolos.get(i).getValorReal();
						valorAguardar = String.valueOf(auxReal);
						break;

					case -53:
						valorAguardar = simbolos.get(i).getValorcadena();
						break;

					case -54:
						boolean auxBool = simbolos.get(i).isValorLogico();
						valorAguardar = String.valueOf(auxBool);
						break;

					default:
						break;
				}
			}
		}

		for (int i = 0; i < simbolos.size(); i++) {
			if (auxVar.equals(simbolos.get(i).getIdentificador())) {
				switch (tokenVariable) {
					case -51:
						simbolos.get(i).setValorInt(Integer.parseInt(valorAguardar));
						break;

					case -52:
						simbolos.get(i).setValorReal(Float.parseFloat(valorAguardar));
						break;

					case -53:
						simbolos.get(i).setValorcadena(valorAguardar);
						break;

					case -54:
						simbolos.get(i).setValorLogico(Boolean.parseBoolean(valorAguardar));
						break;

					default:
						break;
				}
			}
		}

		for (int i = 0; i < simbolos.size(); i++) {
			if (auxVar.equals(simbolos.get(i).getIdentificador())) {

			}
		}

	}

	public static void auxAsiganacion() {
		Token valorAguardar = ejecucion.pop();// valor del simbolo
		Token variable = ejecucion.pop();// variable en la que se va a guardar

		int tk = valorAguardar.getToken();

		switch (tk) {
			case -51, -61:// enteros
				int auxEntero = castearEntero(valorAguardar);

				for (int i = 0; i < simbolos.size(); i++) {
					if (variable.getLexema().equals(simbolos.get(i).getIdentificador())) {
						simbolos.get(i).setValorInt(auxEntero);
					}
				}
				break;

			case -52, -62:// reales
				float auxfloat = castearReal(valorAguardar);
				for (int i = 0; i < simbolos.size(); i++) {
					if (variable.getLexema().equals(simbolos.get(i).getIdentificador())) {
						simbolos.get(i).setValorReal(auxfloat);
						;
					}
				}
				break;

			case -53, -63:// cadena
				String auxCadena = castearCadena(valorAguardar);

				for (int i = 0; i < simbolos.size(); i++) {
					if (variable.getLexema().equals(simbolos.get(i).getIdentificador())) {
						simbolos.get(i).setValorcadena(auxCadena);
						;
						;
					}
				}
				break;

			case -54, -64, -65:// logico
				boolean auxBoolean = castearLogico(valorAguardar);
				for (int i = 0; i < simbolos.size(); i++) {
					if (variable.getLexema().equals(simbolos.get(i).getIdentificador())) {
						simbolos.get(i).setValorLogico(auxBoolean);
						;
						;
						;
					}
				}
				break;
		}
	}

	public static boolean entroIf = false;

	public static void estatutoIf() {
		entroIf = false;
		Token tk = vci.get(indexVci - 1);
		int direccion = Integer.parseInt(tk.getLexema());
		boolean seCumple = Boolean.parseBoolean(ejecucion.peek().getLexema());

		if (seCumple) {
			entroIf = true;
			ejecucion.pop();
		} else {
			ejecucion.pop();
			indexVci = direccion - 1;
		}
	}

	public static void estatutoElse() {
		Token tk = vci.get(indexVci - 1);
		int direccion = Integer.parseInt(tk.getLexema());// obtiene la direecion anteroe al ese

		if (entroIf) {// si entro al if
			indexVci = direccion - 1;
		} else {
			indexVci++;
		}
	}

	public static void estructuraWhile() {
		Token tk = vci.get(indexVci - 1);
		int direccion = Integer.parseInt(tk.getLexema());
		boolean seCumple = Boolean.parseBoolean(ejecucion.peek().getLexema());

		if (seCumple) {// si se cumple saca el valor boolean de la pila y
			ejecucion.pop();
		} else {
			ejecucion.pop();
			indexVci = direccion - 1;
		}
	}

	public static void endWhile() {
		Token tk = vci.get(indexVci - 1);
		int direccion = Integer.parseInt(tk.getLexema());
		indexVci = direccion - 1;
	}

	public static void endDo() {
		Token tk = vci.get(indexVci - 1);
		int direccion = Integer.parseInt(tk.getLexema());// se obtiene la direccion
		boolean seCumple = Boolean.parseBoolean(ejecucion.peek().getLexema());// se obtiene el valor
																				// de la evaluciion de la condici[on

		if (seCumple) {// si se cumple la condicion
			ejecucion.pop();
			indexVci = direccion - 1;
		} else {// si no se cumple la condicion
			ejecucion.pop();
			indexVci = indexVci + 2;
		}
	}

}
