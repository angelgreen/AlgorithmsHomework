import java.util.Arrays;

public class CircularSuffixArray {
	
	private final int length;	
	private final String origin;
	private final int[] index;

	private final String[] circularSuffixArray;

	private class Node implements Comparable<Node> {

		private final int startSuffixIndex; //the start suffix index 

		public Node(int startSuffixIndex) {
			this.startSuffixIndex = startSuffixIndex;
		}	

		public int compareTo(Node that) { 
			int startSuffixIndex0  = this.startSuffixIndex;	
			int startSuffixIndex1 = that.startSuffixIndex;

			char c0 = origin.charAt(startSuffixIndex0);
			char c1 = origin.charAt(startSuffixIndex1);

			int count = 0;
			//find the same goto next one ...
			//prefix compare it ..
			while(c0 == c1 && count < length - 1) {
				//update it 
				startSuffixIndex0 = (startSuffixIndex0 + 1) % length;	
				//update it
				startSuffixIndex1 = (startSuffixIndex1 + 1) % length;
				c0 = origin.charAt(startSuffixIndex0);
				c1 = origin.charAt(startSuffixIndex1);
				count++;
			}

			if(c0 > c1) return 1;

			else if(c0 < c1) return -1;

			else return 0;
		}

		@Override
		public String toString() {
			return String.valueOf(startSuffixIndex);
		}
	}

	private final Node[] nodeArray;

	public CircularSuffixArray(String s) {
		if(null == s) throw new IllegalArgumentException("");
		this.length = s.length();
		this.origin = s;
		this.circularSuffixArray = new String[this.length];
		this.index = new int[this.length];
		this.nodeArray = new Node[this.length];
		//build it 
		buildCircularSuffixArray();
	}	


	private void buildCircularSuffixArray() {

		for(int i = 0; i < this.length; i++) {
			nodeArray[i] = new Node(i);	
		}	
		//sort it 
		Arrays.sort(nodeArray);
	}

	public int length() {
		return length;
	}

	public int index(int i) {

		if( i < 0 || i >= length) throw new IllegalArgumentException("");
		return nodeArray[i].startSuffixIndex;
	}

	public static void main(String[] args) {
		String str = "AABBAAAAAA";
		CircularSuffixArray circularSuffixArray = new CircularSuffixArray(str);
		System.out.println(circularSuffixArray.index(0));
	}
}
