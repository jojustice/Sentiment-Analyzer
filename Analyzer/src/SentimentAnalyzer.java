import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This class contains a static main method that gives prompts to a user for a stop and review file.
 * The review file will then be parsed using a regular expression pattern. The words from the review
 * file will be saved in a word table of word trees, with the cumulative score from the review as well as
 * the count of how many times the word has occurred in reviews. The user can then have a phrase
 * reviewed and the score of their phrase will be returned, as well as a positive, negative, or
 * neutral charge.
 * Course:	COMP 2100
 * Assignment:	Project 3
 *
 * @author	Jacob Littler, Joel Justice
 * @version 	1.0, 11/03/2023
 */
public class SentimentAnalyzer {
    //uses a regular expression to create parameters for identifying what needs to be parsed
    private static final Pattern PATTERN = Pattern.compile("([a-z]+['-]*)*[a-z]+");

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        WordTable stopTable = null;
        WordTable reviewTable = null;
        boolean success = false;
        while(!success) {
            System.out.print("Enter a stop word file: ");
            String stopFile = in.nextLine();
            System.out.print("Enter a review file: ");
            String reviewFile = in.nextLine();
            String written = reviewFile + ".words";
            System.out.println("Word statistics written to file " + written + "\n");
            stopTable = new WordTable();
            reviewTable = new WordTable();
            try {
                Scanner stopReader = new Scanner(new File(stopFile));
                Scanner reviewReader = new Scanner(new File(reviewFile));
                while (stopReader.hasNext()) {
                    stopTable.add(stopReader.nextLine(), 0); //stop file
                }
                stopReader.close();
                reviewParse(reviewReader, stopTable, reviewTable);
                reviewReader.close();
                outputStatistics(reviewTable, written);
                success = true;
            } catch (FileNotFoundException e) {
                System.out.println("One or both of your files cannot be read, please re-enter them.");
            }
        }

        System.out.print("Would you like to analyze a phrase? (yes/no) ");
        String analyze = in.nextLine();

        while(!analyze.equalsIgnoreCase("no")) {
            if (analyze.equalsIgnoreCase("yes")) {
                System.out.print("Enter Phrase: ");
                String phrase = in.nextLine();
                phraseScore(phrase, stopTable, reviewTable);
            }
            System.out.print("Would you like to analyze a phrase? (yes/no) ");
            analyze = in.nextLine();
        }
    }

    /**
     * Parses the review file using the regular expression Pattern matcher.
     * @param reader Scanner reads the file
     * @param stop WordTable containing the stop words
     * @param review WordTable containing review words
     */
    public static void reviewParse(Scanner reader, WordTable stop, WordTable review) {
        while(reader.hasNextLine()){
            int rating = reader.nextInt();
            String line = reader.nextLine().toLowerCase();
            Matcher m = PATTERN.matcher(line);
            while (m.find()) {
                String word = m.group();
                if (!stop.contains(word)){
                    review.add(word, rating);
                }
            }
        }
    }

    /**
     * Prints the phrase's score. Averages the average score of the words that are contained in the
     * review table. The word has a neutral score of 2.0 if the word is not in either the stop nor the
     * review file.
     * @param phrase String phrase
     * @param stop This is the stop file that is stored in the WordTable
     * @param reviewTable This is the review file that is stored in the WordTable
     */
    public static void phraseScore(String phrase, WordTable stop, WordTable reviewTable){
        Matcher m = PATTERN.matcher(phrase);
        double score = 0;
        int number = 0;

        while (m.find()) {
            String word = m.group();
            if (!stop.contains(word)) {
                score += reviewTable.getScore(word);
                ++number;
            }
        }
        if (number > 0){
            score = score/number;
            System.out.printf("Score: %.3f%n" ,score);
            String charge;
            if (score < 2.000) {
                charge = "negative";
            } else if (score > 2.000) {
                charge = "positive";
            } else {
                charge = "perfectly neutral";
            }
            System.out.println("Your phrase was " + charge + ".");
        }
        else{
            System.out.println("Your phrase contained no words that affect sentiment.");
        }
    }

    /**
     * File writer. Writes the necessary output to the review file.
     * @param review WordTable review file words
     * @param written String name of file
     * @throws FileNotFoundException if file is nonexistent
     */
    public static void outputStatistics(WordTable review, String written) throws FileNotFoundException {
        PrintWriter reviewFileWriter = new PrintWriter(written);
        review.print(reviewFileWriter);
        reviewFileWriter.close();
    }
}