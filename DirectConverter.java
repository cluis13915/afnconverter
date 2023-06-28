import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DirectConverter {
    private ArrayList<String> symbols;
    private Set<TreeNode> auxNodes;
    private String treeValues;
    private ArrayList<TreeNode> nodes;

    public DirectConverter() {
        nodes = new ArrayList();
    }

    // Metodo privado que genera el arbol sintactico de la expresion. Devuelve la raiz del arbol.
    private TreeNode generateSintaxTree(String regexp) {
        int index = 0;
        Stack<TreeNode> subTrees = new Stack();
        TreeNode newNode, auxNode1;

        // Paso 1: Encontrar todos los simbolos del alfabeto
        regexp = regexp + "#.";     // Agregar # a la expresion
        this.searchSymbols(regexp);

        // Paso 2: Generacion del arbol sintactico
        for (String s: regexp.split("")) {
            if (symbols.contains(s) || s.equals("&")) {
                index++;
                subTrees.push(new TreeNode(s, index));
            }
            else if(s.equals(".") || s.equals("+")) {
                newNode = new TreeNode(s, -1);
                auxNode1 = subTrees.pop();
                newNode.setLeftNode(subTrees.pop());
                newNode.setRightNode(auxNode1);
                subTrees.push(newNode);
            }
            else if(s.equals("*")) {
                newNode = new TreeNode(s, -1);
                newNode.setLeftNode(subTrees.pop());
                subTrees.push(newNode);
            }
        }
        return subTrees.pop();
    }

    //metodo recursivo para recorrido posorden
    private void findComponentsNodes(TreeNode node) {
        if (node == null)
            return;

        // Recursividad para encontrar los componenetes de las otras hojas
        findComponentsNodes(node.getLeftNode());       // Recorrer hoja izquierdo
        findComponentsNodes(node.getRightNode());      // Recorrer hoja derecha

        // Propiedades de un nodo epsilon
        if (node.getValue().equals("&")) {
            node.setNullable(true);
            node.setFirstPos(null);
            node.setLastPos(null);
        }
        // Propiedades de un nodo con simbolo
        else if (symbols.contains(node.getValue())) {
            node.setNullable(false);
            // Conjunto fistpos y lastpos para el nodo
            auxNodes = new HashSet();
            auxNodes.add(node);
            node.setFirstPos(auxNodes);
            node.setLastPos(auxNodes);
        }
        // Propiedades de un nodo OR
        else if (node.getValue().equals("+")) {
            node.setNullable(this.isNullable(node.getLeftNode()) || this.isNullable(node.getRightNode()));
            // Conjunto firstpos para el nodo
            auxNodes = new HashSet();
            auxNodes.addAll(node.getLeftNode().getFirstPos());
            auxNodes.addAll(node.getRightNode().getFirstPos());
            node.setFirstPos(auxNodes);
            // Conjunto lastpos para el nodo
            auxNodes = new HashSet();
            auxNodes.addAll(node.getLeftNode().getLastPos());
            auxNodes.addAll(node.getRightNode().getLastPos());
            node.setLastPos(auxNodes);
        }
        // Propiedades de un nodo AND
        else if (node.getValue().equals(".")) {
            node.setNullable(this.isNullable(node.getLeftNode()) && this.isNullable(node.getRightNode()));
            // Conjunto fistpos para el nodo
            if (this.isNullable(node.getLeftNode())) {
                auxNodes = new HashSet();
                auxNodes.addAll(node.getLeftNode().getFirstPos());
                auxNodes.addAll(node.getRightNode().getFirstPos());
                node.setFirstPos(auxNodes);
            }
            else
                node.setFirstPos(node.getLeftNode().getFirstPos());
            // Conjunto lastpos para el nodo
            if (this.isNullable(node.getRightNode())) {
                auxNodes = new HashSet();
                auxNodes.addAll(node.getLeftNode().getLastPos());
                auxNodes.addAll(node.getRightNode().getLastPos());
                node.setLastPos(auxNodes);
            }
            else
                node.setLastPos(node.getRightNode().getLastPos());
        }
        else if (node.getValue().equals("*")) {
            node.setFirstPos(node.getLeftNode().getFirstPos());
            node.setLastPos(node.getLeftNode().getLastPos());
        }
    }

    //Metodo recursivo para encontrar el followPos de todos los nodos
    private void findFollowPos(TreeNode node) {
        if (node == null)   // Si es un nodo final
            return;

        if (node.getValue().equals(".")) {   // FollowPos para un nodo AND
            for (TreeNode lastPos: node.getLeftNode().getLastPos()) {
                lastPos.setFollowPos(node.getRightNode().getFirstPos());
            }
        }
        else if (node.getValue().equals("*")) {  // FollowPos para un nodo OR
            for (TreeNode lastPos: node.getLeftNode().getFirstPos()) {
                lastPos.getFollowPos().addAll(node.getLeftNode().getLastPos());
            }
        }

        findFollowPos(node.getLeftNode());    // recorre subarbol izquierdo
        findFollowPos(node.getRightNode());   // recorre subarbol derecho
    }

    // Metodo para verificar si un nodo es Nullable
    private boolean isNullable(TreeNode node) {
        if (node.getValue().equals("*") || node.getValue().equals("&")) // es nullable solo si es un * o un &
            return true;
        return false;
    }

    public AFD convert(String regexp) {
        TreeNode root = this.generateSintaxTree(regexp);    // Generate Sintax Tree
        this.findComponentsNodes(root);     // For each Sintax Tree node, find firstPos and last Pos
        this.findFollowPos(root);       // For each Sintax Tree node, find followPos
        //this.PreOrder_Method(root);

        // Construccion del AFD
        StateAFD U, S;
        int label = 1;
        ArrayList<TransitionAfd> Dtran = new ArrayList();

        // Initialize Dstates to contain only the unmarked state firstpos(root)
        ArrayList<StateAFD> Dstates= new ArrayList();
        U = new StateAFD(label, true, false);
        U.getNodes().addAll(root.getFirstPos());
        U.setMarked(false);
        Dstates.add(U);

        symbols.remove("#");
        boolean thereIsUnmarked = true;
        S = Dstates.get(0);
        while (thereIsUnmarked) {     // there is an unmarked state S in Dstates
            S.setMarked(true);      // mark S
            for (String a: symbols) {        // for each imput symbol a
                // let U be the union of followpos (p) for all p in S that correspond to a
                U = new StateAFD(label+1,false,false);
                for (TreeNode node: S.getNodes()) {
                    if (node.getValue().equals(a)) {
                        U.getNodes().addAll(node.getFollowPos());
                    }
                }
                // if U is not in Dstates
                StateAFD auxState = this.contain(U, Dstates);
                if (auxState == null) {
                    label++;
                    Dstates.add(U);
                }
                else{
                    U = auxState;
                }
                Dtran.add(new TransitionAfd(S,a,U));     // Dtran[S,a] = U
            }

            // Search for a new unmarked state in Dstates
            thereIsUnmarked = false;
            for (StateAFD state: Dstates) {
                if (!state.isMarked()) {
                    S = state;
                    thereIsUnmarked = true;
                    break;
                }
            }

        }

        // Construct the AFD
        AFD afd = new AFD();
        afd.setSymbols(symbols);
        afd.setStates(Dstates);
        afd.setTransitions(Dtran);
        afd.refreshAFD();       // Find the other elements
        return afd;
        //this.PreOrder_Method(root);

    }

    private StateAFD contain(StateAFD state, ArrayList<StateAFD> Dstates) {
        for (StateAFD s: Dstates) {
            if (s.getNodes().containsAll(state.getNodes()) && state.getNodes().containsAll(s.getNodes()))
                return s;
        }
        return null;
    }
    //metodo recursivo para recorrido en preorden
    private void printIndexes(TreeNode node) {
        if (node == null)
            return;
        if (symbols.contains(node.getValue()))
            treeValues += String.valueOf(node.getIndex()) + " ";    // Obtener valor del nodo
        printIndexes(node.getLeftNode());    // recorre subarbol izquierdo
        printIndexes(node.getRightNode());   // recorre subarbol derecho
    }

    //metodo recursivo preorden para recorrer el arbol
    private void PreOrder_Method(TreeNode node) {
        if (node == null)
            return;
        System.out.println("Nodo con valor: " + node.getValue());
        System.out.println("    Es nullable? " + node.isNullable());
        System.out.println("    FirstPos:\n\t\t" + node.getFirstPosToString());
        System.out.println("    LastPos:\n\t\t" + node.getLastPosToString());

        if (symbols.contains(node.getValue())) {
            //nodes.add(node);
            System.out.println("    FollowPos:\n\t\t" + node.getFollowPosToString());
        }
        //treeValues += node.getValue() + " ";    // Obtener valor del nodo
        PreOrder_Method(node.getLeftNode());    // recorre subarbol izquierdo
        PreOrder_Method(node.getRightNode());   // recorre subarbol derecho
    }

    private void searchSymbols(String regexp) {
        String specialChars = "*+.&";
        symbols = new ArrayList();
        // Simbolos del alfabeto
        for (String s: regexp.split(""))
            if (!specialChars.contains(s) && !symbols.contains(s))
                symbols.add(s);
    }
}
