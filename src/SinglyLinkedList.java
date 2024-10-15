import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class SinglyLinkedList<E> implements Iterable<E> {

    private static class Node<E>{
        private E element;
        private Node<E> next;
        public Node(E e, Node<E> n){
            element = e;
            next = n;
        }
        public E getElement(){
            return element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> n) {
            next = n;
        }

        public String toString(){
            return (String) element;
        }
    }
    
    // instance variables of the SinglyLinkedList
    private Node<E> head = null;    // head node of the list (or null if empty)
    private Node<E> tail = null;     // last node of the list (or null if empty)
    private int size = 0;            // number of nodes in the list
    public SinglyLinkedList() { }   // constructs an initially empty list
    // access methods

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public E first() {
        if (isEmpty()) return null; return head.getElement();
    }
    public E last() {
        if (isEmpty()) return null;
        return tail.getElement(); }
    // update methods
    public void addFirst(E e) {
        head = new Node<>(e, head); if (size == 0)
            tail = head; size++;
    }
    public void addLast(E e) {
        Node<E> newest = new Node<>(e, null);
        if (isEmpty( )) head = newest;
        else
            tail.setNext(newest); tail = newest;
        size++;
    }
    public E removeFirst( ) {
        if (isEmpty( )) return null;
        E answer = head.getElement( ); head = head.getNext( ); size--;
        if (size == 0)
            tail = null;
        return answer;
    }
    public E removeLast() {
        if (isEmpty()) return null;
        E element = tail.getElement();
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            Node<E> pointer = head;
            while (pointer.getNext() != tail) {
                pointer = pointer.getNext();
            }
            tail = pointer;
            tail.setNext(null);
        }
        size--;
        return element;
    }
    public String toString(){
        String s = "";
        Node<E> n =head;
        while(n!=null){

            s+= n.getElement().toString()+ "\n ";
            n = n.getNext();
        }
        return s;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator();
    }

    private class SinglyLinkedListIterator implements Iterator<E> {
        private Node<E> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E data = current.getElement();
            current = current.getNext();
            return data;
        }
    }
}
