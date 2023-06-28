import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Ingrese los argumentos requeridos.");
            System.exit(0);
        }

        String pathRegex = args[0];
        String modoEjecucion = args[1];
        String archivoSalida = "output.txt";

        if (args.length > 2) {
            archivoSalida = args[2];

            if (archivoSalida.equals("")) {
                System.out.println("Ingrese un nombre de archivo valido");
                System.exit(0);
            }
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
        String cuerda = "";
        boolean accept;
        ArrayList<String> description;

        String regexPostfix = tool1.postFix(regex);

        // Generar AFD
        afd = converter.convert(regexPostfix);

        if (modoEjecucion.equals("-afd")) {
            description = afd.afdDescription();

            // Impresion del AFD
            System.out.println("Descripcion del AFD:");
            for (String s: description)
                System.out.println(s);

            try {
                // Generar el archivo de descripcion del AFD
                tool1.generateFile(description, archivoSalida);
            }
            catch (Exception err) {
                System.out.println("Ocurrió un error en la generacion del archivo");
                System.exit(0);
            }
        }

        // Generar GLD
        else if (modoEjecucion.equals("-gld")) {
            description = afd.toGLD();

            // Impresion del GLD
            System.out.println("Descripcion del GLD:");
            for (String s: description)
                System.out.println(s);

            try {
                // Generar el archivo de descripcion del GLD
                tool1.generateFile(description, archivoSalida);
            }
            catch (Exception err) {
                System.out.println("Ocurrió un error en la generacion del archivo");
                System.exit(0);
            }
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
                tool1.generateFile(description, archivoSalida);
            }
            catch (Exception err) {
                System.out.println("Ocurrió un error en la generacion del archivo");
                System.exit(0);
            }
        }

        // Evaluar cuerdas
        else if (modoEjecucion.equals("-eval")) {
            // Java usa un "|" para aplicar un OR, por eso sustituimos los "+" por un "|".
            regex = regex.replace("+", "|");

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
                // accept = afd.simulateAFD(cuerda);
                accept = cuerda.matches(regex);

                System.out.println("Acepta el Regex la cuerda " + cuerda + "? " + (accept ? "SI" : "NO"));
            }
        }

        // Ejecucion invalida
        else {
            System.out.println("Modo de ejecucion invalida.");
        }
    }
}
