package species;

import game.Board;
import game.SuperBoard;
/*
 * @author Lisa
 * basic neural net species
 */
public class Species1 extends Species{

	public Species1(){
	    hiddenLayers = new int[]{160};
		numHiddenLayers = 1;
		outputSize = 1;
		searchDepth = 1;
		inputSize = 80;
	}
	public double[] getInputs(SuperBoard b, int x, int y) {
		return super.positionInputs(b);
	}
}
