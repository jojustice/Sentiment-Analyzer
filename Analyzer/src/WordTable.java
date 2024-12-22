import java.io.PrintWriter;
/**
 * This class implements a WordTable. It is a table sorted in alphabetical order. There are 26
 * WordTrees in the WordTable, corresponding with the starting letter.
 * Course:	COMP 2100
 * Assignment:	Project 3
 *
 * @author	Jacob Littler, Joel Justice
 * @version 	1.0, 11/03/2023
 */
public class WordTable {

    WordTree[] table = new WordTree[26];

    /**
     * Constructor. Adds new WordTree to each index in the WordTable.
     */
    public WordTable() {
        for (int i = 0; i < table.length; ++i){
            table[i] = new WordTree();
        }
    }

    /**
     * adds the word and score to the Word Table
     * @param word String word
     * @param score int score of given word from review file
     */
    public void add(String word, int score){
        word = word.toLowerCase();
        int index = word.charAt(0) - 'a';
        table[index].add(word,score);
    }

    /**
     * Gives the cumulative score divided by the number of occurances.
     * @param word String word
     * @return double score
     */
    public double getScore(String word){
        word = word.toLowerCase();
        int index = word.charAt(0) - 'a';
        return table[index].getScore(word);
    }

    /**
     * Checks if the WordTree at the WordTable index contains a given word
     * @param word String word
     * @return boolean if the tree contains the word
     */
    public boolean contains(String word){
        word = word.toLowerCase();
        int index = word.charAt(0) - 'a';
        return table[index].contains(word);
    }

    /**
     * Prints to a file
     * @param out PrintWriter to write to a file
     */
    public void print(PrintWriter out){
        for (int i = 0; i < table.length; ++i){
            table[i].print(out);
        }
    }
}
