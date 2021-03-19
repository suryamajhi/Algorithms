

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;
import java.util.List;

public class SAP {

    private final Digraph G;
    private BreadthFirstDirectedPaths path1, path2;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        if (v == w) return 0;
        Integer commonVertex = getCommonVertex(v, w);
        if (commonVertex == null) return -1;
        return path1.distTo(commonVertex) + path2.distTo(commonVertex);


    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        if (v == w) return v;
        Integer commonVertex = getCommonVertex(v, w);
        if (commonVertex == null) return -1;
        return getCommonVertex(v, w);
    }

    private Integer getCommonVertex(int v, int w) {
        path1 = new BreadthFirstDirectedPaths(G, v);
        path2 = new BreadthFirstDirectedPaths(G, w);
        List<Integer> set = new ArrayList<>();
        List<Integer> common = new ArrayList<>();
        for (int i = 0; i < G.V(); i++) if (path1.hasPathTo(i)) set.add(i);
        for (int vertex : set)
            if (path2.hasPathTo(vertex))
                common.add(vertex);

        Integer minVertex = null;
        if (common.isEmpty()) return minVertex;
        int minDistance = path1.distTo(common.get(0)) + path2.distTo(common.get(0));
        minVertex = common.get(0);
        for (int i = 1; i < common.size(); i++) {
            if (minDistance > (path1.distTo(common.get(i)) + path2.distTo(common.get(i)))) {
                minDistance = (path1.distTo(common.get(i)) + path2.distTo(common.get(i)));
                minVertex = common.get(i);
            }
        }
        return minVertex;
    }

    //validate vertex
    private void validateVertex(Integer v) {
        if (v == null) throw new IllegalArgumentException();
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (G.V() - 1));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        checkNullValues(v);
        checkNullValues(w);
        for (Integer value : v) validateVertex(value);
        for (Integer value : w) validateVertex(value);
        Integer commonVertex = getCommonVertex(v, w);
        if (commonVertex == null) return -1;
        else return path1.distTo(commonVertex) + path2.distTo(commonVertex);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        checkNullValues(v);
        checkNullValues(w);
        for (Integer value : v) validateVertex(value);
        for (Integer value : w) validateVertex(value);
        Integer commonVertex = getCommonVertex(v, w);
        if (commonVertex == null) return -1;
        else return commonVertex;
    }

    private void checkNullValues(Iterable<Integer> v) {
        for (Integer value :
                v) {
            if (value == null) throw new IllegalArgumentException();
        }
    }


    //commonProcess when the inputs are iterables
    private Integer getCommonVertex(Iterable<Integer> v, Iterable<Integer> w) {
        path1 = new BreadthFirstDirectedPaths(G, v);
        path2 = new BreadthFirstDirectedPaths(G, w);
        List<Integer> set = new ArrayList<>();
        List<Integer> common = new ArrayList<>();
        for (int i = 0; i < G.V(); i++) if (path1.hasPathTo(i)) set.add(i);
        for (int vertex : set) if (path2.hasPathTo(vertex)) common.add(vertex);
        Integer minVertex = null;
        if (common.isEmpty()) return minVertex;
        int minDistance = path1.distTo(common.get(0)) + path2.distTo(common.get(0));
        minVertex = common.get(0);
        for (int i = 1; i < common.size(); i++) {
            if (minDistance > (path1.distTo(common.get(i)) + path2.distTo(common.get(i)))) {
                minDistance = (path1.distTo(common.get(i)) + path2.distTo(common.get(i)));
                minVertex = common.get(i);
            }
        }
        return minVertex;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // empty


    }
}
