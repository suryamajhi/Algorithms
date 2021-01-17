import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver{
    private class Move implements Comparable<Move>{
        private Move previous;
        private final Board board;
        private int numMoves = 0;

        public Move(Board board){
            this.board = board;
        }
        public Move(Board board,Move previous) {
            this.board = board;
            this.previous = previous;
            this.numMoves = previous.numMoves + 1;
        }

        @Override
        public int compareTo(Move move) {
            return (this.board.manhattan() - move.board.manhattan()) + (this.numMoves - move.numMoves);
        }
    }
    private Move lastMove;
    //find the solution to the initial board (using A* algorithm)
    public Solver(Board initial){
        if(initial == null) throw new IllegalArgumentException();
        MinPQ<Move> moves = new MinPQ<Move>();
        moves.insert(new Move(initial));

        MinPQ<Move> twinMoves = new MinPQ<Move>();
        twinMoves.insert(new Move(initial.twin()));

        while(true){
            lastMove = expands(moves);
            if(lastMove !=null || expands(twinMoves) != null) return;
        }
    }
    private Move expands(MinPQ<Move> moves){
        if(moves.isEmpty()) return null;
        Move bestMove = moves.delMin();
        if(bestMove.board.isGoal()) return bestMove;
        for (Board neighbor: bestMove.board.neighbors()) {
            if(bestMove.previous == null || !neighbor.equals(bestMove.previous.board))
                moves.insert(new Move(neighbor, bestMove));
        }
        return null;
    }

    //is the initial board solvable?
    public boolean isSolvable(){
        return lastMove !=null;
    }

    //min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        return isSolvable()? lastMove.numMoves: -1;
    }

    //sequence of boards in a shortest solution
    public Iterable<Board> solution(){
        if(!isSolvable()) return null;
        Stack<Board> solution = new Stack<Board>();
        while(lastMove!=null){
            solution.push(lastMove.board);
            lastMove = lastMove.previous;
        }
        return solution;
    }
    //unit testing
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}