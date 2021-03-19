import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        int n = s.length();
        index = new int[n];
        CircularSuffix[] suffixes = new CircularSuffix[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new CircularSuffix(s, i);
        }
        Arrays.sort(suffixes);
        for (int i = 0; i < n; i++) {
            this.index[i] = suffixes[i].index;
        }
    }

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        private final String text;
        private final int index;

        private CircularSuffix(String text, int index) {
            this.text = text;
            this.index = index;
        }

        private char getChar(int i) {
            return text.charAt((this.index + i) % this.text.length());
        }

        private int getLength() {
            return text.length();
        }

        @Override
        public int compareTo(CircularSuffix that) {
            if (this == that) return 0;  // optimization
            int n = getLength();
            for (int i = 0; i < n; i++) {
                if (this.getChar(i) < that.getChar(i)) return -1;
                if (this.getChar(i) > that.getChar(i)) return +1;
            }
            return 0;
        }
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= index.length) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray a = new CircularSuffixArray(s);
        StdOut.println("Length of string : " + a.length());
        for (int i = 0; i < s.length(); i++) {
            StdOut.println("index[" + i + "] : " + a.index(i));
        }
    }
}