import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

	private final WordNet wordnet;

	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}	

	public String outcast(String[] nouns) {
		if(null == nouns || nouns.length == 0) throw new IllegalArgumentException("");
		
		int bestLength = 0;
		int k = 0;		
		String ai = nouns[0];
		for(int j = 0; j < nouns.length; j++) {
			bestLength += wordnet.distance(ai, nouns[j]);	
		}

		for(int i = 1; i < nouns.length; i++) {
			ai = nouns[i];	
			int d = 0;
			for(int j = 0; j < nouns.length; j++) {
				d += wordnet.distance(ai,nouns[j]);
			}	
			//find the max one
			if(d > bestLength) {
				bestLength = d;
				k = i;
			}
		}
		return nouns[k];
	}

	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for(int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] +":" + outcast.outcast(nouns));
		}	
	}
}
