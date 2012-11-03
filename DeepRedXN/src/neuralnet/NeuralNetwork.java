package neuralnet;
import geneticAlgorithm.Mutator;
import species.Species;
/**
 * This class contains the basics of making a generic neural network, as well as the ability to create an offspring network from two parents.
 * @author Jonathan
 * @version 0.10
 * 
 */
public class NeuralNetwork {

	private Species species;
	private NeuralLayer[] layers;
	/**
	 * Species contains following hidden parameters encapsulated for ease of use
	 * @param inputSize - The size of the input layer of neurons.
	 * @param outputSize - The output size is the number of parameters that will be returned by the network
	 * @param hiddenLayers - 
	 * @param perHiddenLayer
	 */
 	public NeuralNetwork(Species s){
		if(s == null){
			throw new IllegalArgumentException("Requires species!");
		}
		if(s.hiddenLayers.length != s.numHiddenLayers){
			throw new IllegalArgumentException("Hidden layer length should be equal to number of hidden layers.");
		}
		layers = new NeuralLayer[s.numHiddenLayers+1];

		layers[0] = new NeuralLayer(s.hiddenLayers[0],s.inputSize);
		
		for(int i = 1; i < s.numHiddenLayers; i++){
			layers[i] = new NeuralLayer(s.hiddenLayers[i],layers[i-1]);
		}
		species = s;
		layers[s.numHiddenLayers] = new NeuralLayer(s.outputSize,layers[s.numHiddenLayers-1]);
	}
	
	public NeuralNetwork(NeuralLayer[] l, Species species2) {
		if(species2 == null){
			throw new IllegalArgumentException("Requires species!");
		}
		layers = l;
		this.species = species2;
	}
	
	/**
	 * Calculates and produces the output of the network given a proper input
	 * @param input
	 * @return output
	 */
	public double[] output(double[] input){
		
		for(NeuralLayer l : layers){
			l.clearData();
		}
		NeuralLayer in = layers[0];
		
		in.setInput(input);
		
		for(int i = 0; i < layers.length-1; i++){
			layers[i].fire();
		}
		
		double[] out = layers[layers.length-1].getOutput();
		
		return out;
	}

	/**
	 * Over all the output
	 * Take the change in weights to be DELTA*learningrate
	 * Calculate delta by summing over previous and multiplying by derivative of previous firing.
	 * @param output
	 */
	public void backPropogate(double[] input, double[] output, double learning_rate){
		
		double[] actual = this.output(input);
		layers[layers.length-1].calculateError(actual,output);
		
		for(int i = layers.length-1; i >= 0; i--){
			layers[i].calculateWeights(learning_rate);
		}
		for(NeuralLayer l : layers){
			l.clearData();
		}
		
	}

	/**
	 * Creates a string representation of a Neural Network
	 */
	public String toString(){
		String out = "<";
		for(int i = 0; i < layers.length; i++){
			out += layers[i].toString();
			out += ";";
		}
		out += ">";
		return out;
	}
	
	/**
	 * produces the neural network in memory from a string representation
	 * @param in - the string
	 * @param species2 - Species which the neural net belongs to
	 * @return the reconstituted net
	 */
	public static NeuralNetwork fromString(String in, Species species2){
		if(species2 == null){
			throw new IllegalArgumentException("Species required");	
		}
		
		if(in.charAt(0) != '<'){
			throw new IllegalArgumentException("Illegal entry. Neural networks start with <");
		}
		
		in = in.substring(1,in.length()-1);
		
		String[] layersString = in.split(";");
		NeuralLayer[] l = new NeuralLayer[layersString.length];

		l[0] = NeuralLayer.fromString(layersString[0],null);
		for(int i = 1; i < l.length; i++){
			l[i] = NeuralLayer.fromString(layersString[i],l[i-1]);
		}
		return new NeuralNetwork(l, species2);
		
		
	}
	
	public NeuralNetwork mutate(Mutator m){
		NeuralNetwork clone = clone();
		for(NeuralLayer n : clone.layers){
			n.mutate(m);
		}
		return clone;
	}
	
	public static NeuralNetwork reproduce(NeuralNetwork a, NeuralNetwork b){
		if(a.layers.length != b.layers.length){
			throw new IllegalArgumentException("Networks must have same size.");
		}
		
		NeuralLayer[] n = new NeuralLayer[a.layers.length];
		n[n.length-1] = NeuralLayer.reproduce(a.layers[n.length-1], b.layers[n.length-1],null);
		
		
		for(int i = n.length-2; i >= 0; i--){
			n[i] = NeuralLayer.reproduce(a.layers[i], b.layers[i],n[i+1]);
		}
		
		NeuralNetwork k =  new NeuralNetwork(n, a.getSpecies());
		return k;
	}
	
	
	public NeuralNetwork clone(){
		return NeuralNetwork.fromString(toString(),species);
	}
	
	public void setSpecies(Species s) {
		
		if(s == null){
			throw new IllegalArgumentException("Species should not be null.");
		}
	
		this.species = s;
	
	}
	
	public Species getSpecies() {
	
		return species;
	
	}

	
}