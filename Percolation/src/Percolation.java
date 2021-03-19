import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int gridSize;
    private int gridSquared;
    private WeightedQuickUnionUF uf;
    private int virtualTop;
    private int virtualBottom;
    private int openSites;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be > 0");
        gridSize = n;
        gridSquared = n * n;
        grid = new boolean[gridSize][gridSize];
        uf = new WeightedQuickUnionUF(gridSquared + 2);
        virtualTop = gridSquared;
        virtualBottom = gridSquared + 1;
        openSites = 0;
    }

    public void open(int row, int col) {
        validateSite(row, col);

        int flatIndex = flattenedGrid(row, col);
        if (isOpen(row, col)) {
            return;
        }
        //Open Sites
        grid[row - 1][col - 1] = true;
        openSites++;

        if (row == 1) {
            uf.union(virtualTop, flatIndex);
        }
        if (row == gridSize)
            uf.union(virtualBottom, flatIndex);

        //Check and open left
        if (isOnGrid(row, col - 1) && isOpen(row, col - 1)) {
            uf.union(flatIndex, flattenedGrid(row, col - 1));
        }
        //Check and open right
        if (isOnGrid(row, col + 1) && isOpen(row, col + 1)) {
            uf.union(flatIndex, flattenedGrid(row, col + 1));
        }
        //Check and open top
        if (isOnGrid(row - 1, col) && isOpen(row - 1, col)) {
            uf.union(flatIndex, flattenedGrid(row - 1, col));
        }
        //Check and open bottom
        if (isOnGrid(row + 1, col) && isOpen(row + 1, col)) {
            uf.union(flatIndex, flattenedGrid(row + 1, col));
        }

    }

    public boolean isOpen(int row, int col) {
        validateSite(row, col);
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        validateSite(row, col);
        int p = uf.find(virtualTop);
        int q = uf.find(flattenedGrid(row, col));
        return p == q;
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        int p = uf.find(virtualTop);
        int q = uf.find(virtualBottom);
        return p == q;
    }

    private void validateSite(int row, int col) {
        if (!isOnGrid(row, col)) {
            throw new IndexOutOfBoundsException("Index is out of bound");
        }
    }

    private boolean isOnGrid(int row, int col) {
        int shiftRow = row - 1;
        int shiftCol = col - 1;
        return (shiftCol >= 0 && shiftRow >= 0 && shiftRow < gridSize && shiftCol < gridSize);
    }

    private int flattenedGrid(int row, int col) {
        return (row - 1) * gridSize + col - 1;
    }

    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);

        Percolation percolation = new Percolation(size);
        if (percolation.percolates()) {
            StdOut.println("The System percolates");
        } else {
            StdOut.println("Does not percolates");
        }
    }
}
