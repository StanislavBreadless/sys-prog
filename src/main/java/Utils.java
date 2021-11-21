public class Utils {

    public static boolean contains(String[] arr, String elem) {
        for (String hay: arr) {
            if (hay.equals(elem)) return true;
        }
        return false;
    }

    public static String startsWithOneOf(String text, String[] words) {
        for (String word: words) {
            if (text.startsWith(word)) return word;
        }
        return null;
    }

    public static void log(String token, String type) {
        System.out.println(token + " - " + type);
    }

}


