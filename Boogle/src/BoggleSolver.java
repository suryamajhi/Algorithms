import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class BoggleSolver {
    private final CustomTST<Integer> trieSET;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        trieSET = new CustomTST<>();

        for(String string: dictionary){
            if(string.length() < 3) continue;
            trieSET.put(string, scoreOf(string));
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        HashSet<String> set = new HashSet<>();
        int m = board.rows();
        int n = board.cols();
        for(int i = 0; i < m ; i++){
            for(int j = 0; j < n; j++){
                char letter = board.getLetter(i,j);
                List<Character> string = new ArrayList<>();
                string.add(letter);
                if(letter == 'Q') string.add('U');
                boolean[][] visited = new boolean[m][n];
                if(!trieSET.keysWithPrefi(String.valueOf(letter))) continue;
                visited[i][j] = true;
                recurseAndBackTrack(string,set, board, i, j, visited);
                visited[i][j] = false;
            }
        }
        return set;
    }
    //recurse and backtrack
    private void recurseAndBackTrack(List<Character> charArray, HashSet<String> set,BoggleBoard board, int i, int j, boolean[][] visited){
        String string = charArray.stream().map(Object::toString).reduce((acc, e) -> acc  + e).get();
        if(!trieSET.keysWithPrefi(string)) return;
        if(trieSET.contains(string) && string.length() >= 3){
            set.add(string);
        }

        if(isValid(i + 1, j, board) && !visited[i+1][j]){
            char letter = board.getLetter(i+1, j);
            charArray.add(letter);
            visited[i+1][j] = true;
            if(letter == 'Q'){
                charArray.add('U');
                recurseAndBackTrack(charArray, set, board, i + 1, j, visited);
                charArray.remove(charArray.size()-1);
            }else {
                recurseAndBackTrack(charArray, set, board, i + 1, j, visited);
            }
            charArray.remove(charArray.size() - 1);
            visited[i+1][j] = false;
        }
        if(isValid(i - 1, j, board) && !visited[i-1][j]){
            char letter = board.getLetter(i-1, j);
            charArray.add(letter);
            visited[i-1][j] = true;
            if(letter == 'Q'){
                charArray.add('U');
                recurseAndBackTrack(charArray, set, board, i - 1, j, visited);
                charArray.remove(charArray.size()-1);
            }else
                recurseAndBackTrack(charArray, set, board, i - 1, j, visited);
            charArray.remove(charArray.size() - 1);
            visited[i-1][j] = false;
        }
        if(isValid(i , j-1, board) && !visited[i][j-1]){
            char letter = board.getLetter(i, j-1);
            charArray.add(letter);
            visited[i][j-1] = true;
            if(letter == 'Q'){
                charArray.add('U');
                recurseAndBackTrack(charArray, set, board, i , j - 1, visited);
                charArray.remove(charArray.size()-1);
            }else recurseAndBackTrack(charArray, set, board, i , j - 1, visited);
            charArray.remove(charArray.size() - 1);
            visited[i][j-1] = false;
        }
        if(isValid(i , j+1, board) && !visited[i][j+1]){
            char letter = board.getLetter(i, j+1);
            charArray.add(letter);
            visited[i][j+1] = true;
            if(letter == 'Q'){
                charArray.add('U');
                recurseAndBackTrack(charArray, set, board, i, j+1, visited);
                charArray.remove(charArray.size()-1);
            }else recurseAndBackTrack(charArray, set, board, i, j+1, visited);
            charArray.remove(charArray.size() - 1);
            visited[i][j+1] = false;
        }
        if(isValid(i + 1, j-1, board) && !visited[i+1][j-1]){
            char letter = board.getLetter(i+1,j-1);
            charArray.add(letter);
            visited[i+1][j-1] = true;
            if(letter == 'Q'){
                charArray.add('U');
                recurseAndBackTrack(charArray, set, board, i + 1, j-1, visited);
                charArray.remove(charArray.size()-1);
            }else recurseAndBackTrack(charArray, set, board, i + 1, j-1, visited);
            charArray.remove(charArray.size() - 1);
            visited[i+1][j-1] = false;
        }
        if(isValid(i + 1, j+1, board) && !visited[i+1][j+1]){
            char letter = board.getLetter(i+1,j+1);
            charArray.add(letter);
            visited[i+1][j+1] = true;
            if(letter == 'Q'){
                charArray.add('U');
                recurseAndBackTrack(charArray, set, board, i + 1, j+1, visited);
                charArray.remove(charArray.size()-1);
            }else  recurseAndBackTrack(charArray, set, board, i + 1, j+1, visited);
            charArray.remove(charArray.size() - 1);
            visited[i+1][j+1] = false;
        }
        if(isValid(i - 1, j+1, board) && !visited[i-1][j+1]){
            char letter = board.getLetter(i-1,j+1);
            charArray.add(letter);
            visited[i-1][j+1] = true;
            if(letter == 'Q'){
                charArray.add('U');
                recurseAndBackTrack(charArray, set, board, i - 1, j+1, visited);
                charArray.remove(charArray.size()-1);
            }else recurseAndBackTrack(charArray, set, board, i - 1, j+1, visited);
            charArray.remove(charArray.size() - 1);
            visited[i-1][j+1] = false;
        }if(isValid(i - 1, j-1, board) && !visited[i-1][j-1]){
            char letter = board.getLetter(i-1,j-1);
            charArray.add(letter);
            visited[i-1][j-1] = true;
            if(letter == 'Q'){
                charArray.add('U');
                recurseAndBackTrack(charArray, set, board, i - 1, j-1, visited);
                charArray.remove(charArray.size()-1);
            }else recurseAndBackTrack(charArray, set, board, i - 1, j-1, visited);
            charArray.remove(charArray.size() - 1);
            visited[i-1][j-1] = false;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if(word.length() < 3) return 0;
        if(trieSET.contains(word))
            return score(word);
        else return 0;
    }

    //helpers
    private int score(String word){
        if(word.length() == 3 || word.length() == 4) return 1;
        else if( word.length() == 5) return 2;
        else if( word.length() == 6) return 3;
        else if(word.length() == 7) return 5;
        else return 11;
    }

    private boolean isValid(int x, int y, BoggleBoard board){
        return x >= 0 && x < board.rows() && y >= 0 && y < board.cols();
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
