import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.NoSuchElementException;

/**
 * This class implements a WordTree. An alphabetically sorted red-black tree of nodes containing
 * the word, its score, its count, and the red or black value of the node.
 * Course:	COMP 2100
 * Assignment:	Project 3
 *
 * @author	Jacob Littler, Joel Justice
 * @version 	1.0, 11/03/2023
 */

public class WordTree {
    private static class Node {
        public String word;
        public int score;
        public int count;
        public boolean black;
        public Node left;
        public Node right;
    }
    Node root = null;

    /**
     * Method that adds a word and its score to the Word Tree
     * @param word that is being passed in from the file
     * @param score that is being passed in from the file
     */
    public void add(String word, int score){
        root = add(root, word, score);
        root.black = true;
    }

    /**
     * Checks to see if a word contains a root and a word
     * @param word being passed in
     * @return whether that word is within the tree
     */
    public boolean contains(String word){
        return contains(root, word);
    }

    /**
     * Gives the score of a phrase
     * @param word being passed in
     * @return Returns the score of the phrase that is inputted
     */
    public double getScore(String word){
        Node node = getNode(root, word);
        if (node != null) {
            return (double) node.score / node.count;
        }
        else{
            return 2;
        }
    }

    /**
     * Writes to the file when reading in the root.
     * @param out PrintWriter to write to a file
     */
    public void print( PrintWriter out ){
        print(root, out);
    }

    /**
     * Helper method for print. Does an inorder traversal in order to print the node's word, score, and count
     * @param node Node being printed
     * @param out Content being written to file
     */
    private static void print(Node node, PrintWriter out){
        if(node != null){
            print(node.left, out);
            out.println(node.word+ "\t" + node.score + "\t" + node.count);
            print(node.right, out);
        }
    }

    /**
     * Adds the node, word, and score into the Word Tree. Converts tree into a red-black implementation.
     * @param node Node being added
     * @param word String word the node contains
     * @param score int score associated with word
     * @return the node that is being added
     */
    private static Node add(Node node, String word, int score){
        if (node == null){
            node = new Node();
            node.word = word;
            node.score = score;
            node.count = 1;
            node.black = false;
        }
        else {
            int value = word.compareTo(node.word);
            if (value < 0){
                node.left = add(node.left, word, score);
            }
            else if (value > 0){
                node.right = add(node.right, word, score);
            }
            else {
                node.count += 1;
                node.score += score;
                //to get the value of the word, you average score by count
            }
            // left rotation
            if(node.right != null && !node.right.black){
                Node temp = node.right;
                node.right = temp.left;
                temp.left = node;
                boolean color = temp.black;
                temp.black = node.black;
                node.black = color;
                node = temp;
            }
            // right rotation
            if (node.left != null && node.left.left != null && !node.left.black && !node.left.left.black){
                Node temp = node.left;
                node.left = temp.right;
                temp.right = node;
                boolean color = temp.black;
                temp.black = node.black;
                node.black = color;
                node = temp;
            }
            // recolor
            if(node.left != null && node.right != null && !node.left.black && !node.right.black){
                node.left.black = true;
                node.right.black = true;
                node.black = false;
            }
        }
        return node;
    }

    /**
     * Checks if a word is contained within the Word Tree
     * @param node Node being checked
     * @param word String word being checked for
     * @return true if a word is within the tree
     */
    private static boolean contains(Node node, String word){
        if (node == null) {
            return false;
        }
        int value = word.compareTo(node.word);
        if (value < 0){
            return contains(node.left, word);
        }
        else if (value > 0){
            return contains(node.right, word);
        }
        return true;
    }

    /**
     * Returns the node what contains the word passed in. Recursively traverses the tree
     * based on the value of the current node's word compared to the word given.
     * @param node Node node being checked
     * @param word String being compared to other node's word value to see if equal
     * @return the node at a given word
     */
    private static Node getNode(Node node, String word){
        if (node == null) {
            return null;
        }
        int value = word.compareTo(node.word);
        if (value < 0){
            return getNode(node.left, word);
        }
        else if (value > 0){
            return getNode(node.right, word);
        }
        return node;
    }

}
