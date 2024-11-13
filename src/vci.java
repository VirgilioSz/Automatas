import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class vci {
    static public List<Token> tokens = new ArrayList<>();
    static public int index = 0;
    static public List<Token> vci = new ArrayList<>();
    static public Stack<String> estatutos = new Stack<>();
    static public Stack<Integer> direccion = new Stack<>();
    static public Stack<Operadores> operadores = new Stack<>();

    public static void main(String[] args) {
        String printVCI = "";
        tablaTokens();
        empezar();
        crearVCI();
        for (int index = 0; index < vci.size(); index++) {
           printVCI += vci.get(index).getLexema() + ", ";
        }
        System.out.println(printVCI);
    }

    public static void tablaTokens() {
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
    }

    public static void empezar() {
        while (tokens.get(index).getToken() != -2) {
            index++;
        }
        index++;
    }

    public static void crearVCI() {
        int token = 0;
        for (; index < tokens.size(); index++) {
            token = tokens.get(index).getToken();

            switch (token) {
                case -4, -5:
                    writeRead();
                    break;

                default:
                    break;
            }
        }
    }

    public static void writeRead() {
        int token = 0;
        Token tokenAux = null;
        while (tokens.get(index).getToken() != -75) {
            token = tokens.get(index).getToken();

            switch (token) {
                case -4, -5:
                    tokenAux = tokens.get(index);
                    break;

                case -73, -74:
                    break;

                case -21, -22:
                    operador();
                    break;

                default:
                    vci.add(tokens.get(index));
                    break;
            }
            index++;
        }
        vci.add(tokenAux);

    }

    public static void operador() {
        int token = 0;
        token = tokens.get(index).getToken();

        switch (token) {
            case -21, -22:
                pilaOperador(60);
                break;

            case -24, -25:
                pilaOperador(50);
                break;

            case -31, -32, -33, -34, -35, -36:
                pilaOperador(40);
                break;

            case -41:
                pilaOperador(30);
                break;

            case -42:
                pilaOperador(20);
                break;

            case -43:
                pilaOperador(10);
                break;

            case -26:
                pilaOperador(0);
                break;

            default:
                break;
        }
    }

    public static void pilaOperador(int prioridadActual) {
        Token tokenNuevo;
        int prioridad = 0;
        while (operadores.peek().getPrioridad() >= prioridadActual) {
            Operadores ultimoOperador = operadores.pop();
            vci.add(ultimoOperador.getToken());
        }
        tokenNuevo = tokens.get(index);
        prioridad = prioridadActual;
        operadores.push(new Operadores(tokenNuevo, prioridad));
    }
}
