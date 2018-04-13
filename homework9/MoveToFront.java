
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
public class MoveToFront {

	private static final int R = 256;	

	public MoveToFront() {
	}	

	public static void encode() {

		char[] array = new char[R]; //the r array 	
		//init the table 
		for(int i = 0; i < R; i++) {
			array[i] = (char)i;
		}
		while(!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar(8); //read 8 bytes
			int i;
			for(i = 0; i < array.length; i++) {
				if(array[i] == c) break; //find it ...
			}

			BinaryStdOut.write(i, 8);
			//move front
			for(int j = i; j > 0; j--) {
				array[j] = array[j-1];
			}	
			array[0] = c; 
		}
		BinaryStdOut.close();
		BinaryStdIn.close();
	}

	public static void decode() {
		char[] array = new char[R]; //the r array 
		//init the table
		//
		for(int i = 0; i < R; i++) {
			array[i] = (char)i;
		}
		while(!BinaryStdIn.isEmpty()) {

			int index = BinaryStdIn.readChar(8); //read 8 bytes

			char c = array[index];

			BinaryStdOut.write(c);
			//move front
			for(int j = index; j > 0; j--) {
				array[j] = array[j-1];
			}	
			array[0] = c; 
		}
		BinaryStdOut.close();
		BinaryStdIn.close();
	}

	public static void main(String[] args) {
		if(args == null || args.length < 1) throw new IllegalArgumentException("");
		String order = args[0];
		if("+".equals(order)) decode();
		if("-".equals(order)) encode();	
	}
}
