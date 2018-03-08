import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.LinkedList;
public class Board {

	private final char[] tiles; 
	private final int dimension; //the dimension

	private int hamming = -1; //the hamming cache
	private int manhattan = -1; //the manhattan cache

	private int blanki; //the blanki index

	private LinkedList<Board> neighbors;

	private String stringRepretation = null; //the string repretation

	public Board(int[][] blocks) {
		//first validate it ..
		if(null == blocks) throw new NullPointerException("");
		int rowLen = blocks.length; //the row length
		int colLen = blocks[0].length; //the col length
		if(rowLen != colLen) throw new IllegalArgumentException("");
		if(rowLen < 2 || rowLen >= 128) throw new IllegalArgumentException(""); //

		this.tiles = new char[rowLen * rowLen];

		int maxV = rowLen * rowLen;
		//copy the blocks
		int k = -1;
		for(int i = 0; i < rowLen; i++) {
			for(int j = 0; j < colLen; j++) {
				int v = blocks[i][j];
				// 0 represent the blank square 
				if( v < 0 || v > maxV ) throw new IllegalArgumentException(""); //the core case	
				if(v == 0) {
					if( k != -1) {
					//find more blank node 
					  throw new IllegalArgumentException("");
					}else {
						k = 0;
						blanki = i * rowLen + j;
					}
				}
				this.tiles[i * rowLen + j] = (char)v;
			}
		}

		this.dimension = rowLen;
	}

	public int dimension() {
		return this.dimension;	
	}
	public int hamming() {
			//the hamming is not cached
		if(hamming == -1) {
			hamming = 0;	
			for(int i = 0 ; i < tiles.length; i++) {
				char v = tiles[i];
				if( v == 0) continue;

				if( v != i + 1) hamming++;
			}
		}	
		return hamming; 
	}
	public int manhattan() {
			//the manhattan is not cached	
		if(manhattan == -1) {
			manhattan = 0;
			for(int i = 0; i < tiles.length; i++) {
				char v = tiles[i];
				if( v == 0) continue;
				//current index 
				int r1 = i / dimension;
				int c1 = i % dimension;

				//calcuate the origin position
				int r2 = v / dimension;
				int c2 = v % dimension;
				if(c2 == 0) {
					c2 = dimension - 1;	
					r2 = r2 - 1;
				}else {
					c2 = c2 - 1;	
				}

				manhattan += Math.abs(r2 - r1) + Math.abs(c2 - c1);
			}
		}
		return manhattan;
	}
	public boolean isGoal() {

		for(int i = 0; i < tiles.length; i++) {
			int v = (int)tiles[i]; //get the value 
			if( v == 0) continue;
			if( v != i + 1) return false;
		}	

		return true;
	}
	public Board twin() {

		int k1 = (blanki + 1) % tiles.length;
		int k2 = (blanki + 2) % tiles.length;

		//copy it ..
		char[] tilesDup = Arrays.copyOf(this.tiles, this.tiles.length);

		swap(tilesDup, k1, k2);

		return new Board(convertFrom(tilesDup));
	}

	//swap it ..
	private void swap(char[] tiles,int i, int j) {
		char tmp = tiles[i];
		tiles[i] = tiles[j];
		tiles[j] = tmp;
	}

	//change char[] to int[][]
	private int[][] convertFrom(char[] tiles) {

		int[][] blocks = new int[dimension][dimension];
		for(int i = 0; i < dimension; i++) {
			for(int j = 0; j < dimension; j++) {
				blocks[i][j] = tiles[i * dimension + j];
			}
		}
		return blocks;
	}

	public boolean equals(Object y) {

		if( this == y) return true;	

		if(null == y) return false;	

		if(this.getClass() != y.getClass()) return false;

		Board that = (Board)y;

		String s1 = this.toString();
		String s2 = that.toString();

		return s1.equals(s2);
	}


	public Iterable<Board> neighbors() {

		if(neighbors == null) {
			neighbors =  new LinkedList<Board>();	
			int r = blanki / dimension;
			int c = blanki % dimension;
			//find the top
			int topR = r - 1;
			if(topR >= 0) {
				int k1 = topR * dimension + c;
				char[] tilesDupTop = Arrays.copyOf(this.tiles, this.tiles.length);
				swap(tilesDupTop, blanki, k1);
				neighbors.add(new Board(convertFrom(tilesDupTop)));
			}
			//find the bottom
			int bottomR = r + 1;
			if(bottomR < dimension) {
				int k2 = bottomR * dimension + c;
				char[] tilesDupBottom = Arrays.copyOf(this.tiles, this.tiles.length);
				swap(tilesDupBottom, blanki, k2);
				neighbors.add(new Board(convertFrom(tilesDupBottom)));	
			}
			//find the left
			int leftC = c - 1;
			if(leftC >= 0) {
				int k3 = r * dimension + leftC;
				char[] tilesDupLeft = Arrays.copyOf(this.tiles, this.tiles.length);
				swap(tilesDupLeft, blanki, k3);
				neighbors.add(new Board(convertFrom(tilesDupLeft)));
			}
			//find the right
			int rightC = c + 1;
			if(rightC < dimension) {
				int k4 = r * dimension + rightC;
				char[] tilesDupRight = Arrays.copyOf(this.tiles, this.tiles.length);
				swap(tilesDupRight, blanki, k4);
				neighbors.add(new Board(convertFrom(tilesDupRight)));		
			}
		}	
		return neighbors;
	}	

	public String toString() {

		if(null == stringRepretation) {	
			StringBuilder builder = new StringBuilder();
			builder.append(dimension +  "\n");
			for(int i = 0; i < dimension; i++) {
				for(int j = 0; j < dimension; j++) {
					builder.append(String.format("%2d ",(int)tiles[i * dimension + j]));
				}
				builder.append("\n");
			}	
			stringRepretation =  builder.toString();
		}
		return stringRepretation;
	}

	public static void main(String[] args) {
		//TODO angelgreen
	}
}
