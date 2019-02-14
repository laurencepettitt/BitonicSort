package sort;

import java.io.InputStream;
import java.util.Scanner;

class Reader {
    public final Scanner sr;

    public Reader(InputStream in) {
        sr = new Scanner(in);
    }

    public int Read() {
        if (sr.hasNextInt()) return sr.nextInt();
        else return -1;
    }
}

public class Sort {
    public static void main(String[] args) {

    }
}
