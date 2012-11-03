package game;

public class Move {
	
	public int[] start;
	public int[] end;
	public int[] marble;
	public void show(){
		System.out.println("START: " + start[0] + ","+start[1]);
		System.out.println("END: " + end[0] + ","+end[1]);
		if(marble!=null)
			System.out.println("MARBLE: " + marble[0] + ","+marble[1]);
		
	}
	/*
	 * return move with specified properties
	 */
	public static Move getMove(int[] start, int[] end, int[] marbleLoc){
		if(marbleLoc==null){
			Move n = Moves.movesWoutMarble[start[0]][start[1]][end[0]][end[1]];
			if(n==null){
				Move m = new Move(start,end,marbleLoc);
				Moves.movesWoutMarble[start[0]][start[1]][end[0]][end[1]]=m;
				return m;
			}
			else return n;
		}else{
			Move n =Moves.movesWMarble[start[0]][start[1]][end[0]][end[1]]
			              [marbleLoc[0]][marbleLoc[1]];
				if(n==null){
					Move m = new Move(start,end,marbleLoc);
					Moves.movesWMarble[start[0]][start[1]][end[0]][end[1]]
					              [marbleLoc[0]][marbleLoc[1]]=m;
					return m;
				}
				else return n;
			}
	}
	private Move(int[] start, int[] end, int[] marbleLoc){
		this.start=start;this.end=end;this.marble = marbleLoc;//marbleLoc = none if not placed
	}
	public boolean marblePlaced(){
		return !(marble==null);
	}
	public int[] getStartLoc() {
		return start;
	}
	public int[] getEndLoc() {
		return end;
	}
	/*
	 * length = progress toward goal achieved if player "turn" makes this move
	 */
	public int getLength(Board board, int turn) {
		int eDist = board.getDistFromGoal(board.locToHash(end[0], end[1]), turn);
		int sDist = board.getDistFromGoal(board.locToHash(start[0],start[1]), turn);
		return sDist - eDist;
		
	}
	
	public String toString(){
		return start[0] + " " + start[1] + " " + end[0] + " " + end[1] + " " + marble[0] + " " + marble[1];
	}
}
