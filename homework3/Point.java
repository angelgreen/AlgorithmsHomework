import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
	
	private final int x; 
	private final int y;


	private class PointComparator implements Comparator<Point> {

		@Override
		public int compare(Point o1, Point o2) {

			double slope1 = slopeTo(o1);

			double slope2 = slopeTo(o2);

			if(slope1 < slope2) return -1;

			if(slope1 == slope2) return 0;

			return 1;
		}	
	}

	public Point(int x, int y) {
		validate(x);
		validate(y);
		this.x = x;
		this.y = y;	
	}	

	private void validate(int o) {
		if(o < 0 || o > 32767) throw new IllegalArgumentException(""); 
	}

	private int getX() { return this.x; }

	private int getY() { return this.y; }

	public void draw() {
//	    StdDraw.setPenRadius(0.010);
//		StdDraw.setPenColor(StdDraw.BLUE);
		 StdDraw.point(x, y);	
	}

	public void drawTo(Point that) {
//		StdDraw.setPenRadius(0.002);	
		StdDraw.line(this.x, this.y, that.x, that.y);	
	}

	public String toString() {
		return "(" + x + ", " + y + ")";	
	}

	public int compareTo(Point that) {
		int thatY = that.getY();

		if(this.y < thatY) return -1;
		if(this.y > thatY) return 1;
		
		int thatX = that.getX();

		if(this.x < thatX) return -1;
		if(this.x > thatX) return 1;

		return 0;
	}

	public double slopeTo(Point that) {
		int thatX = that.getX();
		int thatY = that.getY();

		if(this.y == thatY && this.x == thatX) //degenerate
			return Double.NEGATIVE_INFINITY;

		if(this.y == thatY) return 0; //horizontal 

		if(this.x == thatX) return Double.POSITIVE_INFINITY; //vertical 

		return (thatY - this.y) / (double)(thatX - this.x);
	}
	public Comparator<Point> slopeOrder() {
		return new PointComparator();
	}

	public static void main(String[] args) {
		//TODO angelgreen
	}
}
