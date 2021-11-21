import java.io.*;
import java.nio.file.*;

public class Main {
    public static String readFile(String filename) throws IOException {
        String source;
        System.out.println(Path.of(filename));
        source = Files.readString(Path.of(filename));
        return source;
    }

    public static void main(String[] args) {
        String source;
        System.out.println(args[0]);
        try {
            source = readFile(args[0]);
        } catch(IOException e) {
            System.out.println(e);
            return;
        }

        try {
            Lexer l = new Lexer();
            l.parseCode(source);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
