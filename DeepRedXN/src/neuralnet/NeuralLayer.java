package neuralnet;

import geneticAlgorithm.Mutator;

/**
 * A Neural Layer serves as a level of abstraction between individual neurons and the network itself, containing all the neruons of a layer.
 * @author Jonathan
 * @version 1.0
 */
public class NeuralLayer {
	private Neuron[] layer;

	/**
	 * Biases and weights must be initially very small for backpropogation to suceed.
	 */
	private static double minWeight = -0.000001;
	private static double maxWeight = 0.000001;
	
	private static double minBias = -0.000001;
	private static double maxBias = 0.000001;

	/**
	 * Used for non-input layers
	 * @param neuronNumber - The number of neurons to create
	 * @param previousLayer - previous layer in the network
	 */
	public NeuralLayer(int neuronNumber, NeuralLayer previousLayer){
		layer = new Neuron[neuronNumber];

		for(int i = 0; i < neuronNumber; i++){
				
			double bias;
			bias = minBias + (maxBias-minBias)*Math.random();
			double weight[] = new double[previousLayer.layer.length];
			for(int j = 0; j < weight.length; j++){
				weight[j] = minWeight + Math.random()*(maxWeight-minWeight);
			}
			if(previousLayer != null){
				previousLayer.setNextLayer(this);
				layer[i] = new Neuron(weight,bias,previousLayer.getLayer());
			}
		}
	}
	
	/**
	 * Used for input layers
	 * @param neuronNumber - The number of neurons to create
	 * @param previousLayer - previous layer in the network, null if first layer.
	 */
	public NeuralLayer(int neuronNumber, int inputSize){
		layer = new Neuron[neuronNumber];

		for(int i = 0; i < neuronNumber; i++){
				
			double bias;
			bias = minBias + (maxBias-minBias)*Math.random();
			double weight[] = new double[inputSize];
			for(int j = 0; j < weight.length; j++){
				weight[j] = minWeight + Math.random()*(maxWeight-minWeight);
			}
			layer[i] = new Neuron(weight,bias,inputSize);
		}
	}
	
	public NeuralLayer(Neuron[] l, NeuralLayer previous) {
		layer = l;
		previous.setNextLayer(this);
	}

	public NeuralLayer(Neuron[] l) {
		layer = l;
	}

	public void setNextLayer(NeuralLayer next){
		for(Neuron n : layer){
			n.setNextLayer(next);
		}
	}

	public void setInput(double[] in){	
		for(int j = 0; j < in.length; j++){
			for(int i = 0; i < layer.length; i++){
				layer[i].add(in[j],j);
			}
		}
	}
	
	public void fire(){
		for(int i = 0; i < layer.length; i++){
			layer[i].fire();
		}
	}
	
	public double[] getOutput(){
		double[] out = new double[layer.length];
		
		for(int i = 0; i < layer.length; i++){
			out[i] = layer[i].read();
		}
		
		return out;
	}
	
	public int getLength(){
		return layer.length;
	}
	
	public Neuron[] getLayer(){
		return layer;
	}

	public double[] getStoredOutputs(){
		double[] stored = new double[layer.length];
		for(int i = 0; i < layer.length; i++){
			stored[i] = layer[i].getStoredValue();
		}
		return stored;
	}
	
	/**
	 * Calculates the error in this layer, given output and supposed. Only used for output layers.
	 * @param output
	 * @param supposed
	 */
	public void calculateError(double[] output, double[] supposed){
		for(int i = 0; i < layer.length; i++){
			layer[i].setError(supposed[i]-output[i]);
		}
	}
	
	/**
	 * Updates the weights in this layer based on backpropogation algorithm
	 * @param learning_rate
	 */
	public void calculateWeights(double learning_rate){
		for(int i = 0; i < layer.length; i++){
			layer[i].calculateWeights(learning_rate);
		}
	}


	public void clearData() {
		for(Neuron n : layer){
			n.clearData();
		}
		
	}
	

	
	public String toString(){
		String out = "(";
		
		for(int i = 0; i < layer.length; i++){
			out += layer[i].toString();
			out += ",";
		}
		out += ")";
		return out;
	}


	/**
	 * Creates a layer from toString output from NeuralLayer
	 * @param in
	 * @return
	 */

	public static NeuralLayer fromString(String in, NeuralLayer neuralLayer) {
		if(in.charAt(0) != '('){
			throw new IllegalArgumentException("Illegal entry. Neural layers start with (");
		}
		
		in = in.substring(1,in.length()-1);
		
		String[] layersString = in.split(",");
		Neuron[] layer = new Neuron[layersString.length];
		
		for(int i = 0; i < layer.length; i++){
			if(neuralLayer != null){
				layer[i] = Neuron.fromString(layersString[i],neuralLayer);
			}
			else{
				layer[i] = Neuron.fromString(layersString[i],null);
			}
		}
		if(neuralLayer == null){
			return new NeuralLayer(layer);
		}
		return new NeuralLayer(layer,neuralLayer);
	}

	public void mutate(Mutator m) {
		for(Neuron n : layer){
			n.mutate(m);
		}
	}
	
	/**
	 * Reproduces two neural nets to produce a child net.
	 * @param a - Neural net 1
	 * @param b - Neural net 2
	 * @param previousLayer - the previous layer, required to properly initialize the new layer
	 * @return
	 */
	public static NeuralLayer reproduce(NeuralLayer a,
			NeuralLayer b, NeuralLayer previousLayer) {
		if(a.layer.length != b.layer.length){
			throw new IllegalArgumentException("Layers must be identical size to reproduce.");
		}
		Neuron[] l = new Neuron[a.layer.length];
		
		for(int i = 0; i < l.length; i++){
			if(previousLayer != null){
				l[i] = Neuron.reproduce(a.layer[i],b.layer[i],previousLayer);
			}
			else{
				l[i] = Neuron.reproduce(a.layer[i],b.layer[i],null);
			}
		}
		
		return new NeuralLayer(l,previousLayer);
	}
	
	
}