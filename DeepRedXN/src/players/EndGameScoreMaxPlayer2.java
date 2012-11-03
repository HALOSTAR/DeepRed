package players;

import game.Board;
import game.Move;
import game.Moves;

import java.util.ArrayList;

import players.EndGameScoreMaxPlayer.EndScoreMax;
/*
 * @author Lisa 
 * Like EGSMP1 but does not generate moves that use marble 
 * Also searches to greater depth due to decreased branching factor
 */
public class EndGameScoreMaxPlayer2 extends EndGameScoreMaxPlayer{
	public EndGameScoreMaxPlayer2(){
		depth = 5;
		ab = new myAB();
	}
	public static class myAB extends EndGameScoreMaxPlayer.EndScoreMax{
		public ArrayList<Move> genMoves(Board board) {
			return Moves.getMoves(board, true, true, true, false);
		}
		
	}

}
