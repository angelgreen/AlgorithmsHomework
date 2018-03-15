import java.util.TreeSet;
import java.util.LinkedList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

	private final TreeSet<Point2D> point2DSet;

	public PointSET() {
		this.point2DSet = new TreeSet<Point2D>();	
	}	

	public boolean isEmpty() {
		return point2DSet.isEmpty(); //	
	}	

	public int size() {
		return point2DSet.size();	
	}

	public void insert(Point2D p) {
		if(null == p) throw new IllegalArgumentException("");
		point2DSet.add(p);		
	}

	public boolean contains(Point2D p) {
		if(null == p) throw new IllegalArgumentException("");
		return point2DSet.contains(p);	
	}

	public void draw() {
			//draw all point2D
		for(Point2D point2D: point2DSet) 
			point2D.draw();	
	}

	public Iterable<Point2D> range(RectHV rect) {
		if(null == rect) throw new IllegalArgumentException("");
		double xmin = rect.xmin();
		double xmax = rect.xmax();

		double ymin = rect.ymin();
		double ymax = rect.ymax();	

		LinkedList<Point2D> list = new LinkedList<Point2D>();

		for(Point2D point2D: point2DSet) {
			double x = point2D.x();
			double y = point2D.y();
			if((x >= xmin && x <= xmax) && (y >= ymin && y <= ymax)) {
				list.add(point2D);
			}
		}	

		return list;
	}

	public Point2D nearest(Point2D p) {
		if(null == p) throw new IllegalArgumentException("");
		double minDistanceSquare = Double.MAX_VALUE;
		Point2D point2D = null;
		for(Point2D p1: point2DSet) {
			double distanceSquare = p.distanceSquaredTo(p1); 	
			if(minDistanceSquare > distanceSquare) {
				minDistanceSquare = distanceSquare;
				point2D = p1;
			}	
		}
		return point2D;
	}

	public static void main(String[] args) {
		//TODO angelgreen
	}
}
