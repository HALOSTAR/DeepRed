package players;

import game.Board;
import game.Move;

import java.util.ArrayList;

import species.Species;


public abstract class EndGameScoreMaxPlayer extends Player{
	/*public EndGameScoreMaxPlayer(){
		EndScoreMax myAB = new EndScoreMax();
		
	}*/
	
	protected EndScoreMax ab;
	protected int depth = 3;//Default
	public Move request_move(int[] args) {
		return ab.alphaBeta(depth, board, board.getTurn());
	}
  public static abstract class EndScoreMax extends AlphaBetaSelect{

		public double evaluateBoard(Board b, int myID) {
			return Species.getNormalizedScore(b, myID);
		}
		public abstract ArrayList<Move> genMoves(Board board);/* {
			return Moves.getMoves(board, true, true, true, false);
		}*/
		
	}
	
	

}
