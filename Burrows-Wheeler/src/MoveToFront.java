import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] moveToFront = new char[256];
        for (int i = 0; i < 256; i++)
            moveToFront[i] = (char) i;
        String s = BinaryStdIn.readString();
        for (int k = 0; k < s.length(); k++) {
            char ch = s.charAt(k);
            int index = 0;
            for (int i = 0; i < 256; i++) {
                if (moveToFront[i] == ch) {
                    BinaryStdOut.write((char) i);
                    index = i;
                    break;
                }
            }
            char[] newArray = new char[256];
            newArray[0] = ch;
            int i = 1;
            while (i < 256) {
                if (i <= index) newArray[i] = moveToFront[i - 1];
                else newArray[i] = moveToFront[i];
                i++;
            }
            moveToFront = newArray;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    //need to be done.

    public static void decode() {
        // Didnot understood how to do it.

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else decode();
    }

}