import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;

public class Main {

    final static int MAX_WORD_LENGTH = 30;

    private static boolean isEnglishLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private static boolean isPartOfWord(char c) {
        return  isEnglishLetter(c) || c == '\'';
    }

    static StringBuilder builder = new StringBuilder();
    static Map<String, Integer> count = new HashMap<>();

    public static void endWord() {
        if(builder.isEmpty()) {
            return;
        }

        int strLen = min(builder.length(), MAX_WORD_LENGTH);

        String s = builder.substring(0, strLen);

        if(count.containsKey(s)) {
            count.put(s, count.get(s)+1);
        } else {
            count.put(s, 1);
        }

        builder.setLength(0);
    }

    public static void addChar(char c) {
        if(!isPartOfWord(c)) {
            endWord();
            return;
        }

        if (!builder.isEmpty() || isEnglishLetter(c)) {
            builder.append(c);
        } else {
            endWord();
        }
    }

    public static void main(String[] args) throws IOException {
        // replace this with a known encoding if possible
        Charset encoding = Charset.defaultCharset();
        String filename = args[0];
        String writeFile = args[1];
        File file = new File(filename);
        handleFile(file, encoding);

        saveToFile(writeFile);
    }

    public static void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            String s = entry.getKey() + ": " + entry.getValue() + "\n";
            writer.write(s);
        }

        writer.close();
    }

    private static void handleFile(File file, Charset encoding)
            throws IOException {
        try (InputStream in = new FileInputStream(file);
             Reader reader = new InputStreamReader(in, encoding);
             // buffer for efficiency
             Reader buffer = new BufferedReader(reader)) {
             handleCharacters(buffer);
        }
    }

    private static void handleCharacters(Reader reader)
            throws IOException {
        int r;
        while ((r = reader.read()) != -1) {
            char ch = (char) r;
            addChar(ch);
        }
    }

}
