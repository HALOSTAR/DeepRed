package species;
import java.util.ArrayList;
import java.util.HashSet;
import game.Board;
import game.Moves;
import game.Move;
import game.SuperBoard;
import util.util;
/*
 * @Author Lisa
 * Species of neural nets -- different species cannot reproduce
 */
public abstract class Species {
	public int [] hiddenLayers;
	public int searchDepth;
	public int numHiddenLayers;
	public int outputSize;
	public int inputSize;
	/*
	 * evaluate board at end of game if loser = loser
	 */
	public static double getEndGameScore(game.SuperBoard b, int loser){
				//get all of losers disntances from goal
				double sum = 0;
				for(int loc: b.getPlayerLocs(loser)){
					int distFromGoal = b.getDistFromGoal(loc,loser);//b.getDistancesFromGoal().get(loc)[loser-1];
					//sum squares distances from goal???//TODO?
					sum += distFromGoal * distFromGoal;
				}
				return sum; 
	}
	/*
	* get neural net inputs
	*/
	public abstract double[] getInputs(SuperBoard b, int myID, int opID);
	/*
	 * get all moves to be considered
	 */
	public ArrayList<Move> genAllMoves(Board board) {	
		//DEFAULT -- override this
		
		return Moves.getMoves(board, false, true, false, false);//TODO: Check that have marbles left before saying extend w/ marble
	}
	/*
	 * inputs to neural net based on distances to goal
	 */
	public double[] distanceBasedInputs(Board b, int myPlayer){
		//one possible implementation of getInputs
		inputSize=20;
		double[] in = new double[20];
		HashSet<Integer> glm  = b.getGoalLocs()[myPlayer-1]; 
		HashSet<Integer> glo  = b.getGoalLocs()[2-(myPlayer) ]; 
		int i = 0;
		for(int loc : glm){
			in[i]=b.getDistancesFromGoal().get(loc)[myPlayer];
			i++;
		}
		for(int loc : glo){
			in[i]=b.getDistancesFromGoal().get(loc)[2-myPlayer];
			i++;
		}
		return in;
	}
	/*
	 * inputs to neural net based on current locations
	 */
	public double[] positionInputs(SuperBoard b) {
				double[] r = new double[this.inputSize];
				int myID = b.getTurn();
				int opponentID = 3-b.getTurn();
				util.fill(r, b.getPlayerLocs(myID), 0, b);
				util.fill(r, b.getPlayerLocs(opponentID),19,b);
				util.fill(r, b.getGoalLocs(myID), 39, b);
				util.fill(r, b.getGoalLocs(opponentID), 59,b);
				return r;
	}
	public static double getNormalizedScore(SuperBoard b, int myID) {
		double ifLose = getEndGameScore(b,myID);
		double ifWin = getEndGameScore(b,3-myID);
		double norm = (ifWin - ifLose)/ (ifLose + ifWin);
		return norm;
	}

}
