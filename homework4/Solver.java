import java.util.LinkedList;
import java.util.Stack;
import java.util.Comparator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {

	// private final MinPQ<Node> minPQ; 

	private boolean isSolvable = false;
	
	private Node solutionNode; //the solutionNode 

	private LinkedList<Board> list; //the board list 

	private static final boolean DEBUG = false;
	//the node
	private class Node implements Comparable<Node> {

		private Node  prev;
		private final Board board;
		private final int moves;
		private final boolean isTwin;
		private final int priority;

		public Node(Board board, int moves, boolean isTwin) {
			this.board = board;
			this.moves = moves;
			this.priority = board.manhattan() + moves;
			this.isTwin = isTwin;
		}

		public Node(Board board, int moves, int manhattan, int hamming) {
			this(board, moves, false);;
		}

		public Node getPrev() {
			return this.prev;
		}

		public Board getBoard() {
			return this.board;
		}

		public int getMoves() {
			return this.moves;
		}

		public void setPrev(Node prev) {
			this.prev = prev;
		}

		public boolean isTwin() {
			return this.isTwin;
		}

		public int getPriority()  {
			return this.priority;
		}


		@Override
		public int compareTo(Node n) {
			
			int priorityOfN1 = this.getPriority();
			int priorityOfN2 = n.getPriority();

			if(priorityOfN1 > priorityOfN2) return 1;

			if(priorityOfN1 < priorityOfN2) return -1;

			//break the ties 

			int manhattanOfN1 = priorityOfN1 - this.getMoves();
			int manhattanOfN2 = priorityOfN2 - n.getMoves();

			if(manhattanOfN1 > manhattanOfN2) return 1;

			if(manhattanOfN1 < manhattanOfN2) return -1;

			return 0;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(board);
			builder.append("the manhattan: " + board.manhattan());
			builder.append("\n");
			builder.append("the hamming: " + board.hamming());
			builder.append("\n");
			builder.append("the moves: " + moves);
			builder.append("\n");
			builder.append("the priority: " + (board.manhattan() + moves));
			builder.append("\n");
			return builder.toString();	
		}
	}

	public Solver(Board initial) {

		if(null == initial) throw new IllegalArgumentException("");	
		search(initial);
	}

	private void search(Board initial) {
		
		//root node
		Node cNode = new Node(initial, 0, false);
		//root twin node
		Node ctwinNode = new Node(initial.twin(), 0, true);

		MinPQ<Node> minPQ = new MinPQ<Node>();
		minPQ.insert(cNode);
		minPQ.insert(ctwinNode);
		//loop 	
		while(!minPQ.isEmpty()) {

			Node node = minPQ.delMin(); 
			//check it it gobal 
			Board board = node.getBoard();

			boolean isTwin = node.isTwin();

			if(board.isGoal()) {
				//the twin find it 	
				if(isTwin) {	
				  solutionNode = null;
				  isSolvable = false;
				}else {
				  solutionNode = node;
				  isSolvable = true;
				}
				break;
			}else {
				//can go it ...
				for(Board b : board.neighbors()){
						//record the parent ?
					if(!inPredecessor(node, b)) {	
						Node newNode = new Node(b, node.getMoves() + 1, isTwin);
						//link the predecessor
						newNode.setPrev(node);	
						minPQ.insert(newNode); //increase moves	
					}
				}
			}
		}	
	}

	private boolean inPredecessor(Node node, Board b) {

		Node predecessor = node.getPrev();

		if(null == predecessor) return false;

		Board b1 = predecessor.getBoard();

		return b.equals(b1);
	}

	private Node getRoot(Node node) {

		if(null == node) return null;

		while(node.getPrev() != null) {
			node = node.getPrev();
		}

		return node;
	}
	private void debug(Node c) {
	}



	public boolean isSolvable() {
		return this.isSolvable;	
	}

	public int moves() {
		return null == solutionNode ? -1 : solutionNode.getMoves(); //get the moves
	}


	public Iterable<Board> solution() {
		//cache it .. can solve it 	
		if( isSolvable && null == list) {

			list = new LinkedList<Board>();	

			Stack<Board> stack = new Stack<Board>();

			Node node = solutionNode;
			while(node != null) {
				stack.push(node.getBoard()); 
				node = node.getPrev();
			}	

			while(!stack.isEmpty()) {
				list.add(stack.pop());
			}
		}

		return list; 
	}	

	public static void main(String[] args) {
	 // create initial board from file
	  In in = new In(args[0]);
	  int n = in.readInt();
	  int[][] blocks = new int[n][n];
	    for (int i = 0; i < n; i++)
	     for (int j = 0; j < n; j++)
	           blocks[i][j] = in.readInt();
	           Board initial = new Board(blocks);

                // solve the puzzle
                Solver solver = new Solver(initial);

                // print solution to standard output
                if (!solver.isSolvable()) {
                  StdOut.println("No solution possible");
				  StdOut.println("the solution " + solver.solution());
				}

                else {
                 StdOut.println("Minimum number of moves = " + solver.moves());
                for (Board board : solver.solution())
                     StdOut.println(board);
                }
	}

}
