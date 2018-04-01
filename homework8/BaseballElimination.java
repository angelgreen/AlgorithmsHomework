import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class BaseballElimination {

	private int teamNum;
	private int[] w;
	private int[] l;
	private int[] r;
	private int[][] g;

	private final ST<String,Integer> map;

	private final String[] revertMap;

	//inner class
	private static class Node {
		private final FlowNetwork flowNetwork;
		private final ST<String,Integer> teamMap;
		private final int s;
		private final int t;

		public Node(FlowNetwork flowNetwork,
					   	ST<String,Integer> teamMap,
						int s, 
						int t) {
			this.flowNetwork = flowNetwork;
			this.teamMap = teamMap;
			this.s = s;
			this.t = t;
		}

		public FlowNetwork getFlowNetwork() { return this.flowNetwork;}

		public ST<String,Integer> getTeamMap() { return this.teamMap;}

		public int getSource() { return s;}

		public int getSink() { return t; }
	}

	public BaseballElimination(String filename) {

		In in = new In(filename);

		this.map = new ST<String,Integer>();

		this.teamNum = in.readInt();

		this.revertMap = new String[teamNum]; //revertMap

		this.w = new int[teamNum];
		this.l = new int[teamNum];
		this.r = new int[teamNum];
		this.g = new int[teamNum][teamNum];

		int num = 0;

		while(num < teamNum) {

			String team = in.readString();

			map.put(team, num); 

			revertMap[num] = team; //the revert map

			int winNum = in.readInt(); //win
			int lossNum = in.readInt(); //loss
			int leftNum = in.readInt(); //left

			this.w[num] = winNum;
			this.l[num] = lossNum;
			this.r[num] = leftNum;

			for(int i = 0; i < teamNum; i++) {
				g[num][i] = in.readInt();
			}
			//add num
			num++;
		}
	}	

	public int numberOfTeams() {
		return this.teamNum;
	}

	public Iterable<String> teams() {
		return this.map.keys();	
	}

	public int wins(String team) {
		if(null == team) throw new IllegalArgumentException("");
		if(!map.contains(team)) throw new IllegalArgumentException("");

		int index = map.get(team);

		return w[index];
	}

	public int losses(String team) {
		if(null == team) throw new IllegalArgumentException("");
		if(!map.contains(team)) throw new IllegalArgumentException("");

		int index = map.get(team);	

		return l[index];
	}

	public int remaining(String team) {
		if(null == team) throw new IllegalArgumentException("");
		if(!map.contains(team)) throw new IllegalArgumentException("");
		int index = map.get(team);

		return r[index];	
	}	

	public int against(String team1, String team2) {
		if(null == team1 || null == team2) throw new IllegalArgumentException("");
		if(!map.contains(team1)) throw new IllegalArgumentException("");
		if(!map.contains(team2)) throw new IllegalArgumentException("");
		int index1 = map.get(team1);
		int index2 = map.get(team2);

		return g[index1][index2];	
	}

	public boolean isEliminated(String team) {
		if(null == team) throw new IllegalArgumentException(""); //
		if(!map.contains(team)) throw new IllegalArgumentException(""); 

		int x = map.get(team); 

		//trivial eliminate ?
		boolean eliminated = false;

		double xWins = w[x] + r[x];

		for(int i = 0; i < teamNum; i++) {
			if( i != x) {
				if(xWins < w[i]) {
					eliminated = true;	
					break;
				}	
			}
		}

		if(eliminated) return true; //can trivial eliminate


		Node node = buildNode(team);

		eliminated = false;

		FlowNetwork flowNetwork = node.getFlowNetwork();

		int s = node.getSource(); //get the source vertex
		int t = node.getSink(); //get the sink vertex

		FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork,s, t); 

		//If all edges in the maxflow that are pointing from s are full ,  no team wins more games than x 
		//If some edges pointing from s are not full, then there is no scenario in which team x can win the division
		for(FlowEdge flowEdge: flowNetwork.adj(s)) {
			double residualCapacity = flowEdge.residualCapacityTo(flowEdge.other(s));
			if(residualCapacity > 0) { //not full
				eliminated = true; 
				break; 
			}
		}

		return eliminated;
	}


	//first no cache it ?
	private Node buildNode(String team) {

		ST<String,Integer> gameMap = new ST<String,Integer>();
		ST<String,Integer> teamMap = new ST<String,Integer>();

		int x = map.get(team);

		int m = 1;
		for(int i = 0; i < teamNum; i++) {
			if( i != x) {	
		  		for(int j = i + 1; j < teamNum; j++) {
					if(j != x) {	
						gameMap.put(i+"-"+j , m++);
					}
		  		}
			}
		}

		for(int i = 0; i < teamNum; i++) {
			if(i != x) {
				teamMap.put(String.valueOf(i), m++);
			}
		}

		int s = 0;
		int t = m;

		FlowNetwork flowNetwork = new FlowNetwork(m + 1);
		//parse gameMap
		for(String key: gameMap.keys()) {
			String[] vals = key.split("-");
			int i = Integer.parseInt(vals[0]);
			int j = Integer.parseInt(vals[1]);

			double capacity = g[i][j]; //the capacity 

			int v = gameMap.get(key);

			flowNetwork.addEdge(new FlowEdge(s, v, capacity));	

			int v1 = teamMap.get(vals[0]); //i
			int v2 = teamMap.get(vals[1]); //j

			flowNetwork.addEdge(new FlowEdge(v, v1, Double.POSITIVE_INFINITY));
			flowNetwork.addEdge(new FlowEdge(v, v2, Double.POSITIVE_INFINITY));
		}
		//parse teamMap
		for(String key : teamMap.keys()) {
			int v = teamMap.get(key); //the vertex 
			int i = Integer.parseInt(key);

			flowNetwork.addEdge(new FlowEdge(v, t, w[x] + r[x] - w[i]));
		}

		return new Node(flowNetwork, teamMap, s, t);
	}



	public Iterable<String> certificateOfElimination(String team) {
		if(null == team) throw new IllegalArgumentException("");
		if(!map.contains(team)) throw new IllegalArgumentException("");

		//trivial elimination 
		int x = map.get(team);

		boolean eliminated = false;

		Queue<String> queue1 = new Queue<String>(); //the queue

		double xWins = w[x] + r[x];

		for(int i = 0; i < teamNum; i++) {
			if( i != x) {
				if(xWins < w[i]) {
					eliminated = true;	
					queue1.enqueue(revertMap[i]);
					break;
				}	
			}
		}

		if(eliminated) {
			return queue1;
		}

		Node node = buildNode(team);

		FlowNetwork flowNetwork = node.getFlowNetwork();
		ST<String,Integer> teamMap = node.getTeamMap();

		int s = node.getSource();
		int t = node.getSink();

		//run fordFulkerson algrothim 
		FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, s, t);
		//get the flowNetwork 	
		//
		eliminated = false;

		//If all edges in the maxflow that are pointing from s are full ,  no team wins more games than x 
		//If some edges pointing from s are not full, then there is no scenario in which team x can win the division
		for(FlowEdge flowEdge: flowNetwork.adj(s)) {
			double residualCapacity = flowEdge.residualCapacityTo(flowEdge.other(s));
			if(residualCapacity > 0) { //not full
				eliminated = true; 
				break; 
			}
		}

		if(eliminated) {
		  Queue<String> queue = new Queue<String>();
		  //find the cut 
		  for(String key: teamMap.keys()) {
			int v = teamMap.get(key);
			int i = Integer.parseInt(key);
			//inCut ?
			if(fordFulkerson.inCut(v)) {
				//queue.enqueue(map.get(i)); //the the team name 	
				queue.enqueue(revertMap[i]);
			}
		  }
		 return queue;
		}
		return null;
	}

	public static void main(String[] args) {
		String filename = args[0];
		BaseballElimination division = new BaseballElimination(filename);

		for (String team : division.teams()) {
		    if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
				    StdOut.print(t + " ");
				}
				StdOut.println("}");
			 }else {
			   StdOut.println(team + " is not eliminated");
			 }
		}
	}
}
