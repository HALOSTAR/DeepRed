package players;

import game.Board;
import game.Move;
import game.Moves;

import java.util.ArrayList;

/*
 * @author Lisa
 * Randomly moves forward
 */
public class Stupid extends Player{
	public Stupid(int i) {
		this.id = i;
	}
	boolean pieceAt(int[] loc){
		return this.board.getAtLoc(loc) <2;
	}
	public Move request_move(int[] args) {
		System.out.println("REQ MOVE");
		ArrayList<Move> all =Moves.getMoves(board, false, true, false, false);
		System.out.println("NUM MOVES: " + all.size());
		int g =(int)(Math.random() * all.size());
		Move m = all.get(g);
		return m;
	}

	

}
