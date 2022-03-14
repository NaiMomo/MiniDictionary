import java.util.HashMap;
import java.util.Map;

public class TrieNode {

    // Class fields
    private final Map<Character, TrieNode> children = new HashMap<>();
    private char c;
    private boolean endOfWord;

    public TrieNode(char c){
        this.c = c;
    }

    public TrieNode(){}

    Map<Character, TrieNode> getChildren() {
        return children;
    }

    boolean isEndOfWord() {
        return endOfWord;
    }

    void setEndOfWord(boolean endOfWord) {
        this.endOfWord = endOfWord;
    }

    public char getC() {
        return c;
    }
}