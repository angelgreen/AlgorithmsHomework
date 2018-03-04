import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {

	private LineSegment[] lineSegmentList;
	private int numberOfSegments;	

	private final Point[] dupPoints;

	private Point[] originPoints; //the line origin point

	public FastCollinearPoints(Point[] points) {

	  if(null == points) throw new IllegalArgumentException("");	
	  
	  dupPoints = Arrays.copyOf(points, points.length);

	  validate(dupPoints);

	  this.lineSegmentList = new LineSegment[2];
	  this.originPoints = new Point[2];

	  find();
	}	


	private void find() {

		for(int i = 0; i < dupPoints.length; i++) {

			Point origin = dupPoints[i];

			//copy dupPoints
			Point[] sortPoints = Arrays.copyOfRange(dupPoints, i, dupPoints.length);

			Comparator<Point> slopeOrder = origin.slopeOrder();

			Arrays.sort(sortPoints, slopeOrder);

			int n = 1;

			double slope1 = origin.slopeTo(sortPoints[0]);

			for(int j = 1; j < sortPoints.length; j++) {
				double slope2 = origin.slopeTo(sortPoints[j]);
				//equals
				if(slope2 == slope1) {
					n++;
				}
				else {
					//slope not equals 
					  //find more than 4 point 
					if(n >= 3) {
						Point dest = sortPoints[j - 1];
						findAndPut(origin, dest);
					}	
					//find not equals
					slope1 = slope2; 
					n = 1;
				}	
			}
			//the end one
			if(n >= 3) {
				Point dest = sortPoints[sortPoints.length - 1];
				findAndPut(origin, dest);
			}	
		}	
	  this.lineSegmentList = Arrays.copyOf(this.lineSegmentList, numberOfSegments);
	}

	//find and put lineSegment 
	private void findAndPut(Point origin, Point dest) {

		//find duplicate line
		for(int i = 0; i < numberOfSegments; i++) {

			Point s1 = originPoints[i]; //the line origin point 

			double slope1 = s1.slopeTo(origin);
			double slope2 = s1.slopeTo(dest);
			//the same line ?
			if(slope1 == slope2) {
				return; //find the same line
			}
		}

		if(numberOfSegments == lineSegmentList.length) {
			lineSegmentList = Arrays.copyOf(lineSegmentList, 2 * lineSegmentList.length);
		}

		if(numberOfSegments == originPoints.length) {
			originPoints = Arrays.copyOf(originPoints, 2 * originPoints.length);
		}

		int index = numberOfSegments;

		lineSegmentList[index] = new LineSegment(origin, dest);
		originPoints[index] = origin; //the origin point 

		numberOfSegments++;
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
