import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

	private final Point[] dupPoints;	
	private LineSegment[] lineSegmentList;
	private int numberOfSegments;

	public BruteCollinearPoints(Point[] points) {

		if(null == points) throw new IllegalArgumentException("");	

		dupPoints = Arrays.copyOf(points, points.length);

		validate(dupPoints);

		this.lineSegmentList = new LineSegment[2];

		find(); // do find operation
	}	

	private void find() {
		//bruted it 
	 for(int i = 0; i < dupPoints.length - 3; i++) {
	  for(int j = i + 1; j < dupPoints.length - 2; j++) {
	   for(int k = j + 1; k < dupPoints.length - 1; k++) {
	    for(int m  = k + 1; m < dupPoints.length; m++) {
		  Point p = dupPoints[i];
		  Point q = dupPoints[j];
		  Point r = dupPoints[k];
		  Point s = dupPoints[m];
		  //check the p,q,r,s collinear
		  double slope1 = p.slopeTo(q);
	      double slope2 = p.slopeTo(r);
		  double slope3 = p.slopeTo(s);
		  if(slope1 == slope2 && slope1 == slope3) {
		    //the four points collinear
		    LineSegment lineSegment = new LineSegment(p,s);
		    //expand the arrays 
		    if(numberOfSegments == lineSegmentList.length) {
		      lineSegmentList = Arrays.copyOf(lineSegmentList, 2*lineSegmentList.length);	
			}
			lineSegmentList[numberOfSegments++] = lineSegment;
		   }	
		  }
		 }	
		}
	   }
	//we find it ..
	this.lineSegmentList = Arrays.copyOf(this.lineSegmentList, numberOfSegments);
   }
	//validate the input pointss
	private void validate(Point[] points) {

		if(null == points ) throw new IllegalArgumentException("");	

		for(int i = 0; i < points.length; i++) {
			Point p = points[i];
			if(null == p) throw new IllegalArgumentException("");
		}
		//repeated points ?
		Arrays.sort(points);

		for(int i = 0; i < points.length - 1; i++) {
				//find the repeated points
			if(points[i].compareTo(points[i+1]) == 0) {
				throw new IllegalArgumentException("");
			}
		}
	}

	public int numberOfSegments() {
		return this.numberOfSegments;
	}

	public LineSegment[] segments() {

		return Arrays.copyOf(this.lineSegmentList, this.lineSegmentList.length);	
			
	//	return this.lineSegmentList;
	}

	public static void main(String[] args) {
		//TODO angelgreen
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n ; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}	
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		for(Point p: points) {
			p.draw();
		}
		StdDraw.show();
		 // print and draw the line segments
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
		  StdOut.println(segment);
		  segment.draw();
		}
		StdDraw.show();
	}
}
