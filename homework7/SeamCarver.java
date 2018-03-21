import edu.princeton.cs.algs4.Picture;
import java.util.Stack;
import java.util.Arrays;

public class SeamCarver {

	private double[][] energyCache; //the enery cache

	private int[][] pictureMatrix;

	public SeamCarver(Picture picture) {

		if(null == picture) throw new IllegalArgumentException("");

		int width = picture.width();
		int height = picture.height();

		this.energyCache = new double[width][height]; 

		this.pictureMatrix = new int[width][height];

		//copy the picture ...
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
					//fuck 
				pictureMatrix[i][j] = picture.getRGB(i, j);	
			}
		}

		//TODO angelgreen ? 
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				energyCache[i][j] = -1; 
			}
		}
	}	

	//return a copy of this picture ...
	public Picture picture() {
		int width = width();
		int height = height();	
		Picture picture = new Picture(width, height);
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				picture.setRGB(i, j, pictureMatrix[i][j]);	

		return picture;
	}

	public int width() {
		return pictureMatrix.length;	
	}

	public int height() {
		return pictureMatrix[0].length;
	}

	public double energy(int x, int y) {
		if( x < 0 || x > width() - 1) throw new IllegalArgumentException("");
		if( y < 0 || y > height() - 1) throw new IllegalArgumentException("");
		return getEnergy(x, y);	
	}

	private double getEnergy(int x, int y) {

		double energy = energyCache[x][y];

	   if(energy == -1) {
			//no cache
			if(isBoundary(x,y)) {
				energyCache[x][y] = 1000.00;
				energy = 1000.00; 
			}else {
				energy = caculateEnergy(x,y);
				energyCache[x][y] = energy;
			}
	   }	   

	   return energy;
	}

	private double caculateEnergy(int x, int y) {

		int colorX1 = pictureMatrix[x+1][y];
	  	int colorX2 = pictureMatrix[x-1][y];

		int rx1 = (colorX1 >> 16) & 0xFF;
		int gx1 = (colorX1 >> 8) & 0xFF;
		int bx1 = (colorX1) & 0xFF;
		int rx2 = (colorX2 >> 16) & 0XFF;
		int gx2 = (colorX2 >> 8) & 0xFF;
		int bx2 = (colorX2) & 0xFF;

		double deltaRx = (rx1 - rx2)*(rx1 - rx2);
		double deltaGx = (gx1 - gx2)*(gx1 - gx2);
		double deltaBx = (bx1 - bx2)*(bx1 - bx2);

		int colorY1 = pictureMatrix[x][y+1];
		int colorY2 = pictureMatrix[x][y-1];

		int ry1 = (colorY1 >> 16) & 0xFF;
		int ry2 = (colorY2 >> 16) & 0xFF;
		int gy1 = (colorY1 >> 8) & 0xFF;
		int gy2 = (colorY2 >> 8) & 0xFF;
		int by1 = (colorY1) & 0xFF;
		int by2 = (colorY2) & 0xFF;

		double deltaRy = (ry1 - ry2)*(ry1 - ry2);
		double deltaGy = (gy1 - gy2)*(gy1 - gy2);
		double deltaBy = (by1 - by2)*(by1 - by2);

		double deltaX = deltaRx + deltaGx + deltaBx;
		double deltaY = deltaRy + deltaGy + deltaBy;

		return Math.sqrt(deltaX + deltaY);
	}


	//if the x,y is boudary 
	private boolean isBoundary(int x, int y) {
		int width = width();//get the picture width
		int height = height(); //get the picture height
		//the top
		if(y == 0 || y == height - 1) return true;
		if(x == 0 || x == width - 1) return true;
		return false;
	}

	//find the horizontalSeam ...
	public int[] findHorizontalSeam() {

		transpose(); //start transpose 

		int[] horizontalSeam = findVerticalSeam();

		transpose(); //transpose again 

		return horizontalSeam;
	}

	//find the verticalSeam ...
	public int[] findVerticalSeam() {
		//find the verticalSeam...
		int width = width();//the width
		int height = height(); //the height
		double[][] H = new double[width][height];
		int[][] edgeTo = new int[width][height];
		//init the first row H 
		for(int j = 0; j < width; j++) {
			H[j][0] = energy(j, 0);
		}	

		//the H[i,j] <- min(H[i-1,j-1], H[i][j-1], H[i+1][j-1]) + c[i,j] 
		for(int j = 1; j < height; j++) {
			for(int i = 0; i < width; i++) {	
				double minH = Double.MAX_VALUE;
				int p = i - 1;
				//the H[i-1][j-1]
				if( i-1 >= 0) {
					double energy1 = H[i-1][j-1];
					if(energy1 < minH) {
						minH = energy1;
						p = i - 1;
					}	
				}			
				//the H[i][j-1]
				double energy2 = H[i][j-1];
				if(energy2 < minH) {
					minH = energy2;
					p = i;
				}
				//the H[i+1][j-1]
				if( i+1 < width) {
					double energy3 = H[i+1][j-1];
					if(energy3 < minH) {
						minH = energy3;
						p = i + 1;
					}
				}
				//update the H[i][j]
				H[i][j] = energy(i,j) + minH;
				edgeTo[i][j] = p;
			}
		}

		int k = 0;
		double minEnergy = H[0][height-1];

		//find the min in bottom one
		for(int i = 1; i < width; i++) {
			if(H[i][height-1] < minEnergy) {
				minEnergy = H[i][height-1];
				k = i;
			}
		}

		Stack<Integer> stack = new Stack<Integer>();

		int j = height - 1;

		while(j >= 0) {
			stack.push(k);
			k = edgeTo[k][j]; //got to parent node 
			j--;	
		}

		int[] verticalSeam = new int[height]; //the verticalSeam

		for(int i = 0; i < height; i++) {
			verticalSeam[i] = stack.pop(); //pop it ..
		}

		return verticalSeam;
	}

	public void removeVerticalSeam(int[] seam) {

		if( null == seam || seam.length < 1) throw new IllegalArgumentException("");

		int width = width();
		int height = height();

		if(width <= 1) throw new IllegalArgumentException("");

		transpose();
		removeHorizontalSeam(seam);
		transpose(); //back 
	}

	private void validateHorizontalSeam(int[] seam) {

			int height = height();
			int width = width();

			//the seam length should equals to width 
			if(seam.length != width ) throw new IllegalArgumentException("");

			//for the only one seam 
		if(seam.length == 1) {
			int seam3 = seam[0];
			if(seam3 < 0 || seam3 > height - 1) throw new IllegalArgumentException("");
		}		

		for(int i  = 0; i < seam.length - 1; i++) {

			int seam2 = seam[i+1]; //seam2
			int seam1 = seam[i]; //seam1 	

			//verticalSeam ?
			if(seam2 < 0 || seam2 > height - 1) throw new IllegalArgumentException("");
			if(seam1 < 0 || seam1 > height - 1) throw new IllegalArgumentException("");
		

			int diff = seam[i+1] - seam[i];
			if(diff == 0) {
				continue;
			}else if(diff == -1) {
				continue;
			}else if(diff == 1 ) {
				continue;
			}else {
				throw new IllegalArgumentException("");
			}
		}
	}

	public void removeHorizontalSeam(int[] seam) {

		if(null == seam || seam.length < 1) throw new IllegalArgumentException("");	

		int height = height();
	    int width = width();	

		if(height <= 1) throw new IllegalArgumentException("");


		validateHorizontalSeam(seam);

		for(int j = 0; j < width; j++) {
			int[] data = pictureMatrix[j];
			int i = seam[j]; //the seam index
			// [1:width-1]
			int[] data1 = new int[height - 1];
			if(i == 0) {
				System.arraycopy(data, 1, data1, 0, height - 1);	
				//boundary ...
			//the last one seam
			// [0:width - 2]
			}else if(i == height - 1) {
				System.arraycopy(data, 0, data1, 0, height - 1);	
				//boundary ...
			//the media [0:i-1] [i+1:width-1]		
			}else {
				System.arraycopy(data, 0, data1, 0, i);
				System.arraycopy(data, i+1, data1, i, data1.length - i);
			}
			pictureMatrix[j] = data1;
		}

		int height1 = height();
		int width1 = width();
		//init a new energyCache1
		double[][] energyCache1 = new double[width1][height1];
		for(int i = 0; i < width1; i++) {
			for(int j = 0; j < height1; j++) {
				energyCache1[i][j] = -1;
			}
		}
		this.energyCache = energyCache1;
	}

	private void transpose() {
		int width = width();
		int height = height();	
		int[][] transposeMatrix = new int[height][width];
		double[][] transposeEnergyCache = new double[height][width];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				transposeMatrix[j][i] = pictureMatrix[i][j];
				transposeEnergyCache[j][i] = energyCache[i][j];
			}
		}
		energyCache = transposeEnergyCache;
		pictureMatrix = transposeMatrix;
	}
}

