import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Digraph;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {

	private ST<String,LinkedList<Integer>> st; //string -> index	
	private ST<Integer, String> keys; //index -> string 
	private Digraph graph;
	private SAP sap;

	public WordNet(String synsets,String hypernyms) {
		this.st = new ST<String, LinkedList<Integer>>(); //the set
		this.keys = new ST<Integer, String>();
		//read the synsets files 
		In in = new In(synsets);		
		while(in.hasNextLine()) {
			String[] a = in.readLine().split(",");
			int id = Integer.parseInt(a[0]); //the synset id 	
			String synStr = a[1];

			keys.put(id, synStr);

			String[] synset = synStr.split(" "); //the synset 

			for(int j = 0; j < synset.length; j++) {
				LinkedList<Integer> queue = st.get(synset[j]); //the associate synsets 
				 if(null == queue) { queue = new LinkedList<Integer>();		 
				    st.put(synset[j], queue);
				 }
				 //add it 	
				 queue.add(id);	
			}
		}


		this.graph = new Digraph(keys.size());

		In in1 = new In(hypernyms);
		while(in1.hasNextLine()) {
			String[] a = in1.readLine().split(",");
			int synsetId = Integer.parseInt(a[0]);
			for(int k = 1; k < a.length; k++) {
				graph.addEdge(synsetId, Integer.parseInt(a[k]));
			}
		} 

		//is the graph cycle ?
		DirectedCycle directedCycle = new DirectedCycle(this.graph);
		//NOT DAG
		if(directedCycle.hasCycle()) {
			throw new IllegalArgumentException("");
		}	

		//checked one root DAG
		boolean flag = false;
		for(int v = 0; v < this.graph.V(); v++) {
			if(graph.outdegree(v) == 0 && flag) {
				throw new IllegalArgumentException("");
			//first ?	
			}else if(graph.outdegree(v) == 0 && !flag) {
				flag = true;
			}
		}
		this.sap = new SAP(this.graph);
	}	

	public Iterable<String> nouns() {
		return st.keys();	
	}

	public boolean isNoun(String word) {
		if(null == word) throw new IllegalArgumentException("");	
		return st.contains(word);	
	}

	public int distance(String nounA, String nounB) {
		if(!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("");

		LinkedList<Integer> s = st.get(nounA); //the s iterator
		LinkedList<Integer> t = st.get(nounB); //the t iterator

		return sap.length(s, t);

	}
	
	public String sap(String nounA, String nounB) {
		if(!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("");

		LinkedList<Integer> s = st.get(nounA);
		LinkedList<Integer> t = st.get(nounB);

		int ancestor = sap.ancestor(s, t);

		return keys.get(ancestor);
	}

	public static void main(String[] args) {
		//TODO angelgreen
	}
}
