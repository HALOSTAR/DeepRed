package game;

import java.util.ArrayList;
import java.util.Collection;
/*
 * @Author Lisa
 * Result of game
 */
public class Result {
	private static Result[] results;
	public int winner;
	public int loser;
	
	private ArrayList<LogBoard> history;
	public static void init(){
		results = new Result[2];
		results[0]=new Result(1);
		results[1]=new Result(2);
	}
	public void setHistory(ArrayList<LogBoard> hist){
		history = hist;
	}
	public static Result getResult(int winner){
		return results[winner-1];
	}
	public boolean isUnfinished(){
		return winner == -1;
	}
	Result(){
		//unfinishedResult
		this.winner = -1;
	}
	Result(int winner) {
		
		this.winner = winner;
		this.loser = 3-winner;
	}
	public Collection<? extends LogBoard> getHistory() {
		return history;
	}

}
