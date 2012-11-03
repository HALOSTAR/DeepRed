package util;
import java.util.*;
import game.Board;
import game.SuperBoard;

public class util {
	public static void fill(double[] r,HashSet<Integer> t, int i, SuperBoard b){
		try {
			for(Integer k  : t){
				//System.out.println("HS " +t.size());
				int[] m = Board.hashToLoc(k);
				//System.out.println(m.length);
				for(int mm : m){

					r[i] = mm;

					i++;
				}
				//i =0;
			}
			
			
		}
	catch (ArrayIndexOutOfBoundsException e) {
		//System.out.println("Actual "+ r.length);
		//System.out.println("Actual m "+ m.length);
		//System.out.println("i ? "+ i);
		
	}


	}
}
