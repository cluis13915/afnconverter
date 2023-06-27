import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Ingrese los argumentos requeridos.");

            return;
        }

        String pathRegex = args[0];
        String modoEjecucion = args[1];
        String archivoSalida;

        if (args.length > 2) {
            archivoSalida = args[2];
        }

        String alphabet, regex = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathRegex));
            alphabet = reader.readLine();
            regex = reader.readLine();
            reader.close();
        }
        catch (Exception err) {
            System.out.println("Ocurrio un error al leer archivo");
            System.exit(0);
        }

        DirectConverter converter = new DirectConverter();
        AFD afd;
        AFDMinimized afdMinimized;

        Tools tool1 = new Tools();
        String cuerda = "", acepta;
        boolean accept;
        ArrayList<String> description;

        regex = tool1.postFix(regex);

        // Generar AFD
        afd = converter.convert(regex);

        if (modoEjecucion.equals("-afd")) {
            description = afd.afdDescription();

            // Impresion del AFD construido directamente
            System.out.println("Descripcion del AFD:");
            for (String s: description)
                System.out.println(s);

            try {
                // Generar el archivo de descripcion del AFD
                tool1.generateFile(description,"afd.txt");
            }
            catch (Exception err) {
                System.out.println("Ocurrió un error en la generacion del archivo");
                System.exit(0);
            }
        }

        // Generar GLD
        else if (modoEjecucion.equals("-gld")) {
            System.out.println("Pendiente de implementar.");
        }

        // Minimizar AFD
        else if (modoEjecucion.equals("-min")) {
            // Aplicar el algoritmo de minimizacion sobre el AFD.
            afdMinimized = afd.minimizeAFD();
            description = afdMinimized.afdDescription();

            // Impresion del AFD minimizado
            System.out.println("Descripcion del AFD minimizado:");
            for (String s: description)
                System.out.println(s);

            try {
                // Generar el archivo de descripcion del AFD
                tool1.generateFile(description,"afdMinimized.txt");
            }
            catch (Exception err) {
                System.out.println("Ocurrió un error en la generacion del archivo");
                System.exit(0);
            }
        }

        // Evaluar cuerdas
        else if (modoEjecucion.equals("-eval")) {
            Scanner inputReader = new Scanner(System.in);

            while (true) {
                System.out.print("Ingrese una cuerda para evaluar (exit para salir): ");
                cuerda = inputReader.nextLine();

                if (cuerda.equals("")) {
                    continue;
                }

                if (cuerda.equals("exit")) {
                    break;
                }

                cuerda = cuerda.replace(" ", "");

                // Simulacion del AFD construido directamente
                accept = afd.simulateAFD(cuerda);
                acepta = accept ? "SI": "NO";
                System.out.println("Acepta el AFD la cuerda " + cuerda + "? " + acepta);
            }
        }

        // Ejecucion invalida
        else {
            System.out.println("Modo de ejecucion invalida.");
        }
    }
}
