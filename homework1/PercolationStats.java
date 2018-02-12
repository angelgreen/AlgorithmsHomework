import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {


	private final int trials;
	private final int n;

	private final double[] x;

	public PercolationStats(int n, int trials) {
		if(n <= 0) throw new IllegalArgumentException("");	
		if(trials <= 0) throw new IllegalArgumentException("");

		this.n = n;
		this.trials = trials;
		this.x = new double[trials];

		simulation();
	}	

	private void simulation() {

		int totalNum = this.n * this.n ;	

		for(int i = 0; i < this.trials; i++) {	

			Percolation percolation = new Percolation(this.n);

		//not percolates	
			while(!percolation.percolates()){
			//do random it ...
				int row = StdRandom.uniform(1,this.n + 1);
				int col = StdRandom.uniform(1,this.n + 1);
				percolation.open(row,col);
			}

			x[i] = percolation.numberOfOpenSites() / (double)totalNum;
		}		
	}

	//cache it 
	private double mean1 = -1;
	private double stddev1 = -1;
	private double sqrtTrials = -1;
	public double mean() {
		if(mean1 < 0) {
			mean1 = StdStats.mean(this.x);			
		}
		return mean1;
	}

	public double stddev() {
		if(stddev1 < 0) {
			stddev1 = StdStats.stddev(this.x);
		}		
		return stddev1;
	}

	private double sqrt() {
		if(sqrtTrials < 0) {
			sqrtTrials = Math.sqrt(this.trials);
		}
		return sqrtTrials;
	}

	public double confidenceLo() {
		return mean() - 1.96 * stddev() / sqrt();
	}

	public double confidenceHi() {
		return mean() + 1.96 * stddev() / sqrt();
	}

	public static void main(String[] args) {
		if(args.length < 2 ) throw new IllegalArgumentException("error args");
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);

		PercolationStats  percolationStats = new PercolationStats(n, trials);

		StdOut.printf("the mean = %f\n", percolationStats.mean());
		StdOut.printf("the stddev = %f\n", percolationStats.stddev());
		StdOut.printf("95%% confidence interval = [%f,%f]\n", percolationStats.confidenceLo(), percolationStats.confidenceHi());
	}

}
