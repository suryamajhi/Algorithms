import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] maxDistance = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            maxDistance[i] = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) continue;
                maxDistance[i] += wordNet.distance(nouns[i], nouns[j]);
            }
        }
        int max = maxDistance[0];
        int index = 0;
        for (int i = 1; i < maxDistance.length; i++) {
            if (maxDistance[i] > max) {
                max = maxDistance[i];
                index = i;
            }
        }

        return nouns[index];
    }

    //Its Empty
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }

    }  // see test client below

}
