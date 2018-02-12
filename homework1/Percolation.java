import edu.princeton.cs.algs4.WeightedQuickUnionUF;
//import edu.princeton.cs.algs4.StdOut;

public  class Percolation {

	
	private final int topN;
	private final int bottomN;

	private final boolean[][] array;

	private final WeightedQuickUnionUF UF;

	private final int n;

	private int numberOfOpenSites = 0;

	public Percolation(int n) {
		if(n <= 0) throw new IllegalArgumentException("");	


		this.n = n;
		this.UF = new WeightedQuickUnionUF(2*n*n + 2);

		this.topN = 0;
		this.bottomN = n*n + 1;

		this.array = new boolean[n+1][n+1]; //from 1 to n 
		
		//first blocked
		for(int i = 1; i <= n;i++) {
			for(int j = 1; j <=n;j++){
				array[i][j] = true;
			}
		}
	}	

	private int getNum(int row, int col) {
		if(row < 1 || row > n) throw new IllegalArgumentException("row error");	
		if(col < 1 || col > n) throw new IllegalArgumentException("col error");

		return (row-1)*n + col;
	}

	public void open(int row, int col) {
		if(row < 1 || row > n) throw new IllegalArgumentException("row error");	
		if(col < 1 || col > n) throw new IllegalArgumentException("col error");

		if(isOpen(row,col)) return;

		//array[row][col] = (row-1)*n + col; //marked it
		array[row][col] = false; //not blocked

		int p = getNum(row,col);

		int o = p + n*n + 1;

		if(row == 1) {
			//union topN
			UF.union(p, topN);
			//add the other one
			UF.union(o, topN);
		}

		if(row == n) {
		//union bottomN
			UF.union(p, bottomN);
		}

		//the top
		int top = row - 1;
		int bottom = row + 1;
		int left = col - 1;
		int right = col + 1;

		//the top node
		if(top >= 1) {
			int Ptop = getNum(top,col);	
			int PtopOther = Ptop + n*n + 1;
			if(isOpen(top,col)) {
				UF.union(Ptop,p);	
				UF.union(PtopOther,o);
			}
		}
		//the bottom node
		if(bottom <= n) {
			int Pbottom = getNum(bottom,col);
			int PbottomOther = Pbottom + n*n + 1;
			if(isOpen(bottom,col)){
				UF.union(Pbottom,p);
				UF.union(PbottomOther, o);
			}
		}
		//the left node
		if(left >= 1) {
			int Pleft = getNum(row,left);
			int PleftOther = Pleft + n*n + 1;
			if(isOpen(row,left)) {
				UF.union(Pleft, p);
				UF.union(PleftOther, o);
			}
		}
		//the right node
		if(right <= n) {
			int Pright = getNum(row,right);
			int PrightOther = Pright + n*n + 1;
			if(isOpen(row,right)) {
				UF.union(Pright,p);
				UF.union(PrightOther,o);
			}
		}

		numberOfOpenSites++;
	}

	public boolean isOpen(int row, int col) {
		if(row < 1 || row > n) throw new IllegalArgumentException("row error");	
		if(col < 1 || col > n) throw new IllegalArgumentException("col error");
		return !array[row][col];
	}

	public boolean isFull(int row, int col) {
		if(row < 1 || row > n) throw new IllegalArgumentException("row error");	
		if(col < 1 || col > n) throw new IllegalArgumentException("col error");

		boolean opened = isOpen(row, col);
		if(!opened) return false;

		int p = getNum(row, col);

		int o = p + n*n + 1; //the other one 

		return UF.connected(topN,o);
	}

	public int numberOfOpenSites(){
		return this.numberOfOpenSites;	
	}

	public boolean percolates() {
		return UF.connected(topN, bottomN);
	}

	public static void main(String[] args){
//		Percolation percolation = new Percolation(3);

/**		for(int i = 0; i < 20;i++) {

			int p = StdRandom.uniform(1, 3*3 + 1);

			StdOut.println("the random is " + p);

			int row = p / 3;
			int col = p % 3;
			if(col != 0) {
				row = row + 1;
			}else {
				col = 3;
			}
			percolation.open(row, col);

			StdOut.printf("the row %d, col %d\n",row,col);
		}
		**/
/**		percolation.open(1,3);
		percolation.open(2,3);
		percolation.open(3,3);
		percolation.open(3,1);

		StdOut.println("the isFull(3, 1) = " + percolation.isFull(3,1));
		StdOut.println("percolates " + percolation.percolates());
		StdOut.printf("the numberOfOpenSites %d\n", percolation.numberOfOpenSites());
		**/
//		percolation.open(2,1);
//		percolation.open(3,1);

//		StdOut.println("the full is " + percolation.isFull(1,1));

//		boolean isPercolation = percolation.percolates();

//		StdOut.println("percolates is " + isPercolation);
	}	
}
