import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
	
	public static void main(String[] args) {
		if(args.length < 1) throw new java.lang.IllegalArgumentException("");

		int num = Integer.parseInt(args[0]);

		RandomizedQueue<String> queue = new RandomizedQueue<String>();


		while(!StdIn.isEmpty()) {
			queue.enqueue(StdIn.readString());
		}

		for(int i = 0; i < num; i++) {
			StdOut.println(queue.dequeue());
		}
	}	
}
