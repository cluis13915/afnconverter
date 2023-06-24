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
public class Main {

    public static void main(String[] args) {
        /*
        String regexp = "(a|b)*abb", cad;
        Tools tool1 = new Tools();
        regexp = tool1.postFix(regexp);
        System.out.println(regexp);
        */
        DirectConverter myDirectConverter = new DirectConverter();
        AFD_DC myAfd_DC = new AFD_DC();
        AFDMinimized_DC myAfdMinimized_DC = new AFDMinimized_DC();

        ArrayList<String>[] description = new ArrayList[2];
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
            System.out.println("Construccion Directa del AFD a partir de la ER...");
            myAfd_DC = myDirectConverter.convert(regexp);
            System.out.println("Minimizacion del AFD construido directamente...");
            myAfdMinimized_DC = myAfd_DC.minimizeAFD();    // Minimizacion del AFD

            /*============================================================
             * Descripcion y Simulacion de los AF's
             =============================================================*/
            // Impresion del AFD construido directamente
            System.out.println("\n\nDescripcion del AFD construido directamente:");
            description[0] = myAfd_DC.afdDescription();
            for (String s: description[0])
                System.out.println(s);
            // Simulacion del AFD construido directamente
            startTime = System.nanoTime();
            accept = myAfd_DC.simulateAFD(cad);
            finalTime = System.nanoTime() - startTime;
            acepta = accept? "SI": "NO";
            System.out.println("Acepta el AF la cadena " + cad + "? " + acepta + "\nTiempo de simulacion: " + finalTime + " nanosegundos.");

            // Impresion del AFD minimizado
            System.out.println("\n\nDescripcion del AFD connstruido directamente, minimizado:");
            description[1] = myAfdMinimized_DC.afdDescription();
            for (String s: description[1])
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
            tool1.generateFile(description[0],"afdDirectConstructed1.txt");     // AFD construido directamente
            tool1.generateFile(description[1], "afdMinimized2.txt");    // AFD construido directamente minimizado
        }
        catch (Exception err){
            System.out.println("Ocurrió un error en la generacion de los archivos..!!");
            System.exit(0);
        }
    }
}
