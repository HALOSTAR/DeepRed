package tests;
import game.Board;
import game.Game;
import game.LogBoard;
import game.LogBoardSimple;
import game.LogReader;
import game.Result;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import players.Greedy;
import players.Player;

import species.PlayerGen;
import species.NeuralNetGen;
import species.Species;
/**
 *Test a set of neural networks, and mutate them repeatedly.
 * @author Lisa
 *
 *Used by Jon to store more recent efforts
 */
public class Tester {
	

	private static ArrayList<LogBoard> importantMemories = new ArrayList<LogBoard>();
	/*
	 * run test
	 */
	public static void simpleTest(PlayerGen p1g, PlayerGen p2g, String[] boardStrings,
			int gamesPerPair, 
			int generations, int outputLength, String outname, int maxMoves, String p1Name, String p2Name, 
			boolean verbose)
					throws NumberFormatException, IOException{
		Result.init();
		ArrayList<Board> boards = makeBoards(boardStrings);
		System.out.println("SIMPLE TEST...");
		for(int i = 0; i < generations; i++){
			getNextGen(gamesPerPair, maxMoves, p1g, p2g, boards,i,outputLength,outname, p1Name,p2Name,verbose);
		}
	}
	public static void tdTest(PlayerGen p1g, PlayerGen p2g, String[] boardStrings,
			int gamesPerPair, 
			int generations, int outputLength, String outname, String path, boolean verbose)
					throws NumberFormatException, IOException{
		Result.init();
		String files;

		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();        
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				if (files.endsWith(".txt") || files.endsWith(".TXT")){
<<<<<<< HEAD
					ArrayList<LogBoard> game = LogReader.fromLogFile(folder.getAbsolutePath()+"//"+files, 0.99);

				}
			}
		}
		
		System.out.println("..................1");
		ArrayList<Board> boards = makeBoards(boardStrings);
		System.out.println("..................2");
		for(int i = 0; i < generations; i++){
			getNextGen(gamesPerPair, 30, p1g, p2g, boards,i,outputLength,outname,"p1name","p2name", verbose);
		}
	}
	/*
	 * have p1p and p2p compete, then return their next generations
	 */
	private static void getNextGen(int gamesPerPair, int maxMoves, PlayerGen p1g, PlayerGen p2g,
			ArrayList<Board> boards, int step, int outputLength, 
			String outname, String p1Name, String p2Name, boolean verbose) throws IOException {
		int p1Index = 0;
		int p2Index = 0;
		Player[] p1p = p1g.getCurrentGeneration();
		Player[] p2p = p2g.getCurrentGeneration();
		p1Index=0;
		ArrayList<LogBoard> memories = new ArrayList<LogBoard>();
		for(Player p1 : p1p){
			p2Index=0;
			for(Player p2 : p2p){
				for(Board b : boards){
					for(int g = 0; g < gamesPerPair; g++){	
						b.reset();
						if(verbose){
							System.out.println("INIT BOARD");
							b.printBoard();}
						Result r1 = Game.playGame(b, p1, p2, maxMoves);
						if(verbose){
							String winner = "NONE, p1 = " + p1Name + " p2 = " + p2Name;
							if(r1.winner == 1)winner = p1Name;
							if(r1.winner == 2)winner = p2Name;
							System.out.println("WINNER1 = " + winner);
							b.printBoard();
						}
						
						p1g.takeResult(r1, 1, b, p1Index);
						p2g.takeResult(r1, 2,b, p2Index);
						memories.addAll(r1.getHistory());
						b.reset();
						Result r2 = Game.playGame(b, p2, p1, maxMoves);
						if(verbose){
							String winner = "NONE, p1 = " + p2Name + " p2 = " + p1Name;
							if(r2.winner == 1)winner = p2Name;
							if(r2.winner == 2)winner = p1Name;
							System.out.println("WINNE2 = " + winner);
							b.printBoard();
						}
						p1g.takeResult(r2, 2, b, p1Index);
						p2g.takeResult(r2, 1, b, p2Index);
						memories.addAll(r2.getHistory());
					}
				}p2Index++;
			}p1Index++;
		}
		if(step % outputLength == 0){
			if(p1g instanceof NeuralNetGen){
				String filename1 = outname+step+".out";
				((NeuralNetGen) p1g).save(filename1);
			}
			if(p2g instanceof NeuralNetGen){
				String filename2 = outname+step+".out2";
				((NeuralNetGen) p2g).save(filename2);
			}
		}
		if(p1g instanceof NeuralNetGen){
			((NeuralNetGen) p1g).directedEvolution(step,memories);
		}
		if(p2g instanceof NeuralNetGen){
			((NeuralNetGen) p2g).directedEvolution(step,memories);
		}
	}
	/*
	 * make boards from strings
	 */
	private static ArrayList<Board> makeBoards(String[] boardStrings) throws NumberFormatException, IOException {
		ArrayList<Board> all = new ArrayList<Board>();
		for(String s : boardStrings){
			Scanner sc = new Scanner(new BufferedReader(new FileReader(s)));	
			all.add(new Board(sc));
		}
		return all;
	}
	/*
	 * New method to add the memory to one file named rec
	 */
	public static void writeMemory(ArrayList<Board> mem,String rec) throws IOException{
		FileWriter f = new FileWriter(rec);
		BufferedWriter bf = new BufferedWriter(f);
		for ( Board b : mem){
			bf.write(b.toString());
		}
	}
}
=======
					ArrayList<LogBoard> game = LogReader.fromLogFile(folder.getAbsolutePath()+"//"+files, 0.99);
					if(game != null){
						importantMemories.addAll(game);
					}
				}
			}
		}
		
		if(verbose){
			System.out.println("..................1");
		}
		ArrayList<Board> boards = makeBoards(boardStrings);

		if(verbose){
			System.out.println("..................2");
		}
		for(int i = 0; i < generations; i++){
			getNextGen(gamesPerPair, 20, p1g, p2g, boards,i,outputLength,outname,"p1name","p2name", verbose);
		}
	}
	/*
	 * have p1p and p2p compete, then return their next generations
	 */
	private static void getNextGen(int gamesPerPair, int maxMoves, PlayerGen p1g, PlayerGen p2g,
			ArrayList<Board> boards, int step, int outputLength, 
			String outname, String p1Name, String p2Name, boolean verbose) throws IOException {
		int p1Index = 0;
		int p2Index = 0;
		Player[] p1p = p1g.getCurrentGeneration();
		Player[] p2p = p2g.getCurrentGeneration();
		p1Index=0;
		ArrayList<LogBoard> memories = new ArrayList<LogBoard>();
		for(Player p1 : p1p){
			p2Index=0;
			for(Player p2 : p2p){
				for(Board b : boards){
					for(int g = 0; g < gamesPerPair; g++){	
						b.reset();
						if(verbose){
							System.out.println("INIT BOARD");
							b.printBoard();}
						Result r1 = Game.playGame(b, p1, p2, maxMoves);
						if(verbose){
							String winner = "NONE, p1 = " + p1Name + " p2 = " + p2Name;
							if(r1.winner == 1)winner = p1Name;
							if(r1.winner == 2)winner = p2Name;
							System.out.println("WINNER1 = " + winner);
							b.printBoard();
						}
						
						p1g.takeResult(r1, 1, b, p1Index);
						p2g.takeResult(r1, 2,b, p2Index);
						memories.addAll(r1.getHistory());
						b.reset();
						Result r2 = Game.playGame(b, p2, p1, maxMoves);
						if(verbose){
							String winner = "NONE, p1 = " + p2Name + " p2 = " + p1Name;
							if(r2.winner == 1)winner = p2Name;
							if(r2.winner == 2)winner = p1Name;
							System.out.println("WINNE2 = " + winner);
							b.printBoard();
						}
						p1g.takeResult(r2, 2, b, p1Index);
						p2g.takeResult(r2, 1, b, p2Index);
						memories.addAll(r2.getHistory());
					}
				}p2Index++;
			}p1Index++;
		}
		if(step % outputLength == 0){
			if(p1g instanceof NeuralNetGen){
				String filename1 = outname+step+".out";
				((NeuralNetGen) p1g).save(filename1);
			}
			if(p2g instanceof NeuralNetGen){
				String filename2 = outname+step+".out2";
				((NeuralNetGen) p2g).save(filename2);
			}
		}
		
		memories.addAll(importantMemories);
		
		if(p1g instanceof NeuralNetGen){
			((NeuralNetGen) p1g).directedEvolution(memories);
		}
		if(p2g instanceof NeuralNetGen){
			((NeuralNetGen) p2g).directedEvolution(memories);
		}
	}
	/*
	 * make boards from strings
	 */
	private static ArrayList<Board> makeBoards(String[] boardStrings) throws NumberFormatException, IOException {
		ArrayList<Board> all = new ArrayList<Board>();
		for(String s : boardStrings){
			Scanner sc = new Scanner(new BufferedReader(new FileReader(s)));	
			all.add(new Board(sc));
		}
		return all;
	}
	/*
	 * New method to add the memory to one file named rec
	 */
	public static void writeMemory(ArrayList<Board> mem,String rec) throws IOException{
		FileWriter f = new FileWriter(rec);
		BufferedWriter bf = new BufferedWriter(f);
		for ( Board b : mem){
			bf.write(b.toString());
		}
	}
	public static void trainingTest(PlayerGen p1g, String[] boardStrings,
			int boardsToGen, int generations, int outputPer, String string, Species nnToTrain) throws NumberFormatException, IOException {
		ArrayList<Board> boards = makeBoards(boardStrings);

		Result.init();
		for(int i = 0; i < generations; i++){

			ArrayList<LogBoardSimple> memories = new ArrayList<LogBoardSimple>();

			for(int j = 0; j < boardsToGen; j++){
	
				for(Board b : boards){
					b.randomize();
					double norm1 = Species.getNormalizedScore(b, 1);
					double norm2 = Species.getNormalizedScore(b, 2);
					memories.add(new LogBoardSimple(b,1,norm1));
					memories.add(new LogBoardSimple(b,2,norm2));
				}
			}
			
			
			if(p1g instanceof NeuralNetGen){
				((NeuralNetGen) p1g).directedEvolutionHeuristic(memories);
			}
			testAndSave(p1g,string,boards,i);
		}
	}
	
	private static void testAndSave(PlayerGen p1g, String outName, ArrayList<Board> boards, int step) throws IOException {
			Player[] p1p = p1g.getCurrentGeneration();
			int p1Index = 0;
			int maxMoves = 20;
			Player p2 = new Greedy();
			for(Player p1 : p1p){
				for(Board b : boards){
					b.reset();
					Result r1 = Game.playGame(b, p1, p2, maxMoves);							
					p1g.takeResult(r1, 1, b, p1Index);
					b.reset();
					Result r2 = Game.playGame(b, p2, p1, maxMoves);
					p1g.takeResult(r2, 2, b, p1Index);
				}p1Index++;
			}
			if(p1g instanceof NeuralNetGen){
				String filename1 = outName+step+".out";
				((NeuralNetGen) p1g).save(filename1);
			}
	}
}
>>>>>>> fd72544accec2a3971fd5ff15a20df0c75e8af6a
