import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        String source = "";
        try {
            source = Files.readString(Path.of(args[0]));
        } catch (IOException e) {
            System.err.println(e);
        }

        Lexer l = new Lexer(JSLang.keywords, JSLang.operators, JSLang.punctuation);

        try {
            l.parseCode(source);
        } catch (Exception e) {
            System.out.println("Failed to parse file. Error: " + e);
        }
    }
}
