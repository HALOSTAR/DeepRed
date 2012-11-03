package species;

import java.util.HashSet;

import game.Board;
import game.SuperBoard;
/*
 * @author Lisa
 * full board representation
 */
public class Species2 extends Species{
	public Species2(){
	    hiddenLayers = new int[]{672};
		numHiddenLayers = 1;
		outputSize = 1;
		searchDepth = 3;
		inputSize = 121*3+2;
	}
	 public double[] getInputs(SuperBoard b, int x, int y) {
		 HashSet<Integer> validLocs = b.getValidLocs();
		 double[] in = new double[validLocs.size() * 3 + 2];
		 int myID = b.getTurn();
		 int opID = 3-myID;
		 in[0]=b.getMarbles()[myID-1];
		 in[1]=b.getMarbles()[2-myID];
		 int i = 2;
		 for(int valid : validLocs){
			 int atLoc = b.getAtLoc(valid); 
			 if(atLoc == Board.getEmpty())in[i]=1;
			 else if(atLoc == myID)in[i+1]=1;
			 else if(atLoc == opID)in[i+2]=1;
			 //if special marble at loc do nothing?
				 i+=3;
		 }
		 return in;
	}
	
}
