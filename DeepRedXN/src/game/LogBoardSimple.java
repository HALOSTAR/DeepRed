package game;

import species.Species;
//By Jon
//Created because Lisa's class is too complicated and strange to be useful.

public class LogBoardSimple {

	private double score;
	private Board b;
	private int player;
	

	public LogBoardSimple(Board board, int player, double normalizedScore) {
		b = board;
		this.player = player;
		score = normalizedScore;
	}

	public double[] getNNinputs(Species species) {
		return species.getInputs(b, player, 3-player);
	}

	public double getValue() {
		return score;
	}
}
