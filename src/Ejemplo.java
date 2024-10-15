import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ejemplo {

	public static void main(String[] args) {

		// Ruta del archivo de entrada
        String rutaArchivo = "C:\\Users\\manue\\Desktop\\practica4.txt";

        try {
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea;

            // Leer cada línea del archivo
            while ((linea = lector.readLine()) != null) {
                // Aplicar la lógica de coincidencia de patrones
                //Pattern patron = Pattern.compile("\\d+");
            	 Pattern patron = Pattern.compile("(true)|(false)");
                Matcher matcher = patron.matcher(linea);

                if (matcher.matches()) {
                    System.out.println("Coincide: " + linea);
                }
            }

            lector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
