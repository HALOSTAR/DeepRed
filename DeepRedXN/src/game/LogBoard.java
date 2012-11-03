package game;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import neuralnet.NeuralNetwork;
import species.Species;
public class LogBoard extends SuperBoard{
		private double value;
		private int winningPlayer;
		private Board initBoard;
		private HashSet[] locs;
		private int[] marbles;//TODO: SET THIS!!!
		private int turn;//TODO: SET THIS!!! (? is necessary)
		public int getAtLoc(int r, int c){
			int h = Board.locToHash(r,c);
			if(locs[0].contains(h))	return 1;
			if(locs[1].contains(h)) return 2;
			if(locs[2].contains(h)) return Board.getMarble();
			if(!this.initBoard.isValid(h)) return Board.getInvalidlocation();
			return Board.getEmpty();
		}
		public double[] getNNinputs(Species s, int myID){
			return s.getInputs(this, myID, 3-myID);
		}
		public LogBoard (Scanner sc, int rows, int cols, Board initBoard) throws NumberFormatException, IOException{
			this.initBoard = initBoard;
			lastReadSuccess = this.readBoard(sc,rows,cols);
		}
		public static boolean lastReadSuccess;
		public LogBoard(Board board, int turn, Board initBoard) {
			this.turn = turn;
			this.initBoard=initBoard;
			this.locs = new HashSet[3];
			Arrays.fill(locs, new HashSet<Integer>());
			for(int h : initBoard.getValidLocs()){
				int atLoc = board.getAtLoc(h);
				if(atLoc == Board.getEmpty()){}
				if(atLoc == Board.getMarble())this.locs[2].add(h);
				this.locs[turn - 1].add(h);
			}
		}
		public double getValue(int player){
			if(player ==1)return value;
			return -value;
		}
		void setValue(double v){// value is with respect to player 1
			this.value = v;
		}
		private boolean readBoard(Scanner sc, int rows, int cols) throws IOException {
			this.locs = new HashSet[3];
			this.locs[0]=new HashSet<Integer>();//player 1
			this.locs[1]=new HashSet<Integer>();//player 2
			this.locs[2]=new HashSet<Integer>();//special marbles
			//TODO: SET TURN!!!
		try{
			for(int i=0; i<rows; i++){
				String line = sc.nextLine().replace("#", "");
				for (int j = 0; j < cols&&j < line.length(); j++) {
					if(line.charAt(j)== ' ') {
						//invalid location or empty location
					}
					else { //valid location
						int h = Board.hash(new int[]{i,j});
					if(!(line.charAt(j)+"").equals("*")){
						String jt = line.charAt(j) + "";
						int n = Integer.parseInt(jt);
						this.locs[n-1].add(h);/*
						if(n==1)this.locs[0].add(h);
						else if(n==2)this.locs[1].add(h);
						else if(n==3) this.locs[2].add(h);*/
					}
					}
				}
			}
	        String line = sc.nextLine().replace("#","");
	        String[] part=line.split(" ");
	        if(part.length!=5){
	        	System.out.println("FAIL " + line);
	        	int[] r = new int[0];int t = r[4];
	        }
	        return true;//successful read;
		}catch(Exception e){
			return false;
		}
		}
		@SuppressWarnings("unchecked")
		public HashSet<Integer> getPlayerLocs(int player) {
			return this.locs[player-1];
		}
		@Override
		public int getDistFromGoal(int h, int p) {
			return this.initBoard.getDistFromGoal(h, p);
		}
		@Override
		public HashSet<Integer> getGoalLocs(int id) {
			return initBoard.getGoalLocs(id);
		}
		@Override
		public int[] getMarbles() {
			return this.marbles;
		}
		@Override
		public int getTurn() {
			return this.turn;
		}
		@Override
		public HashSet<Integer> getValidLocs() {
			return this.initBoard.getValidLocs();
		}
		@Override
		public int getCols() {
			return initBoard.getCols();
		}
		@Override
		public int getRows() {
			return initBoard.getRows();
		}
		public static LogBoard generateRandomLogBoard() {
			// TODO Auto-generated method stub
			return null;
		}
	}
