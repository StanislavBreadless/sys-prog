import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class Lexer {
    public enum LexemeType {
        Whitespace("[\t\n\r ]+"),
        Number("\\b\\d+(\\.\\d+)?(e[+-]?\\d+)?\\b"),
        String("\"[^\"]*\"|'[^']*'|`[^`]*`"),
        Comment("//.*"),
        Keyword("\\b(break|case|catch|class|const|continue|debugger|default|delete|do|else|export|extends|finally|for|function|if|import|in|instanceof|let|new|return|super|switch|this|throw|try|typeof|var|void|while|with|yield)\\b"),
        Identifier("\\b[a-zA-Z_$][a-zA-Z_$0-9]*\\b"),
        Operator("(\\+\\+|\\+|--|-|\\*\\*|/|%|\\*|<<|<|>>>|>>|>|==|=|!=|!|&&|\\^|\\|\\||&|\\||\\?\\?)=?|\\?|~"),
        Punctuation("[;:,\\.\\{\\}\\[\\]\\(\\)]");

        public final String pattern;

        LexemeType(String pattern) {
            this.pattern = pattern;
        }
    };

    public void parseCode(String source) throws Exception {
        StringBuffer patternsBuffer = new StringBuffer();
        for (LexemeType type : LexemeType.values()) {
            patternsBuffer.append(String.format("|(%s)", type.pattern));
        }

        Pattern pattern = Pattern.compile(patternsBuffer.substring(1));
        Matcher matcher = pattern.matcher(source);

        int start = 0;
        while (matcher.find()) {
            if (start != matcher.start()) {
                throw new Exception("\nInvalid token: " + source.substring(start, matcher.start()));
            } else {
                start = matcher.end();
            }
            for (LexemeType type: LexemeType.values()) {
                if (matcher.group().matches(type.pattern)) {
                    if (type != LexemeType.Whitespace && type != LexemeType.Comment) {
                        System.out.println(matcher.group() + " - " + type.name());
                    }
                    if (type == LexemeType.Comment) {
                        System.out.println(type.name());
                    }
                    break;
                }
            }
        }
    }

}