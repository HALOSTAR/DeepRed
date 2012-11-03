
package tests;

import java.io.IOException;
import players.Greedy;
import species.Collector;
import species.NeuralNetGen;
import species.NonMutatingPlayerGen;
import species.PlayerGen;
import species.Species;
import species.Species2;

public class TDLearning {
	/**
	 *	 @author Jon - Train using TD learning algorithm
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		runTest();
	}
	
	private static void runTest() throws NumberFormatException, IOException {
		Species test = new Species2();
		PlayerGen p1g = new NonMutatingPlayerGen(new Greedy());
		PlayerGen p2g = new NeuralNetGen(test);
		
		p2g.collector = new Collector(p2g);
		p1g.collector=null;
		
		String[] boardStrings = new String[]{"classBoard"};
		int gamesPerPair = 3;
		int[] perGen = new int[]{1,10};// players per generation
		int generations = Integer.MAX_VALUE;
		int outputPer = 10;
		
		p1g.makePlayers(perGen[0]);
		p2g.makePlayers(perGen[1]);
		
		Tester.tdTest(p1g, p2g, boardStrings, gamesPerPair, generations, outputPer,"output","proGamerData", false);
		
	}

}
