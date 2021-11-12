import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

public class Automata {

    HashSet<Integer> finalStates = new HashSet<>();

    public int s0;
    public int Asize;
    public int Ssize;
    public int Fsize;

    int to[][];

    public int getTo(int _to, int c) {
        return to[_to][c];
    }

    public void initialize(Scanner scan) {
        Asize = scan.nextInt();

        Ssize = scan.nextInt();

        s0 = scan.nextInt();


        Fsize = scan.nextInt();
        for(int i = 0 ; i < Fsize; i++) {
            int f = scan.nextInt();
            finalStates.add(f);
        }

        to = new int[Ssize][Asize];

        for(int i = 0; i < Ssize; i++) {
            for(int j = 0; j < Asize; j++) {
                to[i][j] = -1;
            }
        }

        while(scan.hasNextInt()) {
            int s = scan.nextInt();

            char c = scan.next().charAt(0);
            c -= 'a';
            int s1 = scan.nextInt();


            to[s][c] = s1;
        }
    }


    public Automata() {}

}
