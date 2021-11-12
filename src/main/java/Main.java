import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    static int[][] timing;
    static ArrayList<Integer>[][] parent;
    static int freeLeft;

    static ArrayList<ArrayList<Integer>> current = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> last = new ArrayList<>();

    static int maxA;

    static Automata auto1;
    static Automata auto2;
    static int auto1F;
    static int auto2F;

    public static void setTime(int a, int b, int t) {
        timing[a][b] = t;
    }

    public static void setTime(ArrayList<Integer> pair, int t){
        setTime(pair.get(0), pair.get(1), t);
    }

    // Returns true if the game ends
    public static boolean checkAndReset() {
        for(int i = 0; i < current.size(); i++) {
            freeLeft--;
            ArrayList<Integer> pair = current.get(i);

            int a = pair.get(0);
            int b = pair.get(1);

            if(auto1.finalStates.contains(a) && auto2.finalStates.contains(b)) {
                auto1F = a;
                auto2F = b;
                return true;
            }
        }
        last = current;
        current = new ArrayList<>();
        return false;
    }

    public static ArrayList<Integer> pair(int a, int b) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        return list;
    }

    public static ArrayList<Integer> triple(int a, int b, int c) {
        ArrayList<Integer> pp = pair(a,b);
        pp.add(c);
        return pp;
    }

    public static boolean iterate() {
        if(checkAndReset()) {
            return true;
        }

        if(freeLeft == 0 || last.size() == 0) {
            return false;
        }

        for(int i = 0; i < last.size(); i++) {
            ArrayList<Integer> pair = last.get(i);
            int a = pair.get(0);
            int b = pair.get(1);

            for(int j = 0; j < maxA; j++) {
                int to1 = auto1.getTo(a, j);
                int to2 = auto2.getTo(b, j);

                if(to1 == -1 || to2 == -1 || timing[to1][to2] != -1) {
                    continue;
                }

                timing[to1][to2] = timing[a][b] + 1;
                parent[to1][to2] = triple(a,b,j);
                current.add(pair(to1,to2));
            }
        }

        return false;
    }

    public static String backIterate() {
        int cur1 = auto1F;
        int cur2 = auto2F;

        StringBuilder sb = new StringBuilder();

        while(cur1 != auto1.s0 || cur2 != auto2.s0) {
            ArrayList<Integer> pp = parent[cur1][cur2];
            cur1 = pp.get(0);
            cur2 = pp.get(1);
            sb.append((char)('a'+pp.get(2)));
        }

        return sb.reverse().toString();
    }

    public static void out(String s) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter("out.txt")) {
            out.println(s);
        }
    }

    public static void main(String argv[]) throws FileNotFoundException {
        Scanner scan = new Scanner(new File("tall.txt"));

        auto1 = new Automata();
        auto2 = new Automata();

        auto1.initialize(scan);
        scan.next();
        auto2.initialize(scan);

        timing = new int[auto1.Ssize][auto2.Ssize];
        parent = new ArrayList[auto1.Ssize][auto2.Ssize];
        maxA = Math.min(auto1.Asize, auto2.Asize);

        for (int i = 0; i < auto1.Ssize; i++) {
            for (int j = 0; j < auto2.Ssize; j++) {
                timing[i][j] = -1;
            }
        }

        freeLeft = auto1.Ssize * auto2.Ssize;
        current.add(pair(auto1.s0, auto2.s0));
        setTime(current.get(0), 0);

        while(!iterate()) {
            if(freeLeft == 0 || last.size() == 0) {
                out("No non-empty words are possible in both!");
                return;
            }
        }

        out("The shortest possible word is:" + backIterate());
    }

}

/*

1
2
0
1 1
0 a 1
1
2
0
1 1
0 a 1

 */