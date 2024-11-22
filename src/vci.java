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
    static public int indexVCI = 0;
    static public List<Token> vci = new ArrayList<>();
    static public Stack<Token> estatutos = new Stack<>();
    static public Stack<Integer> direccion = new Stack<>();
    static public Stack<Operadores> operadores = new Stack<>();

    public static void main(String[] args) {
        String printVCI = "";
        tablaTokens();
        empezar();
        crearVCI();
        // guardar todo el vci en la variable printVCI para luego mostrarla en consola
        // con solo los lexemas
        for (int index = 0; index < vci.size(); index++) {
            printVCI += vci.get(index).getLexema() + ", ";
        }
        System.out.println(printVCI);
    }

    // metodo para generar tabla de tokens
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

    // metodo para moverte hasta el primer begin del programa
    public static void empezar() {
        while (tokens.get(index).getToken() != -2) {
            index++;
        }
        index++;
    }

    // metodo principal para generar el VCI
    public static void crearVCI() {
        int token = 0;
        for (; index < tokens.size(); index++) {
            token = tokens.get(index).getToken();

            // el switch redirige a los metodos dependiendo el caso que se encuentre en base
            // al token actual
            switch (token) {
                case -4, -5:
                    writeRead();
                    break;

                case -51, -52, -53, -54, -55, -61, -62, -63, -64, -65, -73, -74:
                    identificadoresConstantes();
                    break;

                // case -6, -7, -16, -2, -3:
                //     condicionIfElse();
                //     break;

                case -8, -17, -2, -3:
                    condicionWhile();
                    break;

                default:
                    break;
            }
        }
    }

    // metodo auxiliar de crearVCI para los casos donde haya un write o un read
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

    public static void condicionIfElse() {
        Token marcador = null;
        switch (tokens.get(index).getToken()) {

            case -6:
                estatutos.push(tokens.get(index));
                break;

            case -16:
                marcador = new Token("0", 0, 0, 0);
                vci.add(marcador);
                indexVCI = vci.size() - 1;
                direccion.push(indexVCI);
                break;

            case -2:
                if (!estatutos.isEmpty()) { // Verificar si estatutos no está vacío
                    vci.add(estatutos.peek());
                }
                break;

            case -3:
                String posicionVCI = "";
                if (!direccion.isEmpty()) { // Verificar si direccion no está vacío
                    if (index + 1 < tokens.size() && tokens.get(index + 1).getToken() == -7) {
                        marcador = new Token("0", 0, 0, 0);
                        vci.add(marcador);
                        posicionVCI = String.valueOf(vci.size() + 1);
                        vci.set(direccion.pop(), new Token(posicionVCI, vci.size() + 1, 0, 0));
                        estatutos.pop();

                        estatutos.push(tokens.get(index + 1));
                        indexVCI = vci.size() - 1;
                        direccion.push(indexVCI);
                    } else {
                        posicionVCI = String.valueOf(vci.size());
                        vci.set(direccion.pop(), new Token(posicionVCI, vci.size(), 0, 0));
                        estatutos.pop();
                    }
                } else if (index + 1 == tokens.size()) { // si se llego al final del programa se sale del metodo
                    return;
                } else {
                    System.out.println("Error: La pila 'direccion' está vacía al intentar hacer 'peek'. "
                            + tokens.get(index).getLexema());
                }
                break;
        }
    }

    public static void condicionWhile() {
        // Es lo mismo que condicionIfElse solo se quita si hay un else, es como el if
        // simple, y agregar la condicion en identificadoresConstantes
        // que si el token es do mandar llamar este metodo -8, -17, -2, -3
        Token marcador = null;
        switch (tokens.get(index).getToken()) {

            case -8:
                estatutos.push(tokens.get(index));
                indexVCI = vci.size();
                direccion.push(indexVCI);
                break;

            case -17:
                marcador = new Token("0", 0, 0, 0);
                vci.add(marcador);
                indexVCI = vci.size() - 1;
                direccion.push(indexVCI);
                break;

            case -2:
                if (!estatutos.isEmpty()) { // Verificar si estatutos no está vacío
                    vci.add(estatutos.peek());
                }
                break;

            case -3:
                String posicionVCI = "";
                if (!direccion.isEmpty()) { // Verificar si direccion no está vacío
                        posicionVCI = String.valueOf(vci.size() + 1);
                        vci.set(direccion.pop(), new Token(posicionVCI, vci.size() + 1, 0, 0));
                        
                        posicionVCI = String.valueOf(direccion.peek());
                        vci.add(new Token(posicionVCI, direccion.pop(), 0, 0));
                        estatutos.pop();
                } else if (index + 1 == tokens.size()) { // si se llego al final del programa se sale del metodo
                    return;
                } else {
                    System.out.println("Error: La pila 'direccion' está vacía al intentar hacer 'peek'. "
                            + tokens.get(index).getLexema());
                }
                break;
        }
    }

    // metodo auxiliar de crearVCI para los casos donde haya una expresion
    // matematica
    public static void identificadoresConstantes() {
        int token = 0;
        while (tokens.get(index).getToken() != -75) {
            token = tokens.get(index).getToken();

            if (token == -16) {
                condicionIfElse();
                return;
            }

            if (token == -17) {
                condicionWhile();
                return;
            }

            switch (token) {
                case -21, -22, -24, -25, -26, -31, -32, -33, -34, -35, -36, -41, -42, -43, -73, -74:
                    operador();
                    break;

                case -51, -52, -53, -54, -55, -61, -62, -63, -64, -65:
                    vci.add(tokens.get(index));
                    break;
            }
            index++;
        }
        while (!operadores.isEmpty()) {
            Operadores ultimoOperador = operadores.pop();
            vci.add(ultimoOperador.getToken());
        }
    }

    // metodo auxiliar para asignar a los operadores su prioridad y mandarlos a la
    // pila
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

            case -73, -74:
                pilaOperador(-1, tokens.get(index).getToken());
                break;

            default:
                break;
        }
    }

    // metodo auxiliar de operador para agregar y sacar los operadores de la pila
    public static void pilaOperador(int prioridadActual) {
        Token tokenNuevo;
        int prioridad = 0;
        while (!operadores.isEmpty() && operadores.peek().getPrioridad() >= prioridadActual) {
            Operadores ultimoOperador = operadores.pop();
            vci.add(ultimoOperador.getToken());
        }
        tokenNuevo = tokens.get(index);
        prioridad = prioridadActual;
        operadores.push(new Operadores(tokenNuevo, prioridad));
    }

    // metodo alternativo de pilaOperador para los casos donde haya ( )
    public static void pilaOperador(int prioridadActual, int token) {
        Token tokenNuevo;
        int prioridad = 0;
        // si es un ( solo se inserta en la pila de operadores
        if (token == -73) {
            tokenNuevo = tokens.get(index);
            prioridad = prioridadActual;
            operadores.push(new Operadores(tokenNuevo, prioridad));
        } else if (token == -74) { // si es un ) va a sacar a todos los operadores hasta encontrar a )
            Operadores ultimoOperador = null;
            // si es un ) va a sacar a todos los operadores con prioridad mas alta
            while (!operadores.isEmpty() && operadores.peek().getPrioridad() > prioridadActual) {
                ultimoOperador = operadores.pop();
                vci.add(ultimoOperador.getToken());
            }
            // cuando la prioridad es igual significa que se encontro con el ( por lo que
            // solo lo tiene que eliminar
            ultimoOperador = operadores.pop();
        }
    }
}
