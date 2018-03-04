import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {


	private class LineSegmentInfo {
		public Point origin; 
		public Point dest;

		public LineSegmentInfo(Point origin, Point dest) {
			this.origin = origin;
			this.dest = dest;
		}
	}	

	private LineSegmentInfo[] lineSegmentInfoList;
	private LineSegment[] lineSegmentList;
	private int numberOfSegments;	

	private final Point[] dupPoints;

	public FastCollinearPoints(Point[] points) {

	  if(null == points) throw new IllegalArgumentException("");	
	  
	  dupPoints = Arrays.copyOf(points, points.length);

	  validate(dupPoints);

	  this.lineSegmentInfoList = new LineSegmentInfo[2];
	  find();
	}	


	private void find() {


		for(int i = 0; i < dupPoints.length; i++) {

			Point origin = dupPoints[i];

			Comparator<Point> slopeOrder = origin.slopeOrder();

			Arrays.sort(dupPoints, i, dupPoints.length, slopeOrder);

			int n = 1;

			double slope1 = origin.slopeTo(dupPoints[i]);

			for(int j = i+1; j < dupPoints.length; j++) {
				double slope2 = origin.slopeTo(dupPoints[j]);
				//equals
				if(slope2 == slope1) {
					n++;
				}
				else {
					//slope not equals 
					  //find more than 4 point 
					if(n >= 3) {
						Point dest = dupPoints[j - 1];
						findAndPut(origin, dest);
					}	
					//find not equals
					slope1 = slope2; 
					n = 1;
				}	
			}
			//the end one
			if(n >= 3) {
				Point dest = dupPoints[dupPoints.length - 1];
				findAndPut(origin, dest);
			}	

			Arrays.sort(dupPoints);
		}	
	  this.lineSegmentInfoList = Arrays.copyOf(this.lineSegmentInfoList, numberOfSegments);
	}

	//find and put lineSegment 
	private void findAndPut(Point origin, Point dest) {

		//find duplicate line
		for(int i = 0; i < numberOfSegments; i++) {

			LineSegmentInfo lineSegmentInfo = this.lineSegmentInfoList[i];

			Point s1 = lineSegmentInfo.origin;
			Point s2 = lineSegmentInfo.dest;

			double slope = s1.slopeTo(s2);
			double slope1 = origin.slopeTo(dest);
			double slope2 = s1.slopeTo(origin);
			//the same line ?
			if(slope == slope1 && slope == slope2) {
				return; //find the same line
			}
		}

		if(numberOfSegments == lineSegmentInfoList.length) {
			lineSegmentInfoList = Arrays.copyOf(lineSegmentInfoList, 2 * lineSegmentInfoList.length);
		}

		lineSegmentInfoList[numberOfSegments++] = new LineSegmentInfo(origin, dest);
	}


	public int numberOfSegments() {
		return this.numberOfSegments;	
	}

	public LineSegment[] segments() {

		if(null == lineSegmentList) {
			this.lineSegmentList = new LineSegment[lineSegmentInfoList.length];
			for(int i = 0; i < numberOfSegments; i++) {
				LineSegmentInfo lineSegmentInfo = lineSegmentInfoList[i];	
				lineSegmentList[i] = new LineSegment(lineSegmentInfo.origin, lineSegmentInfo.dest);
			}
		}	
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
