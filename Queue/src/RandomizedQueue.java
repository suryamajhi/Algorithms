import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a; //array of items
    private int n; //number of elements of the array

    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = a[i];
        }
        a = copy;
    }

    private void swap(int index, int last) {
        Item item = a[index];
        a[index] = a[last];
        a[last] = item;
    }

    //insert an element on to the stack(queue)

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }

    //remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("No elements in the queue");
        int index = StdRandom.uniform(n);
        swap(index, n - 1);
        Item item = a[n - 1];
        a[n - 1] = null;
        n--;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return item;
    }

    //returns a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int index = StdRandom.uniform(n);
        return a[index];
    }

    //Iterator implementation
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;

        public RandomizedQueueIterator() {
            i = n - 1;
        }

        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int index = StdRandom.uniform(i + 1);
            swap(index, i);
            Item item = a[i];
            i--;
            return item;
        }
    }

    //unit testing main method
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (!s.equals("-")) queue.enqueue(s);
            else StdOut.print(queue.dequeue());
        }

    }
}