import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;


public class KdTree {
    private Node root;
    private int size;

    //helper Node class
    private static class Node {
        private Point2D p;
        private Node lb;
        private Node rt;
        private RectHV rect;

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }


    //construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    //is the set empty
    public boolean isEmpty() {
        return size() == 0;
    }

    //number of points in the set
    public int size() {
        return size;
    }

    //add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            root = insert(root, p, true, new RectHV(0, 0, 1, 1));
            size++;
        }
    }

    private Node insert(Node x, Point2D p, boolean axis, RectHV rect) {
        int cmp;
        if (x == null) {
            return new Node(p, rect);
        }
        if (axis) {
            cmp = Point2D.X_ORDER.compare(p, x.p);
            if (cmp < 0) x.lb = insert(x.lb, p, false, new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax()));
            else x.rt = insert(x.rt, p, false, new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        } else {
            cmp = Point2D.Y_ORDER.compare(p, x.p);
            if (cmp < 0) x.lb = insert(x.lb, p, true, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y()));
            else x.rt = insert(x.rt, p, true, new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax()));
        }
        return x;
    }

    //does the set contain point p
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(p) != null;
    }

    private Point2D get(Point2D p) {
        return get(root, p, true);
    }

    private Point2D get(Node x, Point2D p, boolean axis) {
        int cmp;
        if (x == null) return null;
        if (axis) {
            cmp = Point2D.X_ORDER.compare(p, x.p);
            if (cmp < 0) return get(x.lb, p, false);
            else if (cmp > 0) return get(x.rt, p, false);
            else {
                if (x.p.equals(p)) return p;
                else return get(x.rt, p, false);
            }
        } else {
            cmp = Point2D.Y_ORDER.compare(p, x.p);
            if (cmp < 0) return get(x.lb, p, true);
            else if (cmp > 0) return get(x.rt, p, true);
            else {
                if (x.p.equals(p)) return p;
                else return get(x.rt, p, true);
            }
        }
    }

    //draw all points to standard draw
    public void draw() {
        traverseForDraw(root, true);

    }

    private void traverseForDraw(Node x, boolean axis) {
        if (x == null) return;
        if (axis) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            traverseForDraw(x.lb, false);
            traverseForDraw(x.rt, false);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
            traverseForDraw(x.lb, true);
            traverseForDraw(x.rt, true);
        }
    }

    //all the points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        LinkedQueue<Point2D> queue = new LinkedQueue<Point2D>();

        traverse(root, queue, rect);
        return queue;
    }

    private void traverse(Node x, LinkedQueue<Point2D> queue, RectHV rect) {
        if (x == null) return;
        if (rect.intersects(x.rect)) {
            if (rect.contains(x.p))
                queue.enqueue(x.p);
            traverse(x.lb, queue, rect);
            traverse(x.rt, queue, rect);
        }
    }


    // a nearest neighbour in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        Point2D point = root.p;
        point = traverseForNearest(root, p, point, true);
        return point;
    }

    private Point2D traverseForNearest(Node x, Point2D p, Point2D point, boolean axis) {
        int cmp;
        double distance, newDistance;
        if (x == null) return point;
        newDistance = p.distanceSquaredTo(x.p);
        distance = p.distanceSquaredTo(point);

        if (newDistance < distance) {
            point = x.p;
        }
        if (axis) {
            cmp = Point2D.X_ORDER.compare(p, x.p);
            if (cmp < 0) {
                point = traverseForNearest(x.lb, p, point, false);
                if (x.rect.distanceSquaredTo(p) < distance)
                    point = traverseForNearest(x.rt, p, point, false);
            } else {
                point = traverseForNearest(x.rt, p, point, false);
                if (x.rect.distanceSquaredTo(p) < distance)
                    point = traverseForNearest(x.lb, p, point, false);
            }
        } else {
            cmp = Point2D.Y_ORDER.compare(p, x.p);
            if (cmp < 0) {
                point = traverseForNearest(x.lb, p, point, true);
                if (x.rect.distanceSquaredTo(p) < distance)
                    point = traverseForNearest(x.rt, p, point, true);
            } else {
                point = traverseForNearest(x.rt, p, point, true);
                if (x.rect.distanceSquaredTo(p) < distance)
                    point = traverseForNearest(x.lb, p, point, true);
            }
        }
        return point;
    }

    //unit testing of the methods
    public static void main(String[] args) {

    }


}