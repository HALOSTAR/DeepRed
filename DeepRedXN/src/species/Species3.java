package species;

import java.util.HashSet;

import game.Board;
import game.SuperBoard;
/*
 * @author Lisa
 * uses board histogram as inputs
 */
public class Species3 extends Species{
	public Species3(){
	    hiddenLayers = new int[]{300};
		numHiddenLayers = 1;
		outputSize = 1;
		searchDepth = 1;
		inputSize = (17+25)*4 + 3;
	}
	
	/*
	 * histogram inputs + end game score + special marbles left
	 */
	 public double[] getInputs(SuperBoard b, int x, int y) {
		
		 int myid = b.getTurn();
		 int opid = 3-myid;
	
		 int rows = b.getRows();
		 int cols = b.getCols();
	
		 double in[] = new double[inputSize];
		 in[0]=Species.getNormalizedScore(b, myid);
		 in[1]=b.getMarbles()[myid-1];
		 in[2]=b.getMarbles()[opid-1];
		 int i = 3;
		 
		 int[][][] counts= new int[rows][cols][4];
		 
		 for(int r = 0; r < rows; r++){ 
			 for(int c = 0; c < cols; c++){
				 int atLoc = b.getAtLoc(r, c);
				 if(atLoc == Board.getInvalidlocation()){
				 }else if(atLoc == Board.getEmpty()){
					 counts[r][c][0]++;
				 }else if(atLoc == myid){
					 counts[r][c][1]++;
				 }else if(atLoc == opid){
					 counts[r][c][2]++;
				 }else counts[r][c][3]++;
			 }
		 }
		 
		 int[][] rowSums = new int[rows][4];
		 int[][] colSums = new int[cols][4];
		 
		 for(int c = 0; c < cols; c++){
			 for(int r = 0; r < rows; r++){
				 for(int j = 0; j < 4; j++){
					 rowSums[r][j] += counts[r][c][j]; 
					 colSums[c][j] += counts[r][c][j];
				 }
			 }
		 }
		
		 for(int j = 0; j < 4; j++){
			 for(int r = 0; r < rows; r++){
				 in[i] = (0.0+rowSums[r][j]) / rows;
				 i++;
			 }
			 for(int c = 0; c < cols; c++){
				 in[i] = (0.0+colSums[c][j]) / cols;
			 }
		 }
		 return in;
	}
}
