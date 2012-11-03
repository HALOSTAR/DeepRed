package players;

import java.util.ArrayList;
import game.Move;
import game.Moves;
/*
 * @author Lisa - always selects the move that makes the most immediate forward progress
 */
public class Greedy extends Player{

	public Greedy(){
		//System.out.println("INIT GREEDY");
	}
	@Override
	public Move request_move(int[] args) {
		//return move that advances maximum distance toward goal
		ArrayList<Move> m = Moves.getMoves(board, true, true, false, false);
		int maxLen = Integer.MIN_VALUE;
		Move best = null;
		for(Move n : m){
			int nLen = n.getLength(board, board.getTurn());
			if(nLen > maxLen){
				maxLen = nLen;
				best = n;
			}
		}
		//System.out.println("GREEDY BEST LEN = " + maxLen);
		return best;	
	}

}
