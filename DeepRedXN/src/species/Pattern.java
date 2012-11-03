package species;

import game.Board;
import game.SuperBoard;

import java.util.ArrayList;
/*
 * @author Lisa
 * Neural Net higher level inputs
 */

/*
 * Example Pattern: 
 * int[] g = new int[][]{{opMarble, myMarble, anyMarble},{empty},{myMarble, validLoc, dontCare, myMarble}};
 * Pattern p = new Pattern (g);
 */

public class Pattern {
	private int[][] pattern;
	public static final int dontCare = 0;
	public static final int empty = 1;
	public static final int myMarble = 2;
	public static final int opMarble = 3;
	public static final int anyMarble = 4;
	public static final int invalidLoc = 5;
	public static final int validLoc = 6;
	public static final int specialMarble = 7;
	
	private ArrayList<int[]> possibleStartLocs;
	public Pattern(int[][] pattern){
		this.pattern = pattern;
	}
	
	public ArrayList<Integer> findPattern(Board b, int myID, int opID){
		ArrayList<Integer> patterns = new ArrayList<Integer>();
		if(possibleStartLocs==null) getPossibleStartLocs(b);
		for(int[] start : possibleStartLocs){
			Boolean isPattern = patternAtLoc(b,start[0],start[1], myID, opID);
			if(isPattern) patterns.add(b.locToHash(start[0],start[1]));
		}
		return patterns;
	}
	public ArrayList<int[]> getPossibleStartLocs(){
		return possibleStartLocs;
	}
	public void getPossibleStartLocs(Board b) { 
		//call at start of game
		int cols = b.getCols();int rows = b.getRows();
		possibleStartLocs = new ArrayList<int[]>();
		for(int sr = 0; sr < rows; sr++){
			for(int sc = 0; sc < cols; sc++){
				if(possibleStartLoc(b, sr,sc))possibleStartLocs.add(new int[]{sr,sc});
			}
		}
		
	}
	private boolean possibleStartLoc(Board b, int sr, int sc) {
		//only checks valid locations + special marbles (should be start of game, no additional special marbles placed)
		for(int pr = 0; pr < this.pattern.length ; pr++){
			int[] prow = pattern[pr];
			for(int pc = 0; pc < prow.length; pc++){
				int fromPattern = this.pattern[pr][pc];
				int lr = pr+sr;
				int lc = pc+sc;
				int atLoc = b.getAtLoc(lr, lc);
				if(atLoc == Board.getInvalidlocation()){
					if(fromPattern != dontCare && fromPattern != invalidLoc)return false;
				}else if(atLoc == Board.getMarble()){
					if(fromPattern != validLoc && fromPattern != dontCare && fromPattern != anyMarble && fromPattern != specialMarble){
						return false;
					}
				}
			}
		}
		return true;
		
	}
	public int fill(double[] in, int i, int myID, int opID, SuperBoard b) {
		for(int[] start : this.possibleStartLocs){
			boolean x = this.patternAtLoc(b, start[0], start[1], myID, opID);
			if(x)in[i]= 1;
			i++;
		}
		return i;
	}
	private boolean patternAtLoc(SuperBoard b, int sr, int sc, int myID, int opID) { 
		for(int pr = 0; pr < this.pattern.length ; pr++){
			int[] prow = pattern[pr];
			for(int pc = 0; pc < prow.length; pc++){
				int fromPattern = this.pattern[pr][pc];
				if(fromPattern != dontCare){
					int lr = pr+sr;
					int lc = pc+sc;
					int atLoc = b.getAtLoc(lr, lc);
					if(atLoc == Board.getInvalidlocation()){
						if(fromPattern != invalidLoc)return false;
					}else if(fromPattern == validLoc){} // at this point guaranteed to be at valid loc
					else if(atLoc == Board.getEmpty()){
						if(fromPattern != empty){
							return false;
						}
					}else if(fromPattern == anyMarble){// guaranteed to have a marble at this point
					}else if(atLoc == Board.getMarble()){
						if(fromPattern != specialMarble) return false;
					}else if(atLoc == myID){
						if(fromPattern != myMarble) return false;
					}else if(atLoc == opID){
						if(fromPattern != opMarble)return false;
					}else{
						System.out.println("FAIL!!! " + atLoc + " " + fromPattern);
						int[] r = new int[0];int i = r[4];
					}
				}
			}
		}
		return true;
	}
	
}
