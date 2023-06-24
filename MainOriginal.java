/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author timothy
 */
public class MainOriginal {

    public static void main(String[] args) {
        /*
        String regexp = "(a|b)*abb", cad;
        Tools tool1 = new Tools();
        regexp = tool1.postFix(regexp);
        System.out.println(regexp);
        */
        AFNConvert myAFNConvert = new AFNConvert();
        DirectConverter myDirectConverter = new DirectConverter();
        AFN myAfn = new AFN();
        AFD myAfd = new AFD();
        AFD_DC myAfd_DC = new AFD_DC();
        AFDMinimized myAfdMinimized = new AFDMinimized();
        AFDMinimized_DC myAfdMinimized_DC = new AFDMinimized_DC();

        ArrayList<String>[] description = new ArrayList[5];
        Tools tool1 = new Tools();
        String regexp, cad, acepta;
        boolean accept;
        long startTime, finalTime;

        // Ingreso de la expresión regular
        System.out.print("\nINSTRUCCIONES: \n\tUsar '()' para la prioridad de operaciones.\n\tUsar '|' para or\n\tUsar '*' para kleene\nIngrese la expresión regular: ");
        Scanner leer = new Scanner(System.in);
        regexp = leer.nextLine();
        regexp = regexp.replace(" ", "");
        regexp = tool1.postFix(regexp);
        if (regexp.equals(null)){
            System.out.println("Error en la Expresion regular. Intente de nuevo..!");
            System.exit(0);
        }
        System.out.print("Ingrese una cadena para la simulacion: ");
        cad = leer.nextLine();
        cad = cad.replace(" ", "");

        // Conversión de la regexp
        try{
            /*============================================================
             * Construccion, Conversion y Minimizacion de los AF's
             =============================================================*/
            //regexp = "(a|b)*(abba*|(ab)*ba)";
            System.out.println("\nConvirtiendo a AFN...");
            myAfn = myAFNConvert.convert(regexp);   // Conversion de la regexp a AFN
            System.out.println("Convirtiendo el AFN a AFD...");
            myAfd = myAfn.covertToAFD();            // Conversion del AFN a AFD
            System.out.println("Construccion Directa del AFD a partir de la ER...");
            myAfd_DC = myDirectConverter.convert(regexp);
            System.out.println("Todas las conversiones fueron exitosas.");
            System.out.println("Minimizacion del AFD construido con el AFN...");
            myAfdMinimized = myAfd.minimizeAFD();    // Minimizacion del AFD
            System.out.println("Minimizacion del AFD construido directamente...");
            myAfdMinimized_DC = myAfd_DC.minimizeAFD();    // Minimizacion del AFD
            System.out.println("Minimizacion de ambos AFD's exitosa..!");

            /*============================================================
             * Descripcion y Simulacion de los AF's
             =============================================================*/

            // Impresion del AFN
            System.out.println("\nDescripcion del AFN:");
            description[0] = myAfn.afnDescription();
            for (String s: description[0])
                System.out.println(s);
            // Simulacion del AFN
            startTime = System.nanoTime();
            accept = myAfn.simulateAFN(cad);
            finalTime = System.nanoTime() - startTime;
            acepta = accept? "SI": "NO";
            System.out.println("Acepta el AF la cadena " + cad + "? " + acepta + "\nTiempo de simulacion: " + finalTime + " nanosegundos.");

            // Impresion del AFD construido con el AFN
            System.out.println("\n\nDescripcion del AFD construido con el AFN:");
            description[1] = myAfd.afdDescription();
            for (String s: description[1])
                System.out.println(s);
            // Simulacion del AFD construido con el AFN
            startTime = System.nanoTime();
            accept = myAfd.simulateAFD(cad);
            finalTime = System.nanoTime() - startTime;
            acepta = accept? "SI": "NO";
            System.out.println("Acepta el AF la cadena " + cad + "? " + acepta + "\nTiempo de simulacion: " + finalTime + " nanosegundos.");

            // Impresion del AFD construido directamente
            System.out.println("\n\nDescripcion del AFD construido directamente:");
            description[2] = myAfd_DC.afdDescription();
            for (String s: description[2])
                System.out.println(s);
            // Simulacion del AFD construido directamente
            startTime = System.nanoTime();
            accept = myAfd_DC.simulateAFD(cad);
            finalTime = System.nanoTime() - startTime;
            acepta = accept? "SI": "NO";
            System.out.println("Acepta el AF la cadena " + cad + "? " + acepta + "\nTiempo de simulacion: " + finalTime + " nanosegundos.");


            // Impresion del AFD minimizado
            System.out.println("\n\nDescripcion del AFD construido con el AFN, minimizado:");
            description[3] = myAfdMinimized.afdDescription();
            for (String s: description[3])
                System.out.println(s);
            // Simulacion del AFD construido directamente
            startTime = System.nanoTime();
            accept = myAfdMinimized.simulateAFD(cad);
            finalTime = System.nanoTime() - startTime;
            acepta = accept? "SI": "NO";
            System.out.println("Acepta el AF la cadena " + cad + "? " + acepta + "\nTiempo de simulacion: " + finalTime + " nanosegundos.");

            // Impresion del AFD minimizado
            System.out.println("\n\nDescripcion del AFD connstruido directamtne, minimizado:");
            description[4] = myAfdMinimized_DC.afdDescription();
            for (String s: description[4])
                System.out.println(s);
            // Simulacion del AFD construido directamente
            startTime = System.nanoTime();
            accept = myAfdMinimized_DC.simulateAFD(cad);
            finalTime = System.nanoTime() - startTime;
            acepta = accept? "SI": "NO";
            System.out.println("Acepta el AF la cadena " + cad + "? " + acepta + "\nTiempo de simulacion: " + finalTime + " nanosegundos.");
        }
        catch (Exception err){
            System.out.println("Ocurrio un error inesperado. Revise la sintaxis..!");
            System.exit(0);
        }

        /*============================================================
         * Generacion de los archivos de descripcion de los automatas.
         =============================================================*/

        // Generacion de los archivos
        try{
            tool1.generateFile(description[0],"afn1.txt");      // AFN construido a partir de la regexp
            tool1.generateFile(description[1],"afd1.txt");      // AFD construido a partir del AFN
            tool1.generateFile(description[2],"afdDirectConstructed1.txt");     // AFD construido directamente
            tool1.generateFile(description[3], "afdMinimized1.txt");        // AFD construido con el AFN minimizado
            tool1.generateFile(description[4], "afdMinimized2.txt");    // AFD construido directamente minimizado
        }
        catch (Exception err){
            System.out.println("Ocurrió un error en la generacion de los archivos..!!");
            System.exit(0);
        }

        /*=========================================================
         * Generacion de la imagen de un automata.
         ==========================================================*/
        // try{
        //     description[0] = myAfn.getDigraphCode();
        //     description[1] = myAfd.getDigraphCode();
        //     description[2] = myAfd_DC.getDigraphCode();
        //     description[3] = myAfdMinimized.getDigraphCode();
        //     description[4] = myAfdMinimized_DC.getDigraphCode();
        //     String[] objectNames = {"Afn1", "Afd1", "afdDirectConstructed1", "AfdMinimized1", "AfdMinimized2"};
        //     String dotPath = "C:\\Users\\Servir Cesar\\Documents\\Wuelmer\\afnconverter\\graphviz 2.38\\release\\bin\\dot.exe";
        //     String path1 = "C:\\Users\\Servir Cesar\\Documents\\Wuelmer\\afnconverter\\";
        //     String path2 = "C:\\Users\\Servir Cesar\\Documents\\Wuelmer\\afnconverter\\";
        //     String fileInputPath;
        //     String fileOutputPath;
        //     for (int i=0; i<5; i++){
        //         tool1.generateFile(description[i], "codeGraph" + objectNames[i] + ".txt");        // Generacion del codigo del grafo
        //         fileInputPath = path1 + "codeGraph" + objectNames[i] + ".txt";
        //         fileOutputPath = path2 + objectNames[i] + ".jpg";
        //         tool1.generateAFDImage(dotPath, fileInputPath, fileOutputPath);     // Generacion del grafo
        //     }
        // }
        // catch (Exception err){
        //     System.out.println("Error en la generacion de los grafos..!");
        // }
    }
}
