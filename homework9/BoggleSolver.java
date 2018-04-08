import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.TreeSet;
import java.util.HashMap;

public class BoggleSolver {
	
	public BoggleSolver(String[] directory) {
		if(null == directory) throw new IllegalArgumentException("");
		//put the directory in the tries 
		for(int i = 0; i < directory.length; i++) {
			put(directory[i]); //put it ..	
		}	
	}	

	private static final int R = 26; //only 0..25

	private static class Node {
		private boolean val; //default false 	
		private Node[] next = new Node[R];
	}

	private Node root; //the root node

	private void put(String key) {

		root = put(root, key, 0);		
	}

	private boolean keysWithPrefix(String prefix) {
		//first query cache 
		 int d = 0;
		 Node x = root;
		 while(d < prefix.length() && x != null) {
			char c = prefix.charAt(d);
			int i = c - 'A';
			x = x.next[i];	
			d++;		
		 }

		 if( x == null) return false;

		 for(int i = 0; i < R; i++) {
			if( x.next[i] != null) return true;
		 }
		 return false;
	}

	private Node getNode(String key) {
		int d = 0;
		Node x = root;
		while( d < key.length() && x != null) {
			char c = key.charAt(d);
			int i = c - 'A';
			x = x.next[i];
			d++;
		}
		return x;
	}

	private boolean contains(String key) {

		int d = 0;
		Node x = root;
		while( d < key.length() && x != null) {
			char c = key.charAt(d);
			int i = c - 'A';
			x = x.next[i];
			d++;
		}	
	   return x != null && x.val;	
	}

	private Node put(Node x, String key , int d) {

		if(x == null) {
			x = new Node();
		}

		if(d == key.length()) {
			x.val = true; 	
			return x;
		}
		
		char c = key.charAt(d);

		int i = c - 'A';

		x.next[i] = put(x.next[i], key, d + 1);

		return x;
	}

	public Iterable<String> getAllValidWords(BoggleBoard board) {
		if(null == board) throw new IllegalArgumentException("");	
		int rows = board.rows();
		int cols = board.cols();


		TreeSet<String> st = new TreeSet<String>();		

		boolean[] mark = new boolean[rows * cols];	

		int[] pathTo = new int[rows * cols];

		int[][] matrix = new int[rows * cols][8]; //just 8


		for(int i = 0; i < rows * cols; i++) {
			for(int j = 0; j < 8; j++)
				matrix[i][j] = -1;	
			getAdj(board, matrix, i); 
		}

		for(int start = 0; start < rows * cols; start++) {

			StringBuilder builder =new StringBuilder();

			char c = board.getLetter(start / board.cols(), start % board.cols());

			builder.append(c == 'Q' ? "QU": c);	
			//start node
			Node node = getNode(builder.toString());

			pathTo[start] = -1;

			dfs(board, matrix, start, node, mark, pathTo, st);
			mark[start] = false;
		}

		return st;
	}

	private void dfs(BoggleBoard board, int[][] matrix, int source, Node node, boolean[] mark, int[] pathTo,TreeSet<String> st) {

		mark[source] = true;	
		int[] adj = matrix[source];

		for(int i = 0; i < adj.length; i++) {
			int dest = adj[i]; 
			if(dest != -1 && !mark[dest]) {
 
				pathTo[dest] = source; 

				Node x = node;

				int row = dest / board.cols();
				int col = dest % board.cols();

				char c = board.getLetter(row, col);

				String c1 = c == 'Q' ? "QU": String.valueOf(c);

				int d = 0;
				while(d < c1.length() && x != null) {
					char s = c1.charAt(d);	
					x = x.next[s - 'A'];
					d++;
				}


				if(x != null && x.val) {
					StringBuilder builder = new StringBuilder();
					for(int k = dest; k != -1; k = pathTo[k]) {
						int row1 = k / board.cols();
						int col1 = k % board.cols();
						char c0 = board.getLetter(row1, col1);
						builder.append(c0 == 'Q' ? "UQ": c0);
					}
					builder.reverse();
					String word = builder.toString();
					if(word.length() >= 3) {
						st.add(word);
					}
				}

				boolean keysWithPrefix = false;

				if(x != null) {
					for(int k = 0;  k < R; k++) {
						if(x.next[k] != null) {
							keysWithPrefix = true;
							break;
						}
					}
				}

				if(keysWithPrefix) {
					dfs(board, matrix, dest, x, mark, pathTo, st);
					mark[dest] = false;
				}
			}
		}
	}

	private void getAdj(BoggleBoard board, int[][] matrix, int source) {

		int row = source / board.cols();
		int col = source % board.cols();

		int index = 0;
		//leftTop
		int leftTopRow = row - 1;
		int leftTopCol = col - 1;
		//leftTop
		if(leftTopRow >= 0 && leftTopCol >= 0) {
			int v1 = leftTopRow * board.cols() + leftTopCol;
			matrix[source][index++] = v1;
		}

		//top
		int TopRow = row - 1;
		int TopCol = col;
		if(TopRow >= 0 && TopCol >= 0) {
			int v2 = TopRow * board.cols() + TopCol;
			matrix[source][index++] = v2;
		}
		//rightTop
		int rightTopRow = row - 1;
		int rightTopCol = col + 1;
		if(rightTopRow >= 0 && rightTopCol < board.cols()) {
			int v3 = rightTopRow * board.cols() + rightTopCol;	
			matrix[source][index++] = v3;
		}
		//left
		int leftRow = row;
		int leftCol = col - 1;
		if(leftCol >= 0) {
			int v4 = leftRow * board.cols() + leftCol;
			matrix[source][index++] = v4;
		}
		//right 
		int rightRow = row;
		int rightCol = col + 1;
		if(rightCol < board.cols()) {
			int v5 = rightRow * board.cols() + rightCol;	
			matrix[source][index++] = v5;
		}
		//leftBottom
		int leftBottomRow = row + 1;
		int leftBottomCol = col - 1;
		if(leftBottomCol >= 0 && leftBottomRow < board.rows()) {
			int v6 = leftBottomRow * board.cols() + leftBottomCol;	
			matrix[source][index++] = v6;
		}
		//bottom
		int bottomRow = row + 1;
		int bottomCol = col;
		if(bottomRow < board.rows()) {
			int v7 = bottomRow * board.cols() + bottomCol;
			matrix[source][index++] = v7;
		}
		//rightBottom
		int rightBottomRow = row + 1;
		int rightBottomCol = col + 1;
		if(rightBottomRow < board.rows() && rightBottomCol < board.cols()) {
			int v8 = rightBottomRow * board.cols() + rightBottomCol;	
			matrix[source][index++] = v8;
		}
	}

	public int scoreOf(String word) {
		if(null == word) throw new IllegalArgumentException("");

		if(!contains(word)) return 0; //the directory no contains it 
		int len = word.length();
		if(len >= 0 && len <= 2) return 0;
		else if(len > 2 && len <= 4) return 1;
		else if(len == 5) return 2;
		else if(len == 6) return 3;
		else if(len == 7) return 5;
		else return 11;	
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
    	String[] dictionary = in.readAllStrings();
	    BoggleSolver solver = new BoggleSolver(dictionary);
	    BoggleBoard board = new BoggleBoard(args[1]);
//		BoggleBoard board = new BoggleBoard(4, 4);
	    int score = 0;
		int count = 0;
		Stopwatch timer = new Stopwatch();
	    for (String word : solver.getAllValidWords(board)) {
	        StdOut.println(word);
	        score += solver.scoreOf(word);
			count++;
	    }
	   StdOut.println("Score = " + score);
	   StdOut.println("Count = " + count);
	   StdOut.printf("%.2f seconds\n", timer.elapsedTime()); 
	}
}
