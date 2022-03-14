import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class App {

    // Class fields
    private static final TrieNode root = new TrieNode();


    /**
     * Inserts the word s in the trie
     * @param s : the word to insert
     */
    public static void insert(String s) {
        TrieNode current = root;

        for (char l: s.toCharArray()) {
            current = current.getChildren().computeIfAbsent(l, c -> new TrieNode(l));
        }
        current.setEndOfWord(true);
    }

    /**
     * Checks whether the string p is a prefix of a word in the dictionary
     * @param p : the string to check
     *
     * @return true if p is a prefix of any word in the trie, else false
     */
    public static boolean isPrefix(String p) {
        // Initiating a temp node to loop
        TrieNode node = root;

        // Loping over string characters
        for (int i = 0; i < p.length(); ++i) {
            node = node.getChildren().get(p.charAt(i));
            if (node == null)
                return false;
        }

        // Returning result
        return true;
    }


    /**
     * Determines all words that are prefixed by p
     * @param p : the prefix
     *
     * @return String[]
     */
    public static String[] allWordsPrefix(String p) {
        if (!isPrefix(p))
            return new String[0];
        else {
            // Initiating a temp node to loop
            TrieNode node = root;

            // Initiating stacks and strings lists
            Stack<TrieNode> stack1 = new Stack<>();
            Stack<String> stack2 = new Stack<>();
            ArrayList<String> found = new ArrayList<>();

            // Loping over string characters
            for (int i = 0; i < p.length(); ++i) {
                node = node.getChildren().get(p.charAt(i));
            }
            stack1.push(node);
            stack2.push(p);
            while (!stack1.empty() && !stack2.empty()) {
                node = stack1.pop();
                p = stack2.pop();
                // If it's found, adding it to the list
                if (node.isEndOfWord()) {
                    found.add(p);
                }
                for (TrieNode child : node.getChildren().values()) {
                    if (child != null) {
                        stack1.push(child);
                        stack2.push(p + child.getC());
                    }
                }
            }

            // Returning result
            return found.toArray(String[]::new);
        }

    }


    /**
     * Loads data from dictionary file and adds it to the current trie
     */
    public static ArrayList<String> loadDataFromFile() throws IOException {
        // Initiating a file object
        File file = new File("dictionary.csv");

        // Initiating an arraylist of strings that will store dictionary file content
        ArrayList<String> dictionary = new ArrayList<>();

        // Initiating a BufferedReader to file
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Reading file content line by line
        String line;
        while ((line = br.readLine()) != null){
            String word = line.split(",")[0].trim();
            insert(word.toUpperCase());
            dictionary.add(line);
        }

        return dictionary;
    }

    /**
     * Saves search history to a file for 3 days.
     *
     * @param word : the word searched to be saved
     */
    public static void saveToHistory(String word) throws IOException {
        FileWriter fw = new FileWriter("history.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(word);
        bw.newLine();
        bw.close();
    }

    /**
     * Tests the search of some words provided in the file testPrefix
     *
     */
    private static void test() throws IOException {
        // Initiating a file object
        File file = new File("testPrefix.txt");

        // Initiating a BufferedReader to file
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Initiating a FileWriter to file
        FileWriter fw = new FileWriter("testPrefixResult.txt", true);

        // Initiating a BufferedWriter to file
        BufferedWriter bw = new BufferedWriter(fw);

        // Reading file content line by line
        String line;
        while ((line = br.readLine()) != null){
            // Getting all the words with the same prefix
            String[] samePrefix;
            int size = line.trim().length();
            while(size > 0){
                String prefix = line.toUpperCase().substring(0, size);
                samePrefix = allWordsPrefix(prefix);
                if(samePrefix.length > 0 || size == 1){
                    // Writing the number of the words with the same prefix in testPrefixResults file
                    bw.write(Integer.toString(samePrefix.length));
                    break;
                }
                else
                    size--;
            }
            bw.newLine();
        }

        bw.close();

    }

    // Driver
    public static void main(String[] args) throws IOException {
        // Initiating a scanner for user input
        Scanner keyboard = new Scanner(System.in);

        // Loading data from the dictionary file
        ArrayList<String> dictionary = loadDataFromFile();

        while(true){
            // Displaying options menu for the user
            System.out.println("-----------------");
            System.out.println("Enter your choice");
            System.out.println("1. Search word");
            System.out.println("2. Print history on the terminal");
            System.out.println("3. Translate the word");
            System.out.println("4. Run tests");
            System.out.println("0. Exit");

            // Reading user input
            System.out.print("Choice: ");
            int choice = keyboard.nextInt();
            keyboard.nextLine(); // Eliminating 'enter' key pressed by the user

            // Handling user input
            switch (choice) {
                case 1:
                    // Prompting the user to select an action
                    System.out.print("Enter a word to search> ");
                    String word = keyboard.nextLine().toLowerCase();

                    // Initiating a boolean variable to store whether the word was found or not
                    boolean found = false;

                    // Looping over all words in the dictionary
                    for (String w : dictionary) {
                        if (w.split(",")[0].toLowerCase().equals(word)) {
                            found = true;
                            if (w.indexOf("\"") > 0)
                                System.out.println("Meaning: " + w.split("\"")[1]);
                            else
                                System.out.println("Meaning: " + w.split(",")[3]);
                            System.out.println("Type: " + w.split(",")[2]);
                            saveToHistory(w);
                            break;
                        }
                    }

                    // If the word is not found, display all words with the same prefix
                    if (!found) {
                        System.out.println("The word " + word + " can't be found in the dictionary! Here is some similar words :");
                        int size = word.length();
                        String[] samePrefix;
                        while (size > 0) {
                            samePrefix = allWordsPrefix(word.toUpperCase().substring(0, size));
                            if (samePrefix.length > 0) {
                                for (String w : samePrefix) {
                                    System.out.println(w);
                                }
                                break;
                            } else
                                size--;
                        }
                    }
                    break;
                case 2:
                    // Initiating a file object
                    File file = new File("history.txt");

                    // Initiating a BufferedReader to file
                    BufferedReader br = new BufferedReader(new FileReader(file));

                    // Reading file content line by line
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.print("Word: " + line.split(",")[0] + ", ");
                        System.out.print("French translation: " + line.split(",")[1] + ", ");
                        System.out.print("Type: " + line.split(",")[2] + ", ");
                        if (line.indexOf("\"") > 0)
                            System.out.println("Meaning: " + line.split("\"")[1]);
                        else
                            System.out.println("Meaning: " + line.split(",")[3]);
                        System.out.println();
                    }
                    break;

                case 3:
                    // Prompting the user to enter a word to translate
                    System.out.print("Enter the word to translate: ");
                    word = keyboard.nextLine();

                    // Initiating a boolean variable to store whether the word was found or not
                    found = false;

                    // Looping over all words in the dictionary
                    for (String w : dictionary) {
                        if (w.split(",")[0].toLowerCase().equals(word.toLowerCase())) {
                            found = true;
                            System.out.println(word + ": " + w.split(",")[1]);
                            break;
                        }
                    }

                    // If the word is not found, display a message
                    if (!found) {
                        System.out.println("The word " + word + " can't be found in the dictionary");
                    }
                    break;

                case 4:
                    test();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Wrong choice! Please try again!");
            }
            System.out.println();
        }
    }
}