package proyecto_manuel;

import javax.swing.*; //Importa todo lo necesario para usar JOptionPane
import java.io.*; //Importa lo necesario para el manejo de archivos
import java.util.ArrayList; //Importa una clase para poder usar ArrayList
import java.util.regex.Matcher; //Importa una clase para poder trabajar con ER
import java.util.regex.Pattern; //Importa una clase para manipular texto basado en patrones de ER


/*Considere como entrada la lectura desde archivo de texto (varias lineas y varias palabras por linea ) y 
 *como salida la clasificación de tokens en un archivo externo para el lenguaje de prueba generando la tabla 
 *de tokens con la siguiente estructura sin encabezados y separados por comas

  Lexema, token, posicion en tabla de simbolos, numero de linea
*/

public class Practica4 {
	
	static String nombreArchivo = "C:\\Users\\lizet\\OneDrive\\Documentos\\UNIVERSIDAD\\SEPTIMO SEMESTRE\\LENGUAJES Y AUTOMATAS 2\\Compilador\\practica4.txt";
    static SinglyLinkedList<DatosPalabra> palabrasDelArchivo = new SinglyLinkedList<>();
    static SinglyLinkedList<DatosPalabra> erroresDelArchivo = new SinglyLinkedList<>();

    public static void main(String[] args) {
        if (leerArchivo()) {
            analisisLexico();
            erroresAnalisis();
            escribirArchivo();
        }
    }
    
    private static boolean archivoLecturaTieneContenido() {
        try {
            FileReader fr = new FileReader(nombreArchivo);
            BufferedReader br = new BufferedReader(fr);
            boolean tieneContenido = br.readLine() != null; // Verificar si hay al menos una línea para leer
            br.close();
            return tieneContenido; //retorno "true"
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Conflicto para leer el archivo");
            return false;
        }
    }

    private static boolean leerArchivo() {
        if (!archivoLecturaTieneContenido()) {
            JOptionPane.showMessageDialog(null, "El archivo está vacío");
            return false;
        }
        try {
            FileReader fr = new FileReader(nombreArchivo);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            //contador de lineas
            int numLinea = 0;
            while ((linea = br.readLine()) != null) {  //leer el archivo por lineas
                numLinea++;
                logicaLectura(linea,numLinea);
            }
            br.close();//cerrar lectura de archivo
            JOptionPane.showMessageDialog(null, "Archivo leido con exito");
            return true;

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo, porque no existe o no se encuentra");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Conflicto para leer el archivo");
        }
        return false;
    }

    // Método que procesa la línea leída del archivo
    private static void logicaLectura(String linea,int numLinea){
    	Pattern pattern = Pattern.compile("\"(.+?)\"|//(.*?)//|\\d+(\\.)\\d+|\\d+|&&|\\|\\||!|\\+|-|\\*|/|%|:=|<=|>=|<|>|==|!=|[(),;:]|[a-zA-Z]+[a-zA-Z0-9_]*[#$%&?]|\\s+");
        Matcher matcher = pattern.matcher(linea);
        int start = 0;
        while (matcher.find()) {
            if (matcher.start() != start) {
                palabrasDelArchivo.addLast(new DatosPalabra(linea.substring(start, matcher.start()).trim(), numLinea));
            }
            if (!matcher.group().matches("\\s+")) {
                palabrasDelArchivo.addLast(new DatosPalabra(matcher.group(), numLinea));
            }
            start = matcher.end();
        }//no se encontro coincidencia
        if (start != linea.length()) {
            palabrasDelArchivo.addLast(new DatosPalabra(linea.substring(start).trim(), numLinea));
        }
    }

    private static void categoriaIdentificadores(DatosPalabra datosPalabra) {
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("[a-zA-Z]+[a-zA-Z0-9_]*[#$%&?]$")) {
            datosPalabra.setEsIdentificador(-2);
            char ultimoChar = palabra.charAt(palabra.length() - 1);
            if (ultimoChar == '#') { //"identificadores tipo cadena de texto
                datosPalabra.setValorToken(-53);
                //System.out.println(datosPalabra);
            } else if (ultimoChar == '%') { //identificadores de valor real
                datosPalabra.setValorToken(-52);
                //System.out.println(datosPalabra);
            } else if (ultimoChar == '&') {//identificadores de valor entero
                datosPalabra.setValorToken(-51);
                //System.out.println(datosPalabra);
            } else if (ultimoChar == '$') {//identificadores de valor logico
                datosPalabra.setValorToken(-54);
                //System.out.println(datosPalabra);
            } else if (ultimoChar == '?') {//identificadores tipo programa
                datosPalabra.setValorToken(-55);
                //System.out.println(datosPalabra);
            }
        }//no es identificador
    }

    private static void categoriaPalabrasReservadas(DatosPalabra datosPalabra) {
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("program|begin|end|read|write|if|else|while|repeat|until|int|real|string|bool|var|then|do")) {
            datosPalabra.setEsIdentificador(-1);

            switch (palabra) {
                case "program":
                    datosPalabra.setValorToken(-1);
                    //System.out.println(datosPalabra);
                    break;
                case "begin":
                    datosPalabra.setValorToken(-2);
                    //System.out.println(datosPalabra);
                    break;
                case "end":
                    datosPalabra.setValorToken(-3);
                    //System.out.println(datosPalabra);
                    break;
                case "read":
                    datosPalabra.setValorToken(-4);
                    //System.out.println(datosPalabra);
                    break;
                case "write":
                    datosPalabra.setValorToken(-5);
                    //System.out.println(datosPalabra);
                    break;
                case "if":
                    datosPalabra.setValorToken(-6);
                    //System.out.println(datosPalabra);
                    break;
                case "else":
                    datosPalabra.setValorToken(-7);
                    //System.out.println(datosPalabra);
                    break;
                case "while":
                    datosPalabra.setValorToken(-8);
                    //System.out.println(datosPalabra);
                    break;
                case "repeat":
                    datosPalabra.setValorToken(-9);
                    //System.out.println(datosPalabra);
                    break;
                case "until":
                    datosPalabra.setValorToken(-10);
                    //System.out.println(datosPalabra);
                    break;
                case "int":
                    datosPalabra.setValorToken(-11);
                    //System.out.println(datosPalabra);
                    break;
                case "real":
                    datosPalabra.setValorToken(-12);
                    //System.out.println(datosPalabra);
                    break;
                case "string":
                    datosPalabra.setValorToken(-13);
                    //System.out.println(datosPalabra);
                    break;
                case "bool":
                    datosPalabra.setValorToken(-14);
                    //System.out.println(datosPalabra);
                    break;
                case "var":
                    datosPalabra.setValorToken(-15);
                    //System.out.println(datosPalabra);
                    break;
                case "then":
                    datosPalabra.setValorToken(-16);
                    //System.out.println(datosPalabra);
                    break;
                case "do":
                    datosPalabra.setValorToken(-17);
                    //System.out.println(datosPalabra);
                    break;
                default:
                    // No es una palabra reservada
                    break;
            }
        }
    }

    private static void categoriaCaracteresEspeciales(DatosPalabra datosPalabra) {
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("[(),;:]")) {
            datosPalabra.setEsIdentificador(-1);
            switch (palabra) {
                case "(":
                    datosPalabra.setValorToken(-73);
                    //System.out.println(datosPalabra);
                    break;
                case ")":
                    datosPalabra.setValorToken(-74);
                    //System.out.println(datosPalabra);
                    break;
                case ",":
                    datosPalabra.setValorToken(-76);
                    //System.out.println(datosPalabra);
                    break;
                case ";":
                    datosPalabra.setValorToken(-75);
                    //System.out.println(datosPalabra);
                    break;
                case ":":
                    datosPalabra.setValorToken(-77); //Se añadio el -77 para el caracter especial : no estaba en la tabla de tokens
                    //System.out.println(datosPalabra);
                    break;
                default:
                    break;
            }
        }
    }

    private static void categoriaNumerosEnteros(DatosPalabra datosPalabra) {
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("\\d+")) {
            datosPalabra.setEsIdentificador(-1);
            datosPalabra.setValorToken(-61);
            //System.out.println(datosPalabra);
        }//no es numero entero
    }

    private static void categoriaNumerosDecimales(DatosPalabra datosPalabra) {
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("\\d+(\\.)\\d+")) { //modificaciones al (-)?
            datosPalabra.setEsIdentificador(-1);
            datosPalabra.setValorToken(-62);
            //System.out.println(datosPalabra);
        }//no es numero decimal
    }

    private static void categoriaConstanteString(DatosPalabra datosPalabra) {
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("\"(.+?)\"")) {
            datosPalabra.setEsIdentificador(-1);
            datosPalabra.setValorToken(-63);
            //System.out.println(datosPalabra);
        }//no es constante string
    }

    private static void categoriaValorLogico(DatosPalabra datosPalabra) {
        String palabra = datosPalabra.getPalabra();
        if(palabra.matches("true|false")){
            datosPalabra.setEsIdentificador(-1);
            switch (palabra) {
                case "true":
                    datosPalabra.setValorToken(-64);
                    //System.out.println(datosPalabra);
                    break;
                case "false":
                    datosPalabra.setValorToken(-65);
                    //System.out.println(datosPalabra);
                    break;
                default:
                    break;//no es valor logico
            }
        }
    }

    private static void categoriaOperadoresMatematicos(DatosPalabra datosPalabra) {
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("\\+|-|\\*|/|%|:=")) {
            datosPalabra.setEsIdentificador(-1);
            switch (palabra) {
                case "+":
                    datosPalabra.setValorToken(-24);
                    //System.out.println(datosPalabra);
                    break;
                case "-":
                    datosPalabra.setValorToken(-25);
                    //System.out.println(datosPalabra);
                    break;
                case "*":
                    datosPalabra.setValorToken(-21);
                    //System.out.println(datosPalabra);
                    break;
                case "/":
                    datosPalabra.setValorToken(-22);
                    //System.out.println(datosPalabra);
                    break;
                case "%":
                    datosPalabra.setValorToken(-23);
                    //System.out.println(datosPalabra);
                    break;
                case ":=":
                    datosPalabra.setValorToken(-26);
                    //System.out.println(datosPalabra);
                default:
                    break;
            }
        }
    }

    private static void categoriaOperadoresRelacionales(DatosPalabra datosPalabra) {
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("<|>|<=|>=|==|!=")) {
            datosPalabra.setEsIdentificador(-1);
            switch (palabra) {
                case "<":
                    datosPalabra.setValorToken(-31);
                    //System.out.println(datosPalabra);
                    break;
                case "<=":
                    datosPalabra.setValorToken(-32);
                    //System.out.println(datosPalabra);
                    break;
                case ">":
                    datosPalabra.setValorToken(-33);
                    //System.out.println(datosPalabra);
                    break;
                case ">=":
                    datosPalabra.setValorToken(-34);
                    //System.out.println(datosPalabra);
                    break;
                case "==":
                    datosPalabra.setValorToken(-35);
                    //System.out.println(datosPalabra);
                    break;
                case "!=":
                    datosPalabra.setValorToken(-36);
                    //System.out.println(datosPalabra);
                    break;
                default:
                    break;
            }
        }
    }

    private static void categoriaOperadoresLogicos(DatosPalabra datosPalabra){
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("&&|\\|\\||!")) {
            datosPalabra.setEsIdentificador(-1);
            switch (palabra) {
                case "&&":
                    datosPalabra.setValorToken(-41);
                    //System.out.println(datosPalabra);
                    break;
                case "||":
                    datosPalabra.setValorToken(-42);
                    //System.out.println(datosPalabra);
                    break;
                case "!":
                    datosPalabra.setValorToken(-43);
                    //System.out.println(datosPalabra);
                    break;
                default:
                    break;
            }
        }
    }

    private static void categoriaComentarios(DatosPalabra datosPalabra){
        String palabra = datosPalabra.getPalabra();
        if (palabra.matches("//(.*?)//")) {
            datosPalabra.setEsIdentificador(-1);

            //System.out.println(datosPalabra);
        }
    }

    //Se analiza una serie de caracteres guardada en la lista de palabras
    private static void analisisLexico() {
       for (DatosPalabra datosPalabra : palabrasDelArchivo) {
            categoriaIdentificadores(datosPalabra);
            categoriaPalabrasReservadas(datosPalabra);
            categoriaCaracteresEspeciales(datosPalabra);
            categoriaNumerosEnteros(datosPalabra);
            categoriaNumerosDecimales(datosPalabra);
            categoriaConstanteString(datosPalabra);
            categoriaValorLogico(datosPalabra);
            categoriaOperadoresMatematicos(datosPalabra);
            categoriaOperadoresRelacionales(datosPalabra);
            categoriaOperadoresLogicos(datosPalabra);
            categoriaComentarios(datosPalabra);
        }
    }

    public static void erroresAnalisis(){
        for (DatosPalabra datosPalabra : palabrasDelArchivo) {
            if (datosPalabra.getValorToken()==0) {
                int longitud = datosPalabra.getPalabra().length();
                boolean ignoreDot = false;
                if (datosPalabra.getPalabra().charAt(0) == '/' && datosPalabra.getPalabra().charAt(1) == '/' && datosPalabra.getPalabra().charAt(longitud - 1) == '/' && datosPalabra.getPalabra().charAt(longitud - 2) == '/'|| datosPalabra.getPalabra().equals(".")) {
                    continue;
                }else{
                    erroresDelArchivo.addLast(new DatosPalabra(datosPalabra.getPalabra().trim(), datosPalabra.getValorToken(), datosPalabra.getEsIdentificador(), datosPalabra.getPosicion()));
                }
            }
        }
    }

    //Metodo para escribir en el archivo .txt las palabras analizadas
    private static void escribirArchivoTablaDeTokens() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\lizet\\OneDrive\\Escritorio\\tabla de tokens.txt"));
            for (DatosPalabra datosPalabra : palabrasDelArchivo) {
                if (datosPalabra.getValorToken()!=0){
                    writer.write(datosPalabra.toString());
                    writer.newLine();
                }
            }
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Conflicto para escribir el archivo");
        }
    }

    public static void escribirArchivoTablaDeErrores(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\lizet\\OneDrive\\Escritorio\\tabla de errores.txt"));
            for (DatosPalabra datosPalabra : erroresDelArchivo) {
                writer.write("Error en la linea: " + datosPalabra.getPosicion() + ". En la palabra: " + datosPalabra.getPalabra());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Conflicto para escribir el archivo");
        }
    }

    private static void escribirArchivo(){
        if (erroresDelArchivo.isEmpty()) {
            escribirArchivoTablaDeTokens();
        } else {
            escribirArchivoTablaDeErrores();
        }
    }    
}
