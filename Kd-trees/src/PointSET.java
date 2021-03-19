import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    //construct an empty set of points
    private TreeSet<Point2D> set, points;

    public PointSET() {
        set = new TreeSet<>();
    }

    //is the set empty
    public boolean isEmpty() {
        return set.size() == 0;
    }

    //number of points in the set
    public int size() {
        return set.size();
    }

    //add the point to the set(if it is not already there)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        set.add(p);
    }

    //does the set contain point p
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    //draw all points to standard.draw
    public void draw() {
        for (Point2D p :
                set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        points = new TreeSet<>();
        for (Point2D p :
                set) {
            if (rect.contains(p))
                points.add(p);
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (set.isEmpty()) return null;
        Point2D minPoint;
        double min, d;
        minPoint = set.first();
        min = minPoint.distanceSquaredTo(p);

        for (Point2D point :
                set) {
            d = p.distanceSquaredTo(point);
            if (d < min) {
                minPoint = point;
                min = d;
            }
        }
        return minPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}



