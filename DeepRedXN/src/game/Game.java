
package game;

import java.util.ArrayList;
import java.util.HashSet;

import players.Player;

import species.Species;

public class Game{
	/*
	 * @author Lisa
	 * Play game on specified board w/ specified players and maximum number of moves
	 * return game result
	 */
	 //int moveDelay = 1000;
public static Result playGame(Board board, Player p1, Player p2, int maxMoves){
	double decay = .999;//TODO
	Player[] players = new Player[]{p1,p2};
	p1.board = board;
	p2.board = board;
	Board initBoard = new Board(board);
	p1.id = 1;
	p2.id = 2;
	ArrayList<LogBoard> history=new ArrayList<LogBoard>();
	
	while(maxMoves>0){
		maxMoves--;
		int turn = board.getTurn();
		Move move = players[turn-1].request_move(null);//players[board.getTurn()-1].comm(1, new int[]{board.getTimeRemaining(board.getTurn())});
		Result r  = board.makeMove(move);
		history.add(0, new LogBoard(board,turn, initBoard));
		if(r != null)return processHistory(history, r, decay);
	}
	Result r= new Result();//unfinished result
	return processHistory(history, r, decay);
}

private static Result processHistory(ArrayList<LogBoard> history, Result r, double decay) {
	double val;
	if(!r.isUnfinished()){
		val = 1; // val is with respect to player 1. 
		if(r.winner == 2) val = -1;
	}
	else{
		LogBoard b = history.get(0);
		double myDistFromGoal = Species.getEndGameScore(b, 1);
		double otherDistFromGoal = Species.getEndGameScore(b, 2);
		double gameScore = (otherDistFromGoal - myDistFromGoal)/(myDistFromGoal + otherDistFromGoal);
		if(gameScore > 0){
			val = 1;
		}
		else{
			val = -1;
		}
	}
	
	for(int i = 0 ; i < history.size(); i++){

		if(Math.abs(val) > 0.01){
			LogBoard l = history.get(i);
			l.setValue(val);
			val*=decay;
		}
		else{
			while(history.size() > i){
				history.remove(i);
			}
		}
	}
	r.setHistory(history);
	return r;
}

//Created because Lisa's system of Results and LogBoards is too complicated (ie,untested and unreliable)
//by Jon
public static ArrayList<LogBoardSimple> logGame(Board board, Player p1, Player p2, int maxMoves){
	double decay = .999;//TODO
	Player[] players = new Player[]{p1,p2};
	p1.board = board;
	p2.board = board;
	Board initBoard = new Board(board);
	p1.id = 1;
	p2.id = 2;
	ArrayList<Board> history=new ArrayList<Board>();
	
	while(maxMoves>0){
		maxMoves--;
		int turn = board.getTurn();
		Move move = players[turn-1].request_move(null);//players[board.getTurn()-1].comm(1, new int[]{board.getTimeRemaining(board.getTurn())});
		Result r  = board.makeMove(move);
		history.add(0, board);
		if(r != null)return processHistoryLog(history, decay);
	}
	Result r= new Result();//unfinished result
	return processHistoryLog(history, decay);
}


	private static ArrayList<LogBoardSimple> processHistoryLog(ArrayList<Board> history,
		double decay) {
			Board b = history.get(0);
			double val = Species.getNormalizedScore(b, 1);
			double val2 = Species.getNormalizedScore(b, 2);
			
			ArrayList<LogBoardSimple> mems = new ArrayList<LogBoardSimple>();
			
			for(int i = 0 ; i < history.size(); i++){
				if(Math.abs(val) > 0.01){
					Board l = history.get(i);
					mems.add(new LogBoardSimple(b,1,val));
					mems.add(new LogBoardSimple(b,1,val2));
					val*=decay;
					val2*=decay;
				}
			}
			return mems;
		}

	//by Jon, stores game histories as a series of heuristic scores paired with games.
	public static ArrayList<LogBoardSimple> playGameAltStorage(Board board, Player p1,
			Player p2, int maxMoves) {
		Player[] players = new Player[]{p1,p2};
		p1.board = board;
		p2.board = board;
		Board initBoard = new Board(board);
		p1.id = 1;
		p2.id = 2;
		ArrayList<LogBoardSimple> history=new ArrayList<LogBoardSimple>();
		
		while(maxMoves>0){
			maxMoves--;
			int turn = board.getTurn();
			Move move = players[turn-1].request_move(null);//players[board.getTurn()-1].comm(1, new int[]{board.getTimeRemaining(board.getTurn())});
			Result r  = board.makeMove(move);
			history.add(0, new LogBoardSimple(board,1,Species.getNormalizedScore(board, 1)));
			history.add(0, new LogBoardSimple(board,2,Species.getNormalizedScore(board, 2)));
			if(r != null)return history;
		}
		Result r= new Result();//unfinished result
		return history;
	}

}