package game;

import java.util.HashSet;

public abstract class SuperBoard {
	public abstract int getRows();
	public abstract int getCols();
	public abstract HashSet<Integer> getPlayerLocs(int player);
	public abstract int getDistFromGoal(int h, int p);
	public abstract int getAtLoc(int r, int c);
	public abstract int getTurn();
	public abstract HashSet<Integer> getGoalLocs(int id);
	public abstract HashSet<Integer> getValidLocs();
	public abstract int[] getMarbles();
	public int getAtLoc(int h) {
		int[] loc =  Board.hashToLoc(h);
		return this.getAtLoc(loc[0],loc[1]);
	}
}
