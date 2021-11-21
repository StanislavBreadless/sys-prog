public class Lexer {
    private String buffer = "";

    private enum State {
        None,
        String,
        Number,
        Identifier,
        Comment,
        Error,
        End,
    }

    private final String[] keywords;
    private final String[] operators;
    private final String punctuation;

    Lexer(String[] keywords, String[] operators, String punctuation) {
        this.keywords = keywords;
        this.operators = operators;
        this.punctuation = punctuation;
    }

    public void parseCode(String text) throws Exception {
        int i = 0;
        while (state != State.End) {
            if (i >= text.length()) {
                if (state == State.None) {
                    state = State.End;
                } else {
                    state = State.Error;
                    throw new Exception("\nUnexpected end of file");
                }
                break;
            }

            char symbol = text.charAt(i);
            String operator;

            switch (state) {
                case None:
                    buffer = "";
                    if (Character.isWhitespace(symbol)) {
                    } else if (punctuation.contains(""+symbol)) {
                        Utils.log(""+symbol, "Punctuation");
                    } else if (symbol == '/' && text.length() > i+1 && text.charAt(i+1) == '/') {
                        state = State.Comment;
                        buffer += "//";
                        i++;
                    } else if ((operator = Utils.startsWithOneOf(text.substring(i), operators)) != null) {
                        Utils.log(operator, "Operator");
                        i += operator.length() - 1;
                    } else if ("$_".contains(""+symbol) || Character.isLetter(symbol)) {
                        state = State.Identifier;
                        buffer += symbol;
                    } else if ("\"'`".contains(""+symbol)) {
                        state = State.String;
                        buffer += symbol;
                    } else if (Character.isDigit(symbol)) {
                        state = State.Number;
                        buffer += symbol;
                    } else {
                        state = State.Error;
                        buffer += symbol;
                    }
                    i++;
                    break;
                case Identifier:
                    if (Character.isLetter(symbol) || Character.isDigit(symbol) || "$_".contains(""+symbol)) {
                        buffer += symbol;
                        i++;
                    } else {
                        if (Utils.contains(keywords, buffer)) {
                            Utils.log(buffer, "Keyword");
                        } else {
                            Utils.log(buffer, "Identifier");
                        }
                        state = State.None;
                    }
                    break;
                case String:
                    buffer += symbol;
                    if ("\"'`".contains(""+symbol)) {
                        Utils.log(buffer, "String");
                        state = State.None;
                    }
                    i++;
                    break;
                case Number:
                    if (Character.isDigit(symbol)) {
                        buffer += symbol;
                        i++;
                    } else if (symbol == '.') {
                        if (decimalPoint) {
                            state = State.Error;
                        } else {
                            decimalPoint = true;
                            buffer += symbol;
                            i++;
                        }
                    } else if ("eE".contains(""+symbol)) {
                        if (decimalExponent) {
                            state = State.Error;
                        } else {
                            decimalExponent = true;
                            buffer += symbol;
                            i++;
                        }
                    } if ("+-".contains(""+symbol)) {
                    char last = buffer.charAt(buffer.length()-1);
                    if ("eE".contains(""+last)) {
                        buffer += symbol;
                        i++;
                    } else if (last == '.') {
                        state = State.Error;
                    } else {
                        state = State.None;
                    }
                } else {
                    try {
                        Double.parseDouble(buffer);
                        Utils.log(buffer, "Number");
                        state = State.None;
                    } catch (NumberFormatException e) {
                        state = State.Error;
                    }
                }
                case Comment:
                    if (symbol != '\n') {
                        buffer += symbol;
                    } else {
                        state = State.None;
                    }
                    i++;
                    break;
                case Error:
                    System.err.println("\nInvalid token");
                    return;
                case End:
                    return;
            }
        }
    }



    private State state = State.None;
    private boolean decimalPoint = false;
    private boolean decimalExponent = false;
}
