import edu.princeton.cs.algs4.StdRandom;
//import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class RandomizedQueue<Item> implements Iterable<Item> {

	private Item[] a;
	private int n;

	public RandomizedQueue() {
		this.a = (Item[])new Object[2];
		this.n = 0;
	}	

	public boolean isEmpty() {
		return this.n == 0;	
	}

	public int size() {
		return this.n;
	}

	private void resize(int capacity) {
		assert capacity > 0;

		Item[] tmp = (Item[])new Object[capacity];

		for(int i = 0; i < n; i++) {
			tmp[i] = a[i];
		}

		a = tmp;
		
	}

	public void enqueue(Item item) {

		if(item == null) throw new IllegalArgumentException("");

		if(n == a.length) {
			resize(2*a.length);
		}	

		a[n++] = item;
	}

	public Item dequeue() {

		if(isEmpty()) throw new NoSuchElementException("");	

		int index = StdRandom.uniform(0, n);

		Item x = a[index];

		for(int i = index; i < n - 1; i++)
			a[i] = a[i + 1];

		n--;

		if (n > 0 && n == a.length/4) resize(a.length/2);

		return x;
	}

	public Item sample() {
		if(isEmpty()) throw new NoSuchElementException("");	

		int index = StdRandom.uniform(0,n);
		return a[index];
	}

	private class RandomizedQueueItr implements Iterator<Item> {
		
		private Item[] c;

		private int current;

		public RandomizedQueueItr() {
			
			if(n > 0) {	
				c = (Item[])new Object[n];
			}

			for(int i = 0; i < n; i++) {
				c[i] = a[i];
			}	

			this.current = c == null? 0: c.length;
		}

		@Override
		public boolean hasNext() {
			if(null == c)  return false;

			return current  > 0;
		}
		@Override
		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();

			int index = StdRandom.uniform(0, current);

			Item x = c[index];

			for(int i = index; i < current - 1; i++) 
				c[i] = c[i+1];

			current--;

			return x;
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove");
		}
	}

	public Iterator<Item> iterator() {
		return new RandomizedQueueItr();	
	}	

	public static void main(String[] args) {
/**		RandomizedQueue<String> queue = new RandomizedQueue<String>();
		queue.enqueue("1");
		queue.enqueue("2");

		String s1 = queue.sample();
		StdOut.println("sample is " + s1);
		String s = queue.dequeue();
		StdOut.println("enqueue is " + s);

		for(String s3: queue) {
			StdOut.println(s3);
		}
		**/
	}
}

