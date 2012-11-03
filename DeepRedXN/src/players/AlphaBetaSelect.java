package players;

import game.Board;
import game.Move;

import java.util.ArrayList;
/*
 * @author Lisa
 * Implements Min max algorithm with alpha beta pruning
 * For use with our board
 */
public abstract class AlphaBetaSelect {
	public abstract ArrayList<game.Move> genMoves(game.Board board);
	public abstract double evaluateBoard(game.Board b, int myID);
	public game.Move alphaBeta(int depth, Board board,int myID){
		
		ArrayList<game.Move> moves = genMoves(board);
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		game.Move best=null;
		double bestVal=0;
		for(game.Move m : moves){
			board.makeMove(m);
			double r =sub(depth-1, alpha, beta, myID, board);
			if(r>alpha)alpha=r;
			if(best==null || r>bestVal){
				best=m;bestVal=r;
			}
			board.backwardMove(m);
		}
		return best;	
	}
	private double sub(int depth, double alpha, double beta, int myID, game.Board b) {
		if(depth==0)return evaluateBoard(b, myID);
		boolean max = b.getTurn()==myID;
		ArrayList<game.Move> moves = genMoves(b);
		if(max){
			while(!moves.isEmpty() && alpha<beta){
				game.Move m = moves.remove(0);
				b.makeMove(m);
				double r = sub(depth-1, alpha, beta, myID, b);
				if(r>alpha)alpha=r;
				b.backwardMove(m);
			}
			return alpha;
		}else{
			while(!moves.isEmpty() && alpha<beta){
				game.Move m = moves.remove(0);
				b.makeMove(m);
				double r = sub(depth-1, alpha, beta, myID, b);
				if(r<beta)beta=r;
				b.backwardMove(m);
			}
		}return beta;
	}

	
}
