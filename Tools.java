/*
	Autor: Cesar Luis, 12539
	Descripcion: Implementación de los algoritmos básicos de autómatas finitos y expresiones regulares.
	Fecha: 14 de agosto de 2014
*/

/**
 * Autor: Cesar Anibal Luis Alvarado
 * Descripcion: Herramientas
 * Fecha: 18 de julio de 2014
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

public class Tools {

    private File archivo;
    public Tools() {}

    // M�todo generador del archivo de texto
    public void generateFile(ArrayList<String> content, String fileName){
        archivo = new File(fileName);	// Creacion del archivo
        try{
            PrintWriter grabador = new PrintWriter(archivo);	//Instancia del grabador
            for (String line : content)
                grabador.println(line);
            grabador.close();	//Cerrando el grabador
        }
        catch(Exception e){
            System.out.println("Error al crear el archivo de texto..!");
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    //Conversión de notación Infija a Postfija mediante uso de pilas


    public String postFix(String regexp){

        //Adicion de los puntos de concatenacion
        regexp = this.addConcatenation(regexp.replace(" ", ""));
        //Depurar la expresion algebraica
        String expr = depurar(regexp);
        String postfix = "", infix = "";
        String[] arrayInfix = expr.split(" ");

        //Declaración de las pilas
        Stack < String > E = new Stack < String > (); //Pila entrada
        Stack < String > P = new Stack < String > (); //Pila temporal para operadores
        Stack < String > S = new Stack < String > (); //Pila salida

        //Añadir la array a la Pila de entrada (E)
        for (int i = arrayInfix.length - 1; i >= 0; i--) {
            E.push(arrayInfix[i]);
        }

        try {
            //Algoritmo Infijo a Postfijo
            while (!E.isEmpty()) {
                switch (pref(E.peek())){
                    case 1:
                        P.push(E.pop());
                        break;
                    case 3:
                    case 4:
                        while(pref(P.peek()) >= pref(E.peek())) {
                            S.push(P.pop());
                        }
                        P.push(E.pop());
                        break;
                    case 2:
                        while(!P.peek().equals("(")) {
                            S.push(P.pop());
                        }
                        P.pop();
                        E.pop();
                        break;
                    default:
                        S.push(E.pop());
                }
            }

            //Eliminacion de `impurezas´ en la expresiones algebraicas
            infix = expr.replace(" ", "");
            postfix = S.toString().replaceAll("[\\]\\[,]", "");
            postfix = postfix.replace(" ", "");
        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
        return postfix;
    }
    //Depurar expresión algebraica
    private String depurar(String s) {
        s = s.replaceAll("\\s+", ""); //Elimina espacios en blanco
        s = "(" + s + ")";
        String simbols = "*|().";
        String str = "";

        //Deja espacios entre operadores
        for (int i = 0; i < s.length(); i++)
            str += simbols.contains("" + s.charAt(i))? (" " + s.charAt(i) + " "):s.charAt(i);

        return str.replaceAll("\\s+", " ").trim();
    }

    //Jerarquia de los operadores
    private int pref(String op) {
        int prf = 99;
        if (op.equals("*")) prf = 5;
        if (op.equals(".")) prf = 4;
        if (op.equals("|")) prf = 3;
        if (op.equals(")")) prf = 2;
        if (op.equals("(")) prf = 1;
        return prf;
    }

    private String addConcatenation(String regexp){
        //Agregacion de los puntos de concatenacion
        regexp = regexp.replace(" ", "");
        String symbols = "", sp, s;
        //Busqueda de los simbolos
        for (int i=0; i<regexp.length(); i++)
            if (!"|*( )".contains(String.valueOf(regexp.charAt(i))) && !symbols.contains(String.valueOf(regexp.charAt(i))))
                symbols += String.valueOf(regexp.charAt(i));
        //Adicion de los puntos
        for (int i=1; i<regexp.length(); i++){
            sp = (i>0)? String.valueOf(regexp.charAt(i-1)): "";     // anterior, siempre que no se esté en 0
            s = String.valueOf(regexp.charAt(i));           // actual
            if (!s.equals(" "))
                if ((symbols.contains(sp) && symbols.contains(s)) || ("*)".contains(sp) && symbols.contains(s)) ||
                    (symbols.contains(sp) && s.equals("(")) || (sp.equals(")") && s.equals("(")) || (sp.equals("*") && s.equals("(")))
                        regexp = regexp.substring(0, i) + "." + regexp.substring(i, regexp.length());
        }
        return regexp;
    }

    public void generateAFDImage(String dotPath, String fileInputPath, String fileOutputPath){
        try {
            //String dotPath = "C:\\Program Files (x86)\\graphviz 2.38\\release\\bin\\dot.exe";
            //String fileInputPath = "C:\\Users\\timothy\\Documents\\UNIVERSITY\\CYCLES VI\\COMPILADORES I\\Laboratorios\\Laboratorio 5\\AfnConverter\\src\\afnconverter\\grafo1.txt";
            //String fileOutputPath = "C:\\Users\\timothy\\Documents\\UNIVERSITY\\CYCLES VI\\COMPILADORES I\\Laboratorios\\Laboratorio 5\\AfnConverter\\src\\afnconverter\\grafo1.jpg";

            String tParam = "-Tjpg";
            String tOParam = "-o";

            String[] cmd = new String[5];
            cmd[0] = dotPath;
            cmd[1] = tParam;
            cmd[2] = fileInputPath;
            cmd[3] = tOParam;
            cmd[4] = fileOutputPath;

            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);
        }
        catch (Exception ex) {
            System.out.println("Ocurrió un error en la generacion del grafo..!");
            ex.printStackTrace();
        }
        finally {}
  }
}
