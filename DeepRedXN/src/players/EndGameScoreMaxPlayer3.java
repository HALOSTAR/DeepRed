package players;

import game.Board;
import game.Move;
import game.Moves;

import java.util.ArrayList;

import players.EndGameScoreMaxPlayer.EndScoreMax;
/*
 * @author Lisa 
 * Moves are very unconstrained in this version 
 */
public class EndGameScoreMaxPlayer3 extends EndGameScoreMaxPlayer{
	public EndGameScoreMaxPlayer3(){
		depth = 3;
		ab = new myAB();
	}
	public static class myAB extends EndGameScoreMaxPlayer.EndScoreMax{
		public ArrayList<Move> genMoves(Board board) {
			return Moves.getMoves(board, false, false, true, false);
		}
		
	}

}
