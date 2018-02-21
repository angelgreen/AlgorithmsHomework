import java.util.Iterator;
import java.util.NoSuchElementException;
//Gimport edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

	private class Node {

		private final Item item;
		private Node next;
		private Node prev;

		public Node(Item item, Node next, Node prev) {
			this.item = item;
			this.next = next;
			this.prev = prev;
		}

		public Item getItem() {
			return this.item;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		public void setPrev(Node prev) {
			this.prev = prev;
		}

		public Node getNext() {
			return this.next;
		}	

		public Node getPrev() {
			return this.prev;
		}
	}

	private Node header; //the header 
	private Node tailer; //the tailer 
	private int n;

	public Deque() {
		this.header = this.tailer = null;
		this.n = 0;
	}	

	public boolean isEmpty() {
		return this.header == null;
	}

	public int size() {
		return this.n;
	}

	public void addFirst(Item item) {

		if(item == null) throw new IllegalArgumentException("");

		//add the first 

		Node x = new Node(item, header, null); //the node

		if(isEmpty()) {
			tailer = x;
		}else {
			header.setPrev(x);
		}
		header = x;

		n++;	
	}

	public void addLast(Item item) {

		if(item == null) throw new IllegalArgumentException("");

		Node x = new Node(item, null, tailer);

		if(isEmpty()) {
			header = x;
		}else {
			tailer.setNext(x);	
		}
		tailer = x;

		n++;
	}

	public Item removeFirst() {
		if(isEmpty()) throw new NoSuchElementException("");	

		Node x = header;

		if(header == tailer) {
			header = tailer = null;
		}else {
			header = header.getNext();
			header.setPrev(null);
		}	

		x.setPrev(null);
		x.setNext(null);

		n--;

		return x.getItem();
	}

	public Item removeLast() {
		if(isEmpty()) throw new NoSuchElementException("");	

		Node x = tailer;

		if(header == tailer) {
			header = tailer = null;
		}else {
			tailer = tailer.getPrev();
			tailer.setNext(null);
		}

		x.setNext(null);
		x.setPrev(null);

		n--;

		return x.getItem();

	}

	private class DequeIterator implements Iterator<Item> {

		private Node current;

		public DequeIterator() {
			this.current = header;
		}

		@Override
		public boolean hasNext() {
			return current != null;	
		}	

		@Override
		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			Node x = current;

			current = current.getNext();

			return x.getItem();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove");
		}
	}

	@Override
	public Iterator<Item> iterator() {
		return new DequeIterator();			
	}	

	public static void main(String[] args) {
/**		Deque<String> deque = new Deque<String>();
		deque.addFirst("1");
		deque.addFirst("2");
		deque.addLast("3");
		deque.addLast("4");

		for(String s : deque) {
			StdOut.println(s);			
		}

		String s1 = deque.removeFirst();

		StdOut.println("the removeFirst is " + s1);

		String s2 = deque.removeLast();

		StdOut.println("the removeLast is " + s2);

		StdOut.println("the dequeue empty " + deque.isEmpty());
		**/
	}	
}
