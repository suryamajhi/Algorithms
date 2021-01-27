import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final int numberOfTeams;
    private final List<String> teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remainingGames;
    private final HashMap<Integer, List<Integer>> map;
    private FordFulkerson fulkerson;
    private int networkSize;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename){
        In in = new In(filename);
        this.numberOfTeams = in.readInt();
        this.teams = new ArrayList<>();
        this.wins = new int[numberOfTeams];
        this.losses = new int[numberOfTeams];
        this.remainingGames = new int[numberOfTeams];
        map = new HashMap<>();
        for(int i = 0; i < numberOfTeams; i++){
            teams.add(in.readString());
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remainingGames[i] = in.readInt();
            List<Integer> toPlay = new ArrayList<>();
            for(int j = 0; j < numberOfTeams; j++){
                toPlay.add(in.readInt());
            }
            map.put(i, toPlay);
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return this.teams;
    }

    // number of wins for given team
    public int wins(String team) {
        int win = teams.indexOf(team);
        if(win == -1)
            throw new IllegalArgumentException();
        return wins[win];
    }

    // number of losses for given team
    public int losses(String team){
        int loss = teams.indexOf(team);
        if(loss == -1)
            throw new IllegalArgumentException();
        return losses[loss];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        int remainingGame = teams.indexOf(team);
        if(remainingGame == -1)
            throw new IllegalArgumentException();
        return remainingGames[remainingGame];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2){
        int indexOfTeam1 = teams.indexOf(team1);
        int indexOfTeam2 = teams.indexOf(team2);
        if(indexOfTeam1 == -1 || indexOfTeam2 == -1) throw new IllegalArgumentException();
        List<Integer> list = map.get(indexOfTeam1);
        return list.get(indexOfTeam2);
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if(numberOfTeams == 1) return false;
        int lessOne = numberOfTeams - 1;
        int indexOfTeam = teams.indexOf(team);
        if(indexOfTeam == -1) throw new IllegalArgumentException();
        for (int i = 0; i < numberOfTeams; i++) {
            if(i == indexOfTeam) continue;
            if(wins[indexOfTeam] + remainingGames[indexOfTeam] < wins[i])
                return true;
        }
        int networkNode = (numberOfTeams - 2) * (numberOfTeams - 1) + numberOfTeams;
        FlowNetwork network = new FlowNetwork(networkNode);
        networkSize = network.V();
        for(int i = 0; i < numberOfTeams; i++){
            if(i == indexOfTeam) continue;
            for(int j = i + 1; j < numberOfTeams; j++){
                if(j == indexOfTeam) continue;
                int node = i * lessOne + j;
                FlowEdge e = new FlowEdge(0, node, against(teams.get(i), teams.get(j)));
                FlowEdge e1 =new FlowEdge(node, network.V() - 1- (numberOfTeams -i), Double.POSITIVE_INFINITY);
                FlowEdge e2 = new FlowEdge(node, network.V() - 1 - (numberOfTeams - j), Double.POSITIVE_INFINITY);
                network.addEdge(e);
                network.addEdge(e1);
                network.addEdge(e2);
            }
        }
        for(int i = 0; i < numberOfTeams; i++){
            if(i == indexOfTeam) continue;
            int node = network.V() - 1 - numberOfTeams + i;
            FlowEdge e = new FlowEdge(node, network.V() -1, wins[indexOfTeam] + remainingGames[indexOfTeam] - wins[i]);
            network.addEdge(e);
        }

        this.fulkerson = new FordFulkerson(network, 0, network.V() - 1);
        for (FlowEdge e :
                network.adj(0)) {
            if(e.capacity() != e.flow())
                return true;
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if(!isEliminated(team)) return null;
        int indexOfTeam = teams.indexOf(team);
        if(indexOfTeam == -1 ) throw new IllegalArgumentException();

        List<String> teams = new ArrayList<>();

        for (int i = 0; i < numberOfTeams; i++) {
            if(i == indexOfTeam) continue;
            if(wins[indexOfTeam] + remainingGames[indexOfTeam] < wins[i])
                teams.add(this.teams.get(i));
        }
        if(teams.size() > 0) return teams;

        for(int i = networkSize - numberOfTeams - 1; i < networkSize; i++){
            int x = i - (networkSize - numberOfTeams - 1);
            if(x == indexOfTeam) continue;
            if(fulkerson.inCut(i)) {
                teams.add(this.teams.get(x));
            }
        }
        return teams;
    }


    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
