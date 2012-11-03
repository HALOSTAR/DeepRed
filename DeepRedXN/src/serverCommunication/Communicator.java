package serverCommunication;

import game.Board;
import game.Move;

import java.util.HashSet;
import java.util.Scanner;

import players.EndGameScoreMaxPlayer1;

/**
 * @author Jonathan
 *
 * Last minute coup of Lisas responsibilities since her code doesnt work and its due in like 3 days
 * Bypasses not working Board Scanner method, and attempts to be as easy to understand in output as possible
 * Uses EndScoreGameMaxPlayer as this heuristic is said to beat Greedy Bot (at least so says Lisa)
 */
public class Communicator {
	
	private static Board theBoard;
	private static int player=0;
	private static int myTime=0;
	
	public static void main(String[] args){
		
		Scanner readIn = new Scanner(System.in);
		EndGameScoreMaxPlayer1 lisasPlayer = new EndGameScoreMaxPlayer1();
		readConfig(readIn);
		lisasPlayer.board = theBoard;
		boolean gameOver = false;
		
		
		while(!gameOver){
			
			if(player != 1){
				System.err.println("Waiting for enemy update");
				readEnemyUpdate(readIn);
			}
			
			Move myMove = lisasPlayer.request_move(null);
			
			System.out.println(myMove);

			readMyUpdate(readIn);
			
			if(player == 1){
				System.err.println("Waiting for enemy update");
				readEnemyUpdate(readIn);
			}
			
			if(myTime < 10000){
				lisasPlayer.changeDepth(2);
			}
			
		}
	}

	private static void readMyUpdate(Scanner readIn) {
		String l = readIn.nextLine();
		int status = Integer.parseInt(l);
		if(status != 0) System.exit(0);
	}

	private static void readEnemyUpdate(Scanner readIn) {
		String line = readIn.nextLine();
		String[] split = line.split(" ");
		
		int status = Integer.parseInt(split[0]);
		
		if(status != 0) System.exit(0);
		

		int player1Time = Integer.parseInt(split[1]);
		int player2Time = Integer.parseInt(split[2]);

		if(player == 1){
			myTime = player1Time;
		}
		else{
			myTime = player2Time;
		}
		int xStart = Integer.parseInt(split[3]);
		int yStart = Integer.parseInt(split[4]);
		int xEnd = Integer.parseInt(split[5]);
		int yEnd = Integer.parseInt(split[6]);
		int specialX = Integer.parseInt(split[7]);
		int specialY = Integer.parseInt(split[8]);

		int[] start = new int[]{xStart,yStart};
		int[] end = new int[]{xEnd,yEnd};
		int[] marbleLoc= null;
		if(specialX != -1 && specialY != -1){
			marbleLoc = new int[]{specialX,specialY};	
		}
		Move m = Move.getMove(start, end, marbleLoc);
		theBoard.makeMove(m);
	}

	private static void readConfig(Scanner s) {
		HashSet<Integer> player1Locs = new HashSet<Integer>();
		HashSet<Integer> player2Locs = new HashSet<Integer>();
		HashSet<Integer> marbleLocs = new HashSet<Integer>();
		HashSet<Integer> validLocs = new HashSet<Integer>();
		int[] initMarbles = new int[2];
			
		for(int i = 0; i < 17; i++){
			String line = s.nextLine();
			System.err.println("Reading line " + i);
			if(line.length() != 25){
				System.err.println("Line appears to be length" + line.length());
			}
			for(int j = 0; j < line.length(); j++){
				if(line.charAt(j) != ' '){
					validLocs.add(Board.locToHash(i, j));
				}
				if(line.charAt(j) == '1'){
					player1Locs.add(Board.locToHash(i, j));
				}
				if(line.charAt(j) == '2'){
					player2Locs.add(Board.locToHash(i, j));
				}
				if(line.charAt(j) == '0'){
					marbleLocs.add(Board.locToHash(i, j));
				}
			}
			
		}

		String line = s.nextLine();
		
		String[] lines = line.split(" ");

		int player1Time = Integer.parseInt(lines[0]);
		int player2Time = Integer.parseInt(lines[1]);
		initMarbles[0] = Integer.parseInt(lines[2]);
		initMarbles[1] = Integer.parseInt(lines[3]);
		

		String p = s.nextLine();
		player = Integer.parseInt(p);
		if(player == 1){
			myTime = player1Time;
		}
		else{
			myTime = player2Time;
		}
		
		theBoard = new Board(player1Locs, player2Locs, marbleLocs, validLocs, initMarbles);
		
	}

}
