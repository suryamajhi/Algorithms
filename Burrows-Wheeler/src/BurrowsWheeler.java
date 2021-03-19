import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class BurrowsWheeler {


    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);
        char[] chars = new char[s.length()];
        int initial = 0;
        for (int i = 0; i < s.length(); i++) {
            if (suffixArray.index(i) == 0) {
                initial = i;
                chars[i] = s.charAt(s.length() - 1);
                continue;
            }
            int val = suffixArray.index(i);
            chars[i] = s.charAt(val - 1);
        }
        BinaryStdOut.write(initial);
        BinaryStdOut.write(String.valueOf(chars));
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int index = BinaryStdIn.readInt();

        String s = BinaryStdIn.readString();
        char[] t = s.toCharArray();

        char[] first = s.toCharArray();
        Arrays.sort(first);

        //finding next[] array. This takes quadratic time
//        int[] next = new int[t.length];
//        for(int i = 0; i < t.length; i++){
//            char ch = first[i];
//            int val = 0;
//            for(int j = 0; j < t.length; j++){
//                if(t[j] == ch && !flag[j]){
//                    val = j;
//                    flag[j] = true;
//                    break;
//                }
//            }
//            next[i] = val;
//        }

        //finding next[]
        int[] next = new int[t.length];
        Queue<Integer>[] queues = new Queue[257];
        for (int i = 0; i <= 256; i++) {
            queues[i] = new Queue<>();
        }

        for (int i = 0; i < t.length; i++) {
            queues[t[i]].enqueue(i);
        }
        for (int i = 0; i < t.length; i++) {
            next[i] = queues[first[i]].dequeue();
        }


        //Inverting the message given t[], first and next[] array
        char[] realString = new char[t.length];
        realString[0] = first[index];
        int temp = next[index];
        int i = 1;
        while (i < t.length) {
            realString[i++] = first[temp];
            temp = next[temp];
        }
        BinaryStdOut.write(String.valueOf(realString));
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else inverseTransform();
    }
}