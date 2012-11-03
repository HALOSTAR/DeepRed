package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import species.Species;

/*
 * @author Lisa
 * 
 * Usage: 
 * To get list of game states: 
 * ArrayList<LogBoard> log = fromLogFile("my_log_file", .1) <-- .1 = decay const
 * to get NN inputs of a LogBoard lb: 
 * to get Value: lb.getValue() 	
 * to get NN inputs: 						
 * 
 * 
 */
public class LogReader {
	/*
	 * returns log file in reversed order (final state first)
	 */
	/*
	 * train on winning player or player specified by playerID
	 */
	
	public static void main (String[] args) throws NumberFormatException, IOException{
		ArrayList<LogBoard> fb = LogReader.fromLogFile("output10.out2", 0.7);
		fb.size();
		//System.out.println("Board from file "+ fb.size());
		
		
		
	}
	
	public static ArrayList<LogBoard> fromLogFile(String file, double decayConst) throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		Scanner sc = new Scanner(br);
		
		ArrayList<LogBoard> log= new ArrayList<LogBoard>();
		
		int playerID = 1;//train wrt this player (required by log board implementation)
		
		Board initBoard = new Board(sc);
		System.out.println(initBoard.printToString());
		if(!Board.lastReadSuccess)return null;
		int rows = initBoard.getRows();
		int cols = initBoard.getCols();
				
		boolean first = false;
		while(true){
			try{
				sc.nextLine();
				LogBoard lb = new LogBoard(sc, rows, cols, initBoard);
				if(lb != null)log.add(0, lb);
				sc.nextLine();
			}catch(NoSuchElementException e){
				break;
			}
				first=false;
		}
		System.out.println("LOG SIZE: " + log.size());
		try{
		double playerEndState = getNormalizedScore(log.get(0), initBoard,playerID);
		double boardVal = playerEndState;
		for(int i = 0; i < log.size(); i++){
			log.get(i).setValue(boardVal);
			boardVal*= decayConst;
		}
		System.out.println("DONE X");
		return log;
		}
		catch(Exception e){
			return null;
		}
	}
	
	private static double getNormalizedScore(LogBoard logBoard, Board init, int player) {
		// score for player --> + 1 if player wins, -1 if player loses
		double[] sumSquare= new double[2];
		for(int i = 0; i < 2; i++){
			for(int h : logBoard.getPlayerLocs(i+1)){
				int dist = init.getDistFromGoal(h, i+1);
				sumSquare[i] += dist*dist; 
			}
		}
		double sum = sumSquare[0]+sumSquare[1];
		double diff = sumSquare[0]-sumSquare[1];
		if(player==1)diff *= -1;
		return diff/sum;
	}
	
	public static ArrayList<LogBoard> fromLogFile1(String files, double learningConst) throws NumberFormatException, IOException {
		
		Scanner br = new Scanner(new BufferedReader(new FileReader(files)));
		ArrayList<LogBoard> log= new ArrayList<LogBoard>();
		
		Board initBoard = new Board(br);
		int rows = initBoard.getRows();
		int cols = initBoard.getCols();

		Scanner sc = new Scanner(files);		
		boolean first = true;
		while(true){
			try{
				log.add(0, new LogBoard(sc, rows, cols, initBoard));
				sc.nextLine();
				if(!first)sc.nextLine();
			}catch(Exception e){break;}
				first=false;
		}
		double playerEndState = getNormalizedScore(log.get(0), initBoard,1);
		double altScore = getNormalizedScore(log.get(0), initBoard,2);
		if(altScore > playerEndState){
			playerEndState = altScore;
		}
		double boardVal = playerEndState;
		for(int i = 0; i < log.size(); i++){
			log.get(i).setValue(boardVal);
			boardVal*= learningConst;
		}
		return log;
	}
}
