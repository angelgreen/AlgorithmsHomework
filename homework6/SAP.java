import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

	//the reverse Dgraph 	
	private final Digraph G;
	private final int n;

	public SAP(Digraph G) {
		if(null == G) throw new IllegalArgumentException("");
		this.G = new Digraph(G); //immutable it 
		this.n = G.V();
	}	

	public int length(int v, int w) {

		if( v < 0 || v > n - 1) throw new IllegalArgumentException("");
		if( w < 0 || w > n - 1) throw new IllegalArgumentException("");	

		BreadthFirstDirectedPaths path1 = new BreadthFirstDirectedPaths(this.G, v);
		BreadthFirstDirectedPaths path2 = new BreadthFirstDirectedPaths(this.G, w);

		int bestLength = -1;

		for(int d = 0; d < n; d++) {
				//v can go to d and w and go to d
			if(path1.hasPathTo(d) && path2.hasPathTo(d)) {
				int length = path1.distTo(d) + path2.distTo(d);
				bestLength = bestLength == -1? length : length < bestLength ? length: bestLength;
			}
		}

		return bestLength;
	}

	public int ancestor(int v, int w) {

		if( v < 0 || v > n - 1) throw new IllegalArgumentException("");
		if( w < 0 || w > n - 1) throw new IllegalArgumentException("");	
		int ancestor = -1;
		int bestLength = Integer.MAX_VALUE;

		BreadthFirstDirectedPaths path1 = new BreadthFirstDirectedPaths(this.G, v);
		BreadthFirstDirectedPaths path2 = new BreadthFirstDirectedPaths(this.G, w);

		for(int d = 0; d < n; d++) {
			if(path1.hasPathTo(d) && path2.hasPathTo(d)) {
				int length = path1.distTo(d) + path2.distTo(d);
				if(length < bestLength) {
					bestLength = length;
					ancestor = d;
				}
			}	
		}
		return ancestor;
	}	

	public int length(Iterable<Integer> v, Iterable<Integer> w) {

		BreadthFirstDirectedPaths path1 = new BreadthFirstDirectedPaths(this.G, v);
		BreadthFirstDirectedPaths path2 = new BreadthFirstDirectedPaths(this.G, w);

		int bestLength = -1;

		for(int d = 0; d < n; d++) {
				//v can go to d and w and go to d
			if(path1.hasPathTo(d) && path2.hasPathTo(d)) {
				int length = path1.distTo(d) + path2.distTo(d);
				bestLength = bestLength == -1? length : length < bestLength ? length: bestLength;
			}
		}
		return bestLength;
	}

	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

		int ancestor = -1;
		int bestLength = Integer.MAX_VALUE;

		BreadthFirstDirectedPaths path1 = new BreadthFirstDirectedPaths(this.G, v);
		BreadthFirstDirectedPaths path2 = new BreadthFirstDirectedPaths(this.G, w);

		for(int d = 0; d < n; d++) {
			if(path1.hasPathTo(d) && path2.hasPathTo(d)) {
				int length = path1.distTo(d) + path2.distTo(d);
				if(length < bestLength) {
					bestLength = length;
					ancestor = d;
				}
			}	
		}
		return ancestor;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while(!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}	
	}
}
