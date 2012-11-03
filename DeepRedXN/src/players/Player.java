package players;

import game.Board;
import game.Move;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Player extends Thread implements Comparable<Player>{
	public int id;
	public Board board;
	private double score;
	public Move comm(int i, int[] args){
		if (i == 0) {game_init(args); return null;}
		else if (i == 1) return request_move(args);
		else if (i == 2){opponent_moved(args); return null;}
		else if (i == 3) {error(args); return null;}
		else if (i == 4) {end_game(args); return null;}
		else new Error("INVALID COMM");
		return null;}
	public void game_init(int[] args){}
	public abstract Move request_move(int[] args);
	public void opponent_moved(int[] args){}
	public void error(int[] args){}
	public void end_game(int[] args){}
	public void resetScore() {
		setScore(0);
	}
	public void addScore(double s){
		setScore(getScore() + s);
	}
	
	public int compareTo(Player p2){
		if(this.getScore() > p2.getScore()){
			return -1;
		}
		else{
			return 1;
		}
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getScore() {
		return score;
	}
}
