package players;

import game.Board;
import game.LogBoard;
import game.LogBoardSimple;
import game.Move;
import geneticAlgorithm.Mutator;

import java.util.ArrayList;
import java.util.Collections;


import species.Species;

import neuralnet.NeuralNetwork;
/*
 * @Author Lisa
 * Uses neural net and alpha-beta pruning to select moves
 */
public class NeuralNetPlayer extends Player{
	private NeuralNetwork net;
	private NNABSelect selector;
	private static double LEARNING_RATE = 0.01;
	
	public Move request_move(int[] args) {
		int searchDepth = 1;//TODO
		if(selector==null)selector = new NNABSelect(net);
		return selector.alphaBeta(searchDepth, board,id);
	}
	public NeuralNetPlayer(Species s){
		this.net = new NeuralNetwork(s);	
	}

	public NeuralNetPlayer(NeuralNetwork n){
		this.net=n;
	}
	public static class NNABSelect extends AlphaBetaSelect{
		private NeuralNetwork net;
		public NNABSelect(NeuralNetwork n){
			this.net=n;
		}
		public double evaluateBoard(Board b, int myID) {
			return b.evaluateWith(net,myID);
		}
		public ArrayList<Move> genMoves(Board b) {
			
			return net.getSpecies().genAllMoves(b);
		}	
	}
	
	public String toString(){
		return net.toString() + " SCORE : " + getScore();
	}
	public Player mutate(Mutator mut) {
		return new NeuralNetPlayer(this.net.mutate(mut));
	}

	public void trainOn(ArrayList<LogBoard> memories, int times) {
		if(times == 0) return;
		Collections.shuffle(memories);
		
		for(LogBoard mem : memories){
			this.net.backPropogate(mem.getNNinputs(net.getSpecies(),this.id), new double[]{mem.getValue(this.id)},LEARNING_RATE);
		}
		trainOn(memories,times-1);
	}
	public void trainOnHeuristic(ArrayList<LogBoardSimple> memories, int times) {
		if(times == 0) return;
		Collections.shuffle(memories);
		
		for(LogBoardSimple mem : memories){
			this.net.backPropogate(mem.getNNinputs(net.getSpecies()), new double[]{mem.getValue()},LEARNING_RATE);
		}
		trainOnHeuristic(memories,times-1);
		
	}
	
	
}