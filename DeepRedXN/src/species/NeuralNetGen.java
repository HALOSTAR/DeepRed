package species;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;



import players.NeuralNetPlayer;
import players.Player;
import game.LogBoard;
import game.LogBoardSimple;
import geneticAlgorithm.Mutator;
import geneticAlgorithm.RelaxingMutator;
/**
 * @author Lisa
 * edited and now maintained by Jon
 * Stores and evolves Neural Nets, as well as allowing for large scale backpropogation
 * 
 **/
public class NeuralNetGen extends PlayerGen{
	private Species species;
	
	
	public NeuralNetGen(Species s){
		this.species = s;
	}
	
	/**
	 * Evolve returns a new generation using a mutator
	 * responsibility of Jon
	 */
	public void evolve(int stage) {
		Player[] myPlayers = getCurrentGeneration();
		Arrays.sort(myPlayers);
		ArrayList<Player> nextGenSeed = new ArrayList<Player>();
		
		for(int i = 0; i < 20; i++){
			myPlayers[i].resetScore();
			nextGenSeed.add(myPlayers[i]);
		}
		
		Mutator mut = getMutator(stage);

		ArrayList<Player> nextGen = new ArrayList<Player>();
		for(Player p: nextGenSeed){
			NeuralNetPlayer x = (NeuralNetPlayer)p;
			//Mutate
			nextGen.add(x);
			
			for(int j = 0; j < 9; j++){
				nextGen.add(x.mutate(mut));
			}
			
		}
		setCurrentGeneration(nextGen.toArray(new Player[1]));
	}
	
	private Mutator getMutator(int i) {
			return new RelaxingMutator();
	}

	/*
	 * initialize neural net players
	 */
	public Player[] makePlayers(int numToMake) {
		Player[] p = new Player[numToMake];
		for(int i = 0; i < numToMake; i++){
			p[i] = new NeuralNetPlayer(species);
		}	
		setCurrentGeneration(p);
		return p;
	}
	

	/**
	 * Produces a file used for storing generations of Neural nets
	 * @author Jon
	 * @param s
	 * @throws IOException
	 */
	public void save(String s) throws IOException {
		File f = new File(s);
		if(!f.exists())f.createNewFile();
		BufferedWriter x = new BufferedWriter(new FileWriter(f));
		Player[] o = getCurrentGeneration();
		Arrays.sort(o);
		for(Player n : o){
			if(n instanceof NeuralNetPlayer){
				x.write(n.toString()+'!');
			}
		}		
	}

	/**
	 * Trains neural nets on LogBoards, created by Jon to use Lisas LogBoard
	 * @param memories
	 */
	public void directedEvolution(ArrayList<LogBoard> memories) {
		Player[] myPlayers = getCurrentGeneration();
		Arrays.sort(myPlayers);
		ArrayList<Player> nextGenSeed = new ArrayList<Player>();
		for(int i = 0; i < myPlayers.length; i++){ //i<20
			myPlayers[i].resetScore();
			nextGenSeed.add(myPlayers[i]);
		}
		
		for(Player p : nextGenSeed){
			((NeuralNetPlayer) p).trainOn(memories,1);
		}

		setCurrentGeneration(nextGenSeed.toArray(new Player[0]));
	}

	/**
	 * Trains neural nets on LogBoardSimple, created by Jon
	 * @param memories
	 */
	public void directedEvolutionHeuristic(ArrayList<LogBoardSimple> memories) {
		Player[] myPlayers = getCurrentGeneration();
		Arrays.sort(myPlayers);
		ArrayList<Player> nextGenSeed = new ArrayList<Player>();
		for(int i = 0; i < myPlayers.length; i++){ //i<20
			myPlayers[i].resetScore();
			nextGenSeed.add(myPlayers[i]);
		}
		
		for(Player p : nextGenSeed){
			((NeuralNetPlayer) p).trainOnHeuristic(memories,10);
		}

		setCurrentGeneration(nextGenSeed.toArray(new Player[0]));
	}

}
