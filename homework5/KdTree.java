import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;
import java.util.Arrays;
public class KdTree {

	//the KdTreeNode 
	private static class KdTreeNode {

		private KdTreeNode lb; // the left/bottom subtree
		private KdTreeNode rt; // the right/top subtree
		private Point2D point2D;
		private RectHV rectHV; //the axis-aligned rectangle corresponding to this node
		private int size;

		public KdTreeNode(
					   	KdTreeNode lb,
					   	KdTreeNode rt,
						RectHV rectHV,
						Point2D point2D,
						int size){
			this.lb = lb;
			this.rt = rt;
			this.point2D = point2D;
			this.rectHV = rectHV;
			this.size = size;
		}

		public Point2D getPoint2D() {
			return this.point2D;
		}

		public RectHV getRectHV() {
			return this.rectHV;
		}

		public KdTreeNode getLeft() {
			return this.lb;
		}

		public KdTreeNode getBottom() {
			return this.lb;
		}

		public KdTreeNode getRight() {
			return this.rt;
		}

		public KdTreeNode getTop() {
			return this.rt;
		}

		@Override
		public String toString() {
			return point2D.toString();
		}
	}	

	private KdTreeNode root;
	private int count; //the KdTree count

	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private static final int BOTTOM = 3;
	private static final int TOP = 4;

	public KdTree() {
		this.root = null; // the constructor	
	}	

	public boolean isEmpty() {
		return this.root == null;	
	}

	public int size() {
		return size(root); //return the size of root	
	}

	public void insert(Point2D p) {
		if(null == p) throw new IllegalArgumentException("");
		 root = put(TOP, p, null, root);
	}

	//insert the kdTreeNode to kdTree
	private KdTreeNode put(int orientation, //orientation vertical or horizontal
						   Point2D p,
						   KdTreeNode kdTreeNodeParent,
					       KdTreeNode kdTreeNode) {
			//no child 
		if(null == kdTreeNode) {
			if(null == kdTreeNodeParent) {
				//the largest rectHV
				RectHV rectHV = new RectHV(0, 0, 1, 1);
				return new KdTreeNode(null, null, rectHV, p, 1);
			}else {
				//the parent rectHV
				RectHV parentRectHV = kdTreeNodeParent.getRectHV();
				Point2D parentPoint2D = kdTreeNodeParent.getPoint2D();
				//from left
				if(orientation == LEFT) {
					RectHV  rectHV = new RectHV(parentRectHV.xmin(), 
												 parentRectHV.ymin(),		
												 parentPoint2D.x(),
												 parentRectHV.ymax());
					return new KdTreeNode(null, null, rectHV, p, 1);
				//from right	
				}else if(orientation == RIGHT) {
					RectHV rectHV = new RectHV(parentPoint2D.x(),
											   parentRectHV.ymin(),
										   	   parentRectHV.xmax(),
											   parentRectHV.ymax());
			 		return new KdTreeNode(null, null, rectHV, p, 1);		
				//from bottom	
				}else if(orientation == BOTTOM) {
					RectHV rectHV = new RectHV(parentRectHV.xmin(),
											   parentRectHV.ymin(),
											   parentRectHV.xmax(),
											   parentPoint2D.y());
					return new KdTreeNode(null, null, rectHV, p, 1);
				}else {
					//from top
					RectHV rectHV = new RectHV(parentRectHV.xmin(),
											   parentPoint2D.y(),
											   parentRectHV.xmax(),
											   parentRectHV.ymax());
					return new KdTreeNode(null, null, rectHV, p, 1);
				}
			}	
		}


		if(!kdTreeNode.getPoint2D().equals(p)) {
		//from left or right partition ?
		  if(orientation == LEFT || orientation == RIGHT) {
			//then top or bottom partition 	
			  if(p.y() < kdTreeNode.getPoint2D().y()) {
				kdTreeNode.lb = put(BOTTOM,
									p,
							   		kdTreeNode,
								   	kdTreeNode.lb);	
			  }else {
				kdTreeNode.rt = put(TOP,
									 p,
							     	 kdTreeNode, 
									 kdTreeNode.rt);
			  }			
		   //from bottom or top partition ?	
		   }else {
			//then left or right partion 
			 if(p.x() < kdTreeNode.getPoint2D().x()) {
				kdTreeNode.lb = put(LEFT, 
									  p,
								      kdTreeNode,
									  kdTreeNode.lb);
			 }else {
				kdTreeNode.rt = put(RIGHT,
							   		   p,	
								       kdTreeNode, 
									   kdTreeNode.rt);
			 }
		  }
	  }

		//update the size ..
		kdTreeNode.size = 1 + size(kdTreeNode.lb) + size(kdTreeNode.rt);

		return kdTreeNode;
	}

	private int size(KdTreeNode node) {
		if(null == node) return 0;
		else return node.size;
	}

	public boolean contains(Point2D p) {
		if(null == p) throw new IllegalArgumentException("");

		boolean[] container = new boolean[1];
		container[0] = false;

		innerContains(root, p, container);

		return container[0];
	}

	private void innerContains(KdTreeNode node, Point2D p, boolean[] container) {

		if(null == node) return;

		Point2D point2D = node.getPoint2D();

		if(point2D.equals(p)) {
			container[0] = true;
		}

		KdTreeNode lbKdTreeNode = node.lb;

		if(null != lbKdTreeNode) {
			RectHV lbRectHV = lbKdTreeNode.getRectHV();
			if(lbRectHV.contains(p)) {
				innerContains(lbKdTreeNode, p, container);
			}
		}

		KdTreeNode rtKdTreeNode = node.rt;

		if(null != rtKdTreeNode) {
			RectHV rtRectHV = rtKdTreeNode.getRectHV();
			if(rtRectHV.contains(p)) {
				innerContains(rtKdTreeNode, p, container);
			}
		}
	}

	public void draw() {
		innerDraw(TOP, root);
	}

	private void innerDraw(int orientation, KdTreeNode node) {

		if(null == node) return;	
		//first draw point
		StdDraw.setPenRadius(0.05);
		StdDraw.setPenColor(StdDraw.BLACK); //black 
		Point2D point2D = node.getPoint2D(); //get the point2D
		double x = point2D.x();
		double y = point2D.y();
		StdDraw.point(x, y); //draw the point

		StdDraw.setPenRadius(0.01);
		RectHV rectHV = node.getRectHV(); //get the rectange that contains the point
		//orientation left right
		if(orientation == LEFT || orientation == RIGHT) {
			StdDraw.setPenColor(StdDraw.BLUE);	
			StdDraw.line(rectHV.xmin(), y, rectHV.xmax(), y);
			innerDraw(TOP, node.rt);
			innerDraw(BOTTOM, node.lb);		
		//orientation top bottom
		}else {
			StdDraw.setPenColor(StdDraw.RED);	
			StdDraw.line(x, rectHV.ymin(), x, rectHV.ymax());	
			innerDraw(LEFT, node.lb);
			innerDraw(RIGHT, node.rt);
		}
	}

	public Iterable<Point2D> range(RectHV rect) {

		if(null == rect) throw new IllegalArgumentException("");	

		LinkedList<Point2D> queue = new LinkedList<Point2D>();	
		innerRange(root, rect, queue);
		return queue;
	}

	private void innerRange(KdTreeNode node, RectHV rect, LinkedList<Point2D> queue) {

		if(null == node) return; //just return 

		RectHV rectHV = node.getRectHV();
		Point2D point2D = node.getPoint2D();
		//intersects it ?
		if(rect.contains(point2D)) {
			queue.add(point2D); //add it to queue 
		}

		if(node.lb != null) {
			RectHV rectHVOflb = node.lb.getRectHV();
			if(rect.intersects(rectHVOflb)) {
				innerRange(node.lb, rect, queue);
			}
		}

		if(node.rt != null) {
			RectHV rectHVOfrt = node.rt.getRectHV();
			if(rect.intersects(rectHVOfrt)) {
				innerRange(node.rt, rect, queue);
			}
		}
	}

	public Point2D nearest(Point2D p) {

		if(null == p) throw new IllegalArgumentException("");

		if(null == root) return null;

		Point2D[] container = new Point2D[1];
		container[0] = root.getPoint2D();

		innerNearest(TOP, root, p, container);

		return container[0];

	}

	private void innerNearest(int orientation, KdTreeNode node, Point2D p, Point2D[] container) {

		if(null == node) return;

		Point2D nearestPoint2D = container[0];
		double nearestDistance = nearestPoint2D.distanceSquaredTo(p);

		Point2D p1 = node.getPoint2D();
		double distance = p1.distanceSquaredTo(p);

		if(distance < nearestDistance) {
			nearestDistance = distance;
			container[0] = p1;
		}

		double distanceTolbRect = 2.0;
		double distanceTortRect = 2.0;

		if(null != node.lb) {
			RectHV lbRectHV = node.lb.getRectHV();
			distanceTolbRect = lbRectHV.distanceSquaredTo(p);
		}
		
		if(null != node.rt) {
			RectHV rtRectHV = node.rt.getRectHV();
			distanceTortRect = rtRectHV.distanceSquaredTo(p);
		}

		//both first to 
		if(distanceTolbRect < nearestDistance && distanceTortRect < nearestDistance) {
			//
			RectHV lbRectHV = node.lb.getRectHV();
			//from top or bottom 	
			if(orientation == TOP || orientation == BOTTOM) {
				//left or right
				double x = node.getPoint2D().x();
				double x1 = node.lb.getPoint2D().x();
				//on the same side of the splitting line ?
				if(p.x() < x && x1 < x) {
					innerNearest(LEFT, node.lb, p, container);
					innerNearest(RIGHT, node.rt, p, container);
				}else {
					innerNearest(RIGHT, node.rt, p, container);
					innerNearest(LEFT, node.lb, p, container);
				}
			}else {
				double y = node.getPoint2D().y();
				double y1 = node.lb.getPoint2D().y();
				//on the same side of the splitting line ?
				if(p.y() < y && y1 < y) {
					innerNearest(BOTTOM, node.lb, p, container);
					innerNearest(TOP, node.rt, p, container);
				}else {
					innerNearest(TOP, node.rt, p, container);
					innerNearest(BOTTOM, node.lb, p, container);
				}	
			}	
		}else {
		   if(distanceTolbRect < nearestDistance) {
			//update the nearest Point2D		   
			if(orientation == LEFT || orientation == RIGHT) {
			 innerNearest(BOTTOM, node.lb, p, container);
			}else {
				innerNearest(LEFT, node.lb, p, container);
			}
		   }
		   if(distanceTortRect < nearestDistance) {
			if(orientation == LEFT || orientation == RIGHT) {	   
				innerNearest(TOP, node.rt, p, container);
			}else {
				innerNearest(RIGHT, node.rt, p, container);
			}
		   }
		}
	}

	private void updateNearestPoint(double nearestDistance,KdTreeNode node, Point2D p, Point2D[] container) {
		if(null == node) return ;
		Point2D p1 = node.getPoint2D();
		double d1 = p1.distanceSquaredTo(p);
		if(d1 < nearestDistance)
			container[0] = p1;	
	}


	public static void main(String[] args) {

		KdTree kdtree = new KdTree();

		Point2D p = new Point2D(0.5, 0.5);
		Point2D p1 = new Point2D(0.25, 0.25);

		kdtree.insert(p);
		kdtree.insert(p1);

		System.out.println("nearest : " + kdtree.nearest(new Point2D(0.1, 0.1)));

		//System.out.println("size: " + kdtree.size());

		//System.out.println("contains: " + kdtree.contains(p));
	}
}
