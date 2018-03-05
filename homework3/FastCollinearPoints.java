import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {

	private LineSegment[] lineSegmentList;
	private int numberOfSegments;	

	private final Point[] dupPoints;

	public FastCollinearPoints(Point[] points) {

	  if(null == points) throw new IllegalArgumentException("");	
	  
	  dupPoints = Arrays.copyOf(points, points.length);

	  validate(dupPoints);

	  this.lineSegmentList = new LineSegment[2];

	  find();
	}	


	private void find() {

		for(int i = 0; i < dupPoints.length; i++) {

			Point origin = dupPoints[i];

			//copy dupPoints
			Point[] sortPoints = Arrays.copyOf(dupPoints,dupPoints.length);

			Arrays.sort(sortPoints, origin.slopeOrder());

			int n = 1;

			double slope1 = origin.slopeTo(sortPoints[0]);
			Point min = sortPoints[0];

			for(int j = 1; j < sortPoints.length; j++) {
				double slope2 = origin.slopeTo(sortPoints[j]);
				//equals
				if(slope2 == slope1) {
					Point s = sortPoints[j];
					//find the min point
					if(s.compareTo(min) < 0) {
						min = s;
					}	
					n++;
				}
				else {
					//slope not equals 
					  //find more than 4 point 
					if(n >= 3) {
						Point dest = sortPoints[j - 1];
						//if the min is less than origin the line not bigger
						// a -> b -> c -> d //the biggest line segment
						//  b -> a -> c -> d //the smaller line segment 
						if(min.compareTo(origin) < 0) {
							//find the largest line segment
						//	findAndPut(origin, dest);
						}else {
							findAndPut(origin, dest);
						}
					}	
					//find not equals
					slope1 = slope2; 
					n = 1;
					min = sortPoints[j]; //save the minPoints
				}	
			}
			//the end one
			if(n >= 3) {
				Point dest = sortPoints[sortPoints.length - 1];
				//fnd the largest line segment
				if(min.compareTo(origin) < 0) {
					//findAndPut(origin, dest);
				}else {
					findAndPut(origin, dest);
				}
			}	
		}	

		//
		lineSegmentList = Arrays.copyOf(lineSegmentList, numberOfSegments);
	}

	private void findAndPut(Point origin, Point dest) {

		if(numberOfSegments == lineSegmentList.length) {
			lineSegmentList = Arrays.copyOf(lineSegmentList, 2 * lineSegmentList.length);
		}
		lineSegmentList[numberOfSegments++] = new LineSegment(origin, dest);
	}

	public int numberOfSegments() {
		return this.numberOfSegments;	
	}

	public LineSegment[] segments() {
		return Arrays.copyOf(this.lineSegmentList, this.lineSegmentList.length);
	}


	private void validate(Point[] points) {
	  if(null == points) throw new IllegalArgumentException("");	
	  for(int i = 0; i < points.length; i++) {
		Point p = points[i];
		if(null == p) throw new IllegalArgumentException("");
	  }
		//repeated point ?
	  Arrays.sort(points);
	  for(int i = 0; i < points.length - 1; i++) {
		//find the repeated point
	    if(points[i].compareTo(points[i+1]) == 0) {
	    	throw new IllegalArgumentException("");
	    }
	  }
	}

	public static void main(String[] args) {
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
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
		  StdOut.println(segment);
		  segment.draw();
		}
		StdDraw.show();
	}
}
