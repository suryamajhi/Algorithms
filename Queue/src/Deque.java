import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first,last;
    private int n;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    public Deque(){
        first = null;
        last = null;
        n = 0;
    }

    //is the deque empty
    public boolean isEmpty(){
        return first == null;
    }
    //return the number of items on the deque
    public int size(){
        return n;
    }
    //add the item to the front
    public void addFirst(Item item){
        if (item == null) throw new IllegalArgumentException("Pass the argument");
        if(isEmpty()){
            first = new Node<>();
            first.item = item;
            first.next = null;
            last = first;
        }else {
            Node<Item> oldFirst = first;
            first = new Node<>();
            first.item = item;
            first.next = oldFirst;
        }
        n++;
    }

    public void addLast(Item item){
        if (item == null) throw new IllegalArgumentException("Pass the argument");
        if(isEmpty()){
            addFirst(item);
        }else {
            Node<Item> oldLast = last;
            last = new Node<>();
            last.item = item;
            last.next = null;
            oldLast.next = last;
            n++;
        }
    }
    //remove and return the item from the front
    public Item removeFirst(){
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        if(first.next == null){
            first = null;
            last = null;
        }else {
            first = first.next;
        }
        n--;
        return item;
    }
    //remove and return the item from the back
    public Item removeLast(){
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        if(first.next == null){
            return removeFirst();
        }else if (first.next.next == null){
            Item item = last.item;
            first.next = null;
            last = first;
            n--;
            return item;
        }else {
            Node<Item> current = first;
            while (current.next.next != null) {
                current = current.next;
            }
            Item item = current.next.item;
            current.next = null;
            last = current;
            n--;
            return item;
        }
    }

    //return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new LinkedIterator();
    }
    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> current = first;
        public boolean hasNext(){return current !=null;}
        public void remove() { throw new UnsupportedOperationException("Not supported");}
        public Item next(){
            if(!hasNext()) throw new NoSuchElementException("No such element");
            Item item =current.item;
            current = current.next;
            return item;
        }
    }

    //unit testing
    public static void main(String[] args) {
        Deque<String> deque =new Deque<>();
        while(!StdIn.isEmpty()){
            String s = StdIn.readString();
            if(!s.equals("-")) deque.addFirst(s);
            else StdOut.print(deque.removeFirst());
        }
    }
}
