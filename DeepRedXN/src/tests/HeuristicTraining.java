
package tests;

import java.io.IOException;
import species.Collector;
import species.NeuralNetGen;
import species.PlayerGen;
import species.Species;
import species.Species3;

public class HeuristicTraining {
	/**
	 *	 @author Jon - train NN on randomly generated boards.
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		runTest();
	}
	private static void runTest() throws NumberFormatException, IOException {
		Species test = new Species3();
		PlayerGen p1g = new NeuralNetGen(test);
		
		p1g.collector = new Collector(p1g);		
		
		String[] boardStrings = new String[]{"classBoard"};
		int boardsToGen = 300;
		int generations = Integer.MAX_VALUE;
		int outputPer = 1;
		p1g.makePlayers(10);
		Tester.trainingTest(p1g, boardStrings, boardsToGen, generations, outputPer,"output",test);
	}

}
