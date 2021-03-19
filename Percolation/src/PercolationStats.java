import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    private int trialCount;
    private double[] trialResult;

    //perform independent trials on an n by n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N and T must be > 0");
        }
        int gridSize = n;
        trialCount = trials;
        trialResult = new double[trialCount];

        for (int trial = 0; trial < trialCount; trial++) {
            Percolation percolation = new Percolation(gridSize);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, gridSize + 1);
                int col = StdRandom.uniform(1, gridSize + 1);
                percolation.open(row, col);
            }
            int openSites = percolation.numberOfOpenSites();
            double result = (double) openSites / (gridSize * gridSize);
            trialResult[trial] = result;
        }
    }

    public double mean() {
        return StdStats.mean(trialResult);
    }

    public double stddev() {
        return StdStats.stddev(trialResult);
    }

    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(trialCount));
    }

    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(trialCount));
    }

    public static void main(String[] args) {
        int gridSize = 10;
        int trialCount = 10;
        if (args.length >= 2) {
            gridSize = Integer.parseInt(args[0]);
            trialCount = Integer.parseInt(args[1]);
        }
        PercolationStats ps = new PercolationStats(gridSize, trialCount);
        String confidence = ps.confidenceLo() + "," + ps.confidenceHi();
        StdOut.println("mean          =" + ps.mean());
        StdOut.print("stddev          =" + ps.stddev());
        StdOut.println("confidence     =" + confidence);
    }
}
