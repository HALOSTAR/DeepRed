package game;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import neuralnet.NeuralNetwork;
import species.Species;
import util.util;
import java.util.*;
public class Board extends SuperBoard{
	/*
	 * @author Lisa
	 */
	private static int[] initMarbles;
	private int[] marbles;
	private static int[] total_time;
	private int[] time_remaining;
	private int turn =1;
	private static int hashWidth = 100;
	private HashSet<Integer>[] playerLocs;
	private HashSet<Integer> marbleLocs;
	private HashSet<Integer> validLocs;
	private HashSet<Integer>[] goalLocs;	
	private HashSet<Integer> initFilledLocs;
	private HashMap<Integer, int[]> distancesFromGoal;
	private int rows;
	private int cols;
	private static final int invalidLocation= 34534;
	private static final int empty = 23423;
	private static final int marble = 949;
	
	
	/*
	 * CopyBoard
	 */
	public Board(Board board) {//TODO: THis might need fixing (deep clone vs shallow)
		this.initMarbles = board.initMarbles.clone();
		this.marbles=new int[]{board.marbles[0], board.marbles[1]};
		this.playerLocs = new HashSet[2];
		this.playerLocs[0] = new HashSet<Integer>();
		this.playerLocs[1] = new HashSet<Integer>();
		this.marbleLocs = new HashSet<Integer>();
		this.validLocs = board.validLocs;
		this.goalLocs= board.goalLocs;
		this.initFilledLocs = board.initFilledLocs;
		this.distancesFromGoal = (HashMap<Integer, int[]>) board.distancesFromGoal.clone();
		
		for(int i = 0; i < 2; i++){
			for(int x : board.playerLocs[i]){
				playerLocs[i].add(x);
			}
		}

		for(int i : board.marbleLocs){
			this.marbleLocs.add(i);
		}
	}
	
	/*
	 * Checks if player has all marbles in goal locs
	 */
	public boolean checkWin(int player){
		int otherPlayer;
		if(player == 1){
			otherPlayer = 2;
		}
		else{
			otherPlayer = 1;
		}
		boolean oneIn = false;
		for(int goalLoc : getGoalLocs()[player-1]){
			if(getPlayerLocs()[player-1].contains(goalLoc))
				oneIn = true;
			else if (!getPlayerLocs()[otherPlayer-1].contains(goalLoc) && !  (marbleLocs.contains(goalLoc))){
				return false;
			}
		}
		return oneIn;
	}
	
	public int[] getMarblesInHome() {
		int[] t = new int[2];
		for(int i = 0; i < 2;i++)
			for(int g : goalLocs[i]){
				int n = getAtLoc(g);
				if(n == 2-i)t[i]++;
			}
		return t;
	}
	public int[] getMarblesInGoal() {
		int[] t = new int[2];
		for(int i = 0; i < 2;i++)
			for(int g : goalLocs[i]){
				int n = getAtLoc(g);
				if(n == i+1)t[i]++;
			}
		return t;
	}
	
	/*
	 * gets board ready for new game by moving all marbles to their initial locations
	 */
	public void reset() {
		
		getPlayerLocs()[0].clear();
		getPlayerLocs()[1].clear();
		marbleLocs.clear();
		for(int gl : getGoalLocs()[0])getPlayerLocs()[1].add(gl);
		for(int gl : getGoalLocs()[1])getPlayerLocs()[0].add(gl);
		for(int ml : initFilledLocs)marbleLocs.add(ml);
		time_remaining[0]=total_time[0];
		time_remaining[1]=total_time[1];
		setMarbles(new int[2]);
		getMarbles()[0]=initMarbles[0];
		getMarbles()[1]=initMarbles[1];
	}
	/*
	 * reads board from file + inits distances from goal and moves
	 */
	@SuppressWarnings("unchecked")
	public Board(Scanner sc)
	throws NumberFormatException, IOException{
		time_remaining = new int[2];
		total_time = new int[2];
		setPlayerLocs(new HashSet[2]);
		setGoalLocs(new HashSet[2]);
		getPlayerLocs()[0]=new HashSet<Integer>();
		getPlayerLocs()[1]=new HashSet<Integer>();
		validLocs = new HashSet<Integer>();
		getGoalLocs()[0]=new HashSet<Integer>();
		getGoalLocs()[1]=new HashSet<Integer>();
		marbleLocs = new HashSet<Integer>();
		initFilledLocs = new HashSet<Integer>();
		initMarbles = new int[2];
		lastReadSuccess= readBoard(sc);
		if(!lastReadSuccess) return;
		setDistancesFromGoal();
		Moves.initMoves(rows, cols);
		reset();	
	}
	
	
	//By Jon to bypass previous failing scanner.
	public Board(HashSet<Integer> player1Locs, HashSet<Integer> player2Locs,HashSet<Integer> marbleLocs, HashSet<Integer> validLocs, int[] initMarbles){
		time_remaining = new int[2];
		total_time = new int[2];
		setPlayerLocs(new HashSet[2]);
		setGoalLocs(new HashSet[2]);
		getPlayerLocs()[0]=player1Locs;
		getPlayerLocs()[1]=player2Locs;
		this.validLocs = validLocs;
		getGoalLocs()[0]=(HashSet<Integer>) player2Locs.clone();
		getGoalLocs()[1]=(HashSet<Integer>) player1Locs.clone();
		this.marbleLocs = marbleLocs;
		initFilledLocs = new HashSet<Integer>();
		this.initMarbles = initMarbles;
		this.marbles = initMarbles;
		setDistancesFromGoal();
		rows = 17;
		cols = 25;
		Moves.initMoves(rows, cols);
	}
	
	public static boolean lastReadSuccess;
	/*
	 * calculate shortest distance from each location to goal loc for each player
	 */
	private void setDistancesFromGoal() {
		distancesFromGoal=new HashMap<Integer, int[]>();
		for(int i : validLocs){
			int[] k = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
			distancesFromGoal.put(i, k);
		}
		ArrayList<Integer> openList= new ArrayList<Integer>();
		
		//only add farthest goal locs to i
		
		
		//for(int i = 0; i < 2; i++){
			//insert goal locs as 0
		//	for(int g : goalLocs[i]){
			//	distancesFromGoal.get(g)[i]=0;
			//	openList.add(g);
			//	int[] hash = this.hashToLoc(g);
			//	System.out.println("G: " + g + "I: " + i + " HASH: " + hash[0] + " " + hash[1]);
			//}
		int[] g = new int[]{1612, 12}; //TODO: THIS Has been hardcoded for normal initial conditions. Figure out way to change this. 
		for(int i = 0; i < 2; i++){
			distancesFromGoal.get(g[i])[i]=0;
			openList.add(g[i]);
			while(openList.size() > 0){
				int t = openList.remove(0);
				int[] rc = hashToLoc(t);
				int[][] immediate = Moves.getImmediate(rc[0], rc[1]);
				int tDist = distancesFromGoal.get(t)[i];
				for(int[] adj : immediate){
					int adjHash = hash(adj);
					if(isValid(adjHash)){
					int currentDistAtAdj = distancesFromGoal.get(adjHash)[i];
					if(currentDistAtAdj > 1+tDist){
						distancesFromGoal.get(adjHash)[i]=1+tDist;
						openList.add(adjHash);
					}
					//get dist at t
						//if dist > 1+ dist to t
							//replace dist w/ 1+dist to t
			
					// add adj loc to open list
				}
				}
			}
		}
	}
	/*
	 * is h the hash of a location on the board
	 */
	public boolean isValid(int h) {
		return validLocs.contains(h);
	}
	/*
	 * undo move
	 */
	public void backwardMove(Move move) {
		int[] st = move.getStartLoc();
		int[] end = move.getEndLoc();
		int player = getAtLoc(end);
		getPlayerLocs()[player-1].remove(hash(end));
		getPlayerLocs()[player-1].add(hash(st));
		if(move.marble != null)	{
			getMarbles()[player-1]++;
			marbleLocs.remove(this.hash(move.marble));
		}
	}
	/*
	 * player p makes move, result is returned if a player wins, otherwise null is returned
	 */
	public Result makeMove(Move move){
		int[] st = move.getStartLoc();
		int[] end = move.getEndLoc();
    	int player = getAtLoc(st);
    	
		getPlayerLocs()[player-1].remove(hash(st));
		getPlayerLocs()[player-1].add(hash(end));
		if(move.marble != null){
			this.marbleLocs.add(hash(move.marble));
			getMarbles()[player-1]--;
		}
		turn = 3-turn;
		boolean win =  checkWin(3-turn);
		if(win) return Result.getResult(3-turn);
		return null;
		
	}
	/*
	 * returns hashValue of location
	 */
	static int hash(int[] st) {
		return locToHash(st[0], st[1]);
	}
	/*
	 * get value at location st
	 */
	public int getAtLoc(int[] st) {
	return getAtLoc(hash(st));
	}
	/*
	 * read board from file
	 */
	
	public boolean readBoard(Scanner sc) throws IOException {
		rows = 17;
		cols = 25;
		try{
		for(int i=0; i<rows; i++){
			//if(!sc.ready()) 
			//	return false;
			String line = sc.nextLine().replace("#", "");
			//System.out.println("LN: " + line);
			for (int j = 0; j < cols; j++) {
				if(line.charAt(j)== ' ') {
					//invalid location or empty location
				}
				else { //valid location
					int h = hash(new int[]{i,j});
					this.validLocs.add(h);
					
				if(!(line.charAt(j)+"").equals("*")){
					String jt = line.charAt(j) + "";
					
					int n = Integer.parseInt(jt);
					if(n==1)getGoalLocs()[0].add(h);
					else if(n==2)getGoalLocs()[1].add(h);
					else if(n==3) initFilledLocs.add(h);
				}
				}
			}
		}
   //     if(!sc.ready()){
    //        return false;
     //   }
        String line = sc.nextLine().replace("#","");
        String[] part=line.split(" ");
        if(part.length!=5){
        	System.out.println("FAIL " + line);
        	int[] r =new int[0];
        	int t = r[42];
            return false;
        }
        
//        try{
        	total_time = new int[2];
            total_time[0]=Integer.parseInt(part[0]);
            total_time[1]=Integer.parseInt(part[1]);
             
            initMarbles[0]=Integer.parseInt(part[2]);
            initMarbles[1]=Integer.parseInt(part[3]);
           // turn=Integer.parseInt(part[4]);
           // if(turn<1 || turn >2)
            //    return false;
  //      }catch(Exception e){
   //         return false;
    //    }
		
		return true;
		}catch(Exception e){
			return false;
		}
	}
	public void readBoard1(String file) throws NumberFormatException, IOException{
			BufferedReader f = new BufferedReader(new FileReader(file));	
			cols = Integer.parseInt(f.readLine());	
			rows = Integer.parseInt(f.readLine());
			System.out.println("ROWS = " + rows);
			System.out.println("COLS: " + cols);
			for(int i = 0; i < rows; i++){
				StringTokenizer s = new StringTokenizer(f.readLine());
				for(int j = 0; j < cols; j++){
					int k = Integer.parseInt(s.nextToken());
					if(k!= 3){
						int h = locToHash(i,j);
						validLocs.add(h);
						if(k!= 0){
							getGoalLocs()[2-k].add(h);
						}
					}
				}
			}
		}
	/*
	 * hash location
	*/
	public static int locToHash(int x, int y){
		return x*hashWidth + y;
	}
	/*
	 * unhash
	 */
	public static int[] hashToLoc(int h){
		int  y = h%hashWidth;
		int x = (h-y)/hashWidth;
		return new int[]{x,y};
	}
	public void printBoard(){
		System.out.println("BOARD");
		for(int i = 0; i < rows; i++){
			System.out.println();
			for(int j = 0; j < cols; j++){
				int a= getAtLoc(i,j);
				String s= a + "";
				if(a==(getEmpty())){
					s="*";
				}else if(a==getInvalidlocation()){
					s="-";
				}else if(a== marble){
					s = "x";
				}
				System.out.print(s);}
			
		}
		System.out.println();
	}
	/*
	 * print the board to one string according to the order of the printBoard, but this one just will give one string.
	 * Every row is segemented by one space char
	 * 
	 */
	public String printToString(){
		
		String accu="";
		for(int i = 0; i < rows; i++){
			System.out.println();
			for(int j = 0; j < cols; j++){
				int a= getAtLoc(i,j);
				String s= a + "";
				if(a==(getEmpty())){
					s="*";
					s = accu+s;
				}else if(a==getInvalidlocation()){
					s="-";
					s = accu+s;
				}else if(a== marble){
					s = "x";
					s = accu+s;
				}
				
				System.out.print(s);}
			accu = accu + " ";
			
		}
		return accu;
	}
	
	public int getAtLoc(int h){
		if(!validLocs.contains(h)) return getInvalidlocation();
		if(getPlayerLocs()[0].contains(h))return 1;
		if(getPlayerLocs()[1].contains(h))return 2;
		if(marbleLocs.contains(h))return marble;
		return getEmpty();
	}
	public int getAtLoc(int i, int j) {
		return getAtLoc(locToHash(i,j));
	}
	public int getTurn() {
		return turn;
	}
	public int getTimeRemaining(int turn2) {
		return time_remaining[turn2-1];
	}
	public boolean pieceAt(int r1, int c1) {
		int at = getAtLoc(r1, c1);
		return at!= getInvalidlocation() && at != getEmpty();
	}
	public boolean isEmpty(int r2, int c2) {
		return getAtLoc(r2,c2)== getEmpty();
	}
	/*
	 * evaluate this board w/ given neural net
	 */
	public double evaluateWith(NeuralNetwork net, int myID) {
		double[] input = net.getSpecies().getInputs(this, myID, 3-myID);
		return net.output(input)[0];
	}
	/*
	 * get player p's distance from goal at location h
	 */
	public int getDistFromGoal(int h, int p) {
		return distancesFromGoal.get(h)[p-1];
	}
	public void setGoalLocs(HashSet<Integer>[] goalLocs) {
		this.goalLocs = goalLocs;
	}
	public HashSet<Integer>[] getGoalLocs() {
		return goalLocs;
	}
	public void setDistancesFromGoal(HashMap<Integer, int[]> distancesFromGoal) {
		this.distancesFromGoal = distancesFromGoal;
	}
	public HashMap<Integer, int[]> getDistancesFromGoal() {
		return distancesFromGoal;
	}
	public void setPlayerLocs(HashSet<Integer>[] playerLocs) {
		this.playerLocs = playerLocs;
	}
	public HashSet<Integer>[] getPlayerLocs() {
		return playerLocs;
	}
	public void setMarbles(int[] marbles) {
		this.marbles = marbles;
	}
	public int[] getMarbles() {
		return marbles;
	}
	public static int getMarble(){
		return marble;
	}
	public static int getEmpty() {
		return empty;
	}
	public static int getInvalidlocation() {
		return invalidLocation;
	}
	public HashSet<Integer> getValidLocs() {
		return validLocs;
	}
	public int getRows() {
		return rows;
	}
	public int getCols(){
		return cols;
	}
	@Override
	public HashSet<Integer> getPlayerLocs(int player) {
		return this.playerLocs[player-1];
	}
	@Override
	public HashSet<Integer> getGoalLocs(int id) {
		return this.goalLocs[id-1];
	}

	public void randomize() {
		getPlayerLocs()[0].clear();
		getPlayerLocs()[1].clear();
		marbleLocs.clear();
		
		Integer[] newPos = validLocs.toArray(new Integer[0]);
		
		int randomElem = (int) (newPos.length*Math.random());
		
		for(int i = 0; i < 6; i++){
			boolean is = isValid(newPos[randomElem]);
			while(getAtLoc(newPos[randomElem]) != getEmpty()){
				randomElem = (int) (newPos.length*Math.random());
			}
			getPlayerLocs()[0].add(newPos[randomElem]);
		}
		randomElem = (int) (newPos.length*Math.random());
		for(int i = 0; i < 6; i++){
			while(getAtLoc(newPos[randomElem]) != getEmpty()){
				randomElem = (int) (newPos.length*Math.random());
			}
			getPlayerLocs()[1].add(newPos[randomElem]);
		}

		for(int ml : initFilledLocs)marbleLocs.add(ml);
		time_remaining[0]=total_time[0];
		time_remaining[1]=total_time[1];
		setMarbles(new int[2]);
		getMarbles()[0]=initMarbles[0];
		getMarbles()[1]=initMarbles[1];
		this.setDistancesFromGoal();
	}


	
	
	
	
}
