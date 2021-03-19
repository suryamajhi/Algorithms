import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.LinkedList;

public class Board {
    private static final int SPACE = 0;
    private final int[][] tiles;
    private final int boardSize;

    //create a board from n-by-n array of tiles,
    //where tiles[row][col] = tile at [row][col]
    public Board(int[][] tiles) {
        boardSize = tiles.length;
        this.tiles = copy(tiles);
    }

    private int[][] copy(int[][] tiles) {
        int[][] copy = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            System.arraycopy(tiles[i], 0, copy[i], 0, tiles.length);
        }
        return copy;
    }

    //string representation of the board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(boardSize).append("\n");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                s.append(String.format(" %2d", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    //board dimension n
    public int dimension() {
        return boardSize;
    }

    //number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (tileIsNotInPlace(row, col)) count++;
            }
        }
        return count;
    }

    private boolean tileIsNotInPlace(int row, int col) {
        int tile = tiles[row][col];

        return !isSpace(tile) && tile != goalFor(row, col);
    }

    private boolean isSpace(int tile) {
        return tile == SPACE;
    }

    private int goalFor(int row, int col) {
        return row * dimension() + col + 1;
    }

    //sum of Manhattan distance between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                sum += calculateDistance(row, col);
            }
        }
        return sum;
    }

    private int calculateDistance(int row, int col) {
        int tile = tile(row, col);

        return isSpace(tile) ? 0 : Math.abs(row - row(tile)) + Math.abs(col - col(tile));
    }

    private int tile(int row, int col) {
        return tiles[row][col];
    }

    private int row(int tile) {
        return (tile - 1) / dimension();
    }

    private int col(int tile) {
        return (tile - 1) % dimension();
    }


    //is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    //does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (!(y instanceof Board) || ((Board) y).tiles.length != this.tiles.length) return false;
        return Arrays.deepEquals(this.tiles, ((Board) y).tiles);
    }

    //all neighbouring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<Board>();

        int[] location = spaceLocation();
        int spaceRow = location[0];
        int spaceCol = location[1];
        if (spaceCol > 0) neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol - 1)));
        if (spaceCol < dimension() - 1) neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol + 1)));
        if (spaceRow > 0) neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow - 1, spaceCol)));
        if (spaceRow < dimension() - 1) neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow + 1, spaceCol)));

        return neighbors;
    }

    private int[] spaceLocation() {
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                if (isSpace(tile(i, j))) {
                    int[] array = new int[2];
                    array[0] = i;
                    array[1] = j;
                    return array;
                }
        throw new RuntimeException();
    }

    private int[][] swap(int row1, int col1, int row2, int col2) {
        int[][] copy = copy(tiles);
        int temp = copy[row1][col1];
        copy[row1][col1] = copy[row2][col2];
        copy[row2][col2] = temp;
        return copy;
    }

    //a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int row1, row2, col1, col2;
        while (true) {
            row1 = StdRandom.uniform(0, dimension());
            row2 = StdRandom.uniform(0, dimension());
            col1 = StdRandom.uniform(0, dimension());
            col2 = StdRandom.uniform(0, dimension());
            if (!isSpace(tile(row1, col1)) && !isSpace(tile(row2, col2)) && tile(row1, col1) != tile(row2, col2))
                return new Board(swap(row1, col1, row2, col2));
        }
    }

    //unit testing
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        System.out.println(initial.twin());
    }
}