package edu.guilford;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeSet;

public class WordCountDriver {
    // *
    // * This program will read a file and count the number of times each word
    // appears
    // * in the file. It will then write the words and their counts to a new file.
    // * Finally, it will prompt the user for a word to search for, and then search
    // * for it. If the word is found, return it along with the number of times it
    // * appears in the file. If it is not found, return a message saying so.
    //

    public static void main(String[] args) {
        // Prompt the user for the name of the file they would like to sort.
        // Try to read the file and store it in a 2D array.
        // If the file is not found, print an error message and exit the program.

        Scanner scan = new Scanner(System.in);
        Scanner fileScan = null;

        Path dataLocation = null;
        String fileName = null;
        String[][] words = null;

        try {
            System.out.println("Please enter the name of the file you would like to sort: ");
            fileName = scan.nextLine();
            dataLocation = Paths.get(WordCountDriver.class.getResource("/" + fileName).toURI());
            FileReader fileReader = new FileReader(dataLocation.toString());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            fileScan = new Scanner(bufferedReader);
            words = readData(fileScan);
        } catch (URISyntaxException | FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("File not found.");
            System.exit(1);
        }
        // now we have the words in a 2D array, we can write them to a file
        // we need to create a new file, and then write the words to it
        // we can use the same path as the original file, but with a different name
        // we can use the same scanner to read the words from the 2D array
        // we can use a BufferedWriter to write the words to the new file
        // if there is an error, print an error message and exit the program

        try {
            writeData(words, "output.txt");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Prompt the user for a word to search for.
        // If the word is found, print it along with the number of times it appears in
        // the file.
        System.out.println("Please enter a word to search for: ");
        String searchWord = scan.nextLine();
        int count = 0;

        for (int i = 0; i < words.length; i++) {
            if (searchWord.equals(words[i][0])) {
                count = Integer.parseInt(words[i][1]);
            }
        }

        if (count > 0) {
            System.out.println("The word '" + searchWord + "' appears " + count + " times in the file.");
        } else {
            System.out.println("The word '" + searchWord + "' does not appear in the file.");
        }

    }
    // Create a method to read the file and store it in a 2D array.
    // Create a method to write the words to a new file.
    // Create a method to search for a word in the file.

    public static String[][] readData(Scanner fileScan) {

        Stack<String> stackTemp = new Stack<String>();
        TreeSet<String> organizedWords = new TreeSet<String>();

        // While loop to read the file to the stack, and modify it according to the
        // specifications.

        while (fileScan.hasNext()) {
            String temp = fileScan.next();
            // Remove all punctuation from the string
            temp = temp.replaceAll("[^a-zA-Z0-9]", "");
            // Remove all numbers from the string
            temp = temp.replaceAll("[0-9]", "");
            // Convert all letters to lowercase
            temp = temp.toLowerCase();
            // Push the modified string to the stack
            stackTemp.push(temp);
        }

        // Loop over the stack and count the number of times each word appears.
        // Add each word and its count to a TreeSet.

        while (!stackTemp.isEmpty()) {
            String word = stackTemp.pop();
            int count = 1; // The word will always appear at least once.

            for (int i = 0; i < stackTemp.size(); i++) {
                if (word.equals(stackTemp.get(i))) {
                    count++;
                }
            }

            // Create a WordCount object and add it to the TreeSet.
            // The TreeSet will automatically sort the words by count.
            WordCount wordCount = new WordCount(word, count);
            organizedWords.add(wordCount.getWord() + " " + String.valueOf(wordCount.getCount()));

            while (stackTemp.contains(word)) {
                stackTemp.remove(word);
            }

        }
        // Convert the TreeSet to a 2D array.
        // The first column will contain the words, and the second column will contain
        // the counts.

        String[][] words = new String[organizedWords.size()][2];
        int i = 0;

        for (String word : organizedWords) {
            words[i][0] = word.substring(0, word.indexOf(" "));
            words[i][1] = word.substring(word.indexOf(" ") + 1);
            i++;
        }

        return words;
    }

    // Create a method to wrtie the words to a new file.

    public static void writeData(String[][] words, String location) throws URISyntaxException, IOException {
        // Create a new file in the same folder as the original file
        // Get the path to the folder
        // This is a relative path, so it will work on any computer
        Path locationPath = Paths.get(WordCountDriver.class.getResource("/edu/guilford/").toURI());
        // Open a file in that folder
        FileWriter fileLocation = new FileWriter(locationPath.toString() + "/" + location);
        BufferedWriter bufferWrite = new BufferedWriter(fileLocation);
        // Write the data to the file
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[0].length; j++) {
                bufferWrite.write(words[i][j] + " ");
            }
            bufferWrite.newLine();
        }
        // Close the file after writing
        // This is important!
        bufferWrite.close();
    }
    // create a WordCount class to store the words and their counts
    // this class implements the Comparable interface so that the TreeSet can sort
    // the words by count
    // the compareTo method is used to compare the counts of two WordCount objects
    // the TreeSet will sort the words in ascending order by count

    public static class WordCount implements Comparable<WordCount> {
        private String word;
        private int count;

        public WordCount(String word, int count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return this.word;
        }

        public int getCount() {
            return this.count;
        }

        @Override
        public int compareTo(WordCount wordCount) {
            if (this.count > wordCount.getCount()) {
                return 1;
            } else if (this.count < wordCount.getCount()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}