package neuralnet;

import geneticAlgorithm.Mutator;

/**
 * This class contains the neuron framework for a Neural Network.
 * @author Jonathan
 * @version 1.0
 */
public class Neuron {
	
	/**
	 * Weights contains the weights from this node to nodes in the previous layer
	 */
	private double[] weights;
	/**
	 * Bias contains the bias term for this node
	 */
	private double bias;
	
	private Neuron[] previous;
	
	/**
	 * Children contains the neural nodes that are receiving this nodes connections
	 */
	private Neuron[] children;
	/**
	 * Sum contains the current activation potential for this node.
	 */
	private double storedActivation;
	
	/**
	 * Stored Error and StoreInputs are used when backpropogating, and are empty otherwise.
	 */
	private double storedError;
	private double[] storedInputs;
	
	/**
	 * This constructor is used for a noninput layers, and sets up the previous layer for use in acitvation and backpropogation
	 * @param weights - Weights contains the weights between this neuron and its child neurons.
	 * @param bias - Bias contains the bias between this neuron and its child neurons. (0 for input, random for hidden/output)
	 * @param previous - Previous layer in the neural net. Connections will be made between this neuron and the previous
	 */
	public Neuron(double[] weights, double bias, Neuron[] previous){
		if(previous != null &&weights.length != previous.length){
			throw new IllegalArgumentException("Must have number of weights equivalent to previous layer.");
		}
		this.weights = weights;
		this.bias = bias;
		this.previous = previous;
		storedActivation = 0;
		storedError=0;
		storedInputs = new double[previous.length];
	}
	
	
	/**
	 * This constructor is for input layers only.
	 * @param weights - Weights contains the weights between this neuron and its child neurons.
	 * @param bias - Bias contains the bias between this neuron and its child neurons. (0 for input, random for hidden/output)
	 * @param previous - Previous layer in the neural net. Connections will be made between this neuron and the previous
	 */
	public Neuron(double[] weights, double bias, int previous){
		if(weights.length != previous){
			throw new IllegalArgumentException("Must have number of weights equivalent to previous layer.");
		}
		this.weights = weights;
		this.bias = bias;
		storedActivation = 0;
		storedError=0;
		storedInputs = new double[previous];
	}
	
	/**
	 * This function receives values from Neurons earlier in the network, and adds them
	 * to the sum which keeps track of the Neuron's state.
	 * @param neuron 
	 */
	public void add(double d, Neuron neuron){
		int id = this.getMyWeightId(neuron);
		storedInputs[id] = d;
	}

	public void add(double d, int i) {
		storedInputs[i] = d;
	}
	
	
	/**
	 * This function causes the firing of the neuron. It sends a signal to each child.
	 */
	public void fire(){
		if(children == null){
			throw new IllegalStateException("Cannot call fire on an output layer.");
		}
		
		storedActivation = calculateActivation();
		double impulse =Math.tanh(storedActivation);
		for(int i = 0; i < children.length; i++){
			children[i].add(impulse,this);
		}
	}
	
	private double calculateActivation() {
		double value = 0;
		for(int i = 0; i < storedInputs.length; i++){
			value += storedInputs[i]*weights[i];
		}
		return value;
	}
	/**
	 * This function outputs a string containing the bias and the weights of the Neuron
	 */
	public String toString(){
		String out = "";
		out += bias;
		if(weights != null){
			for(int i = 0; i < weights.length; i++){
				out += " " + weights[i];
			}
		}
		return out;
	}
	
	/**
	 * This function is only called on a Neuron that doesn't fire, that is, an output neuron.
	 * @return The value of the sum stored in the Neuron
	 * @throws IllegalStateException when called on a non-output Neuron.
	 */
	public double read(){
			storedActivation = calculateActivation();
			double impulse = Math.tanh(storedActivation);
			return impulse;
	}

	private int getMyWeightId(Neuron neuron) {
		for(int i = 0; i < previous.length; i++){
			if(previous[i]==neuron){
				
				return i;
			}
		}
		throw new IllegalStateException("Not a valid neuron to search for.");
	}

	public void setError(double e){
		double derivative = 1 - Math.pow(Math.tanh(storedActivation), 2);
		storedError = e*derivative;
	}
	
	public double getError(){
		return storedError;
	}
	public int getChildren() {
		return children.length;
	}

	public double getStoredValue() {
		return storedActivation;
	}

	public void calculateWeights(double learningrate) {
		if(children != null)
		{
			double deltaSum = 0;
			for(Neuron n : children){
				deltaSum += n.getError()*n.getMyWeight(this);
			}
			setError(deltaSum);
		}
		
		
		for(int i = 0; i < weights.length; i++){
			weights[i] += learningrate*storedError*storedInputs[i];
		}
		//Bias functions as a weight with storedInput always 1
		bias += learningrate*storedError*1;
	}

	private double getMyWeight(Neuron neuron) {
		return weights[getMyWeightId(neuron)];
	}
	public void setNextLayer(NeuralLayer next) {
		children = next.getLayer();
	}
	
	public void clearData() {
		storedError = 0;
		storedActivation = 0;
		storedInputs = new double[storedInputs.length];
		
	}
	

	public static Neuron fromString(String string, NeuralLayer neuralLayer) {
		String[] readIn = string.split(" ");
		double[] weights = new double[readIn.length-1];
		double bias = Double.parseDouble(readIn[0]);
		for(int i = 1; i < readIn.length; i++){
			weights[i-1] = Double.parseDouble(readIn[i]);
		}
		if(neuralLayer != null){
			return new Neuron(weights,bias,neuralLayer.getLayer());
		}
		else{
			return new Neuron(weights,bias, weights.length);
		}
	}

	public void mutate(Mutator m) {		
		for(int i = 0; i < weights.length; i++){
			weights[i] = m.mutate(weights[i]);
		}
	}

	public static Neuron reproduce(Neuron neuron, Neuron neuron2,
			NeuralLayer nextLayer) {
		if(neuron.weights.length != neuron2.weights.length){
			throw new IllegalArgumentException("Invalid neuron matchup.");
		}
		double b;
		double w[] = new double[neuron.weights.length];
		boolean neuron1 = Math.random() < 0.5;
		if(neuron1){
			b = (neuron.bias);
			w = neuron.weights.clone();
		}
		else{
			b = neuron2.bias;
			w = neuron2.weights.clone();
		}
		
		
		for(int i = 0; i < w.length; i ++){
			if(Math.random() < 0.01){
				if(neuron1){
					w[i] = neuron2.weights[i];
				}
				else{
					w[i] = neuron.weights[i];
				}
			}
		}
		
		return new Neuron(w,b,nextLayer.getLayer());
		
		
		
	}
	public int getInputSize() {
		return weights.length;
	}

}