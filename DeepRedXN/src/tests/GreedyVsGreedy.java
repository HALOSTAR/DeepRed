package tests;

import java.io.IOException;

import players.EndGameScoreMaxPlayer1;
import players.Greedy;
import species.NonMutatingPlayerGen;
import species.PlayerGen;

public class GreedyVsGreedy {

	/**
	 * @author lisaf - tests greedybot vs greedybot
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		runTest2();
	}
	private static void runTest2() throws NumberFormatException, IOException {
		PlayerGen p1g = new NonMutatingPlayerGen(new EndGameScoreMaxPlayer1());
		PlayerGen p2g = new NonMutatingPlayerGen(new Greedy());
		String[] boardStrings = new String[]{"classBoard"};
		int gamesPerPair = 1;
		int generations = 1;
		int outputPer = 10;
		int maxMoves = 1000;
		Tester.simpleTest(p1g, p2g, boardStrings, gamesPerPair, generations,outputPer,"output", maxMoves, "OURPLAYER", "GREEDY", true);
		System.out.println("DONE");
	}
	private static void runTest1() throws NumberFormatException, IOException {
		PlayerGen p1g = new NonMutatingPlayerGen(new Greedy());
		PlayerGen p2g = new NonMutatingPlayerGen(new Greedy());
		
		String[] boardStrings = new String[]{"classBoard"};
		int gamesPerPair = 1;
		int generations = 1;
		int outputPer = 10;
		int maxMoves = 1000;
		
		Tester.simpleTest(p1g, p2g, boardStrings, gamesPerPair, generations,outputPer,"output", maxMoves, "p1name", "p2name", true);
		System.out.println("DONE");
	}

}
