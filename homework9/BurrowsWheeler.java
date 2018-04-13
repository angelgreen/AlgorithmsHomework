import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.Arrays;

public class BurrowsWheeler {

	public static void transform() {

		String str = BinaryStdIn.readString(); //read the string	

		CircularSuffixArray circularSuffixArray = new CircularSuffixArray(str);

		//build the t
		int len = str.length();
		char[] t = new char[len]; //the len 
		int first = 0;
		for(int i = 0; i < len; i++) {
			int j = circularSuffixArray.index(i); //mapping to the circularSuffix
			//System.out.println("j : " + j);
			if(j == 0 ) { first = i;} //find the first
			//the start of circularSuffix
			//the prev char
			t[i] = str.charAt((j - 1 + len) % len);
		}
		//out just dump it 
		BinaryStdOut.write(first);
		for(int i = 0; i < len; i++) {
			BinaryStdOut.write(t[i]);
		}
		BinaryStdIn.close(); //close it 
		BinaryStdOut.close(); //close it 
	}

	public static void inverseTransform() {
		int first = BinaryStdIn.readInt(); //read the first 
		String str = BinaryStdIn.readString(); //read the string 		

		char[] startChar = new char[str.length()];
		char[] endChar = new char[str.length()];

		for(int i = 0; i < str.length(); i++) {
			startChar[i] = str.charAt(i); //copy it	
			endChar[i] = str.charAt(i); //copy it 
		}

		int[] next = new int[str.length()]; //the next index array

		char[] aux = new char[str.length()];
		int R = 256;
		// compute frequency count
		int[] count = new int[R + 1];
		for(int i = 0; i < startChar.length; i++) {
			count[startChar[i] + 1]++;
		}
		//compute cumulates
		for(int r = 0; r < R; r++) {
			count[r+1] += count[r];
		}
		//move data 
		for(int i = 0; i < startChar.length; i++) {
			//the sorted array index j
			//the origin array inde i
			int j = count[startChar[i]]++;
			next[j] = i;
			aux[j] = startChar[i];
		}

		//copy back
		for(int i = 0; i < startChar.length; i++) {
			startChar[i] = aux[i]; 
		}

		char[] originChar = new char[str.length()];
		int  i = 0;
		while(i < str.length()) {
			char c1 = startChar[first]; 
			originChar[i] = c1; //record it ..
			first = next[first]; //goto next first 
			i++;
		}
		//start dump it ..
		for(int j = 0; j < str.length(); j++)
			BinaryStdOut.write(originChar[j]);

		BinaryStdOut.close();	
		BinaryStdIn.close();
	}

	public static void main(String[] args) {
		String order = args[0];
		if("-".equals(order)) transform(); //transform it 
		if("+".equals(order)) inverseTransform(); //inverseTransform it 
	}
}
