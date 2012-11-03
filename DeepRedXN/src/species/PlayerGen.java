package species;
import game.Board;
import game.Result;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import players.NeuralNetPlayer;
import players.Player;

/*
 * @author Lisa
 * for use with tester, generates, and evolves players 
 * + has collector to collect statistics about game results
 */
public abstract class PlayerGen {
	public Collector collector;
	boolean save;
	private Player[] currentGen;
	public abstract Player[] makePlayers(int numToMake);
	public abstract void evolve(int stage);
    public void takeResult(Result r, int playingAs, Board b, int playerIndex) {
    	if(collector == null)return;
    	collector.takeResult(r, playingAs, b, playerIndex);	
	}
	public void setCollector(Collector collector2) {
		collector = collector2;
	}

	public void setCurrentGeneration(Player[] readGen) {
		currentGen = readGen;
	}
	
	public Player[] getCurrentGeneration(){
		return currentGen;
	}
}
