package deprecated;
import java.util.*;

import game.Board;
/*
 * @author Lisa
 * Possible alternative to neuralNets (never finished)
 */
public abstract class LogicTree {
	private int logicNodeID=0;
	private static final boolean[] cannotEval=new boolean[]{false};
	private static final boolean[] outputFalse = new boolean[]{true,false};
	private static final boolean[] outputTrue = new boolean[]{true,true};
	private int numOutputs;
	private LogicNode[] outputs;
	protected int numInputs;
	private Leaf[] inputs;
	/*
	 * random tree
	 */
	public LogicTree(int numInputs, int numOutputs, int numExpansionNodes,
			int inputsLow, int inputsHigh){
		this.numInputs=numInputs;
		this.numOutputs=numOutputs;
		outputs = new LogicNode[numOutputs];
		inputs = new Leaf[numInputs];
		for(int i = 0; i < numInputs; i++)inputs[i]=new Leaf(0);
		ArrayList<LogicNode> allNodes = new ArrayList<LogicNode>();
		HashMap<Integer, LogicNode> top = new HashMap<Integer,LogicNode>();
		//Top = logic nodes that are not the children of any other logic nodes, and, thus, will not contribute to the output
		
		for(Leaf l : inputs){
			allNodes.add(l);top.put(l.id,l);
		}
		
		//Expand from inputs
		for(int i = 0; i < numExpansionNodes; i++){
			//System.out.println("I = " + i + " " + top.size());
			LogicNode l = randomNode(inputsLow, inputsHigh);
			int numIn = l.getNumInputs();
			LogicNode[] children = new LogicNode[numIn]; 
			for(int j = 0; j < numIn; j++){
				int childIndex = (int)(Math.random() * allNodes.size());
				LogicNode child = allNodes.get(childIndex);
				top.remove(child);
				children[j]=child;
			}
			l.setChildren(children);
			top.put(l.id,l); allNodes.add(l);
		}
	
		//Now shrink top until it is the same size as outputs
		//this loop decreases the size of top by 1 on each iteration
	
		while(top.size() > numOutputs){
			int in = 2;
			LogicNode n = randomNode(in,in);//2 inputs --> 1 output
			LogicNode[] children = new LogicNode[in];
			for(int i = 0; i < in; i++){
				int childIndex = (int)(Math.random() * top.size());
				LogicNode child = getAtIndex(childIndex, top);
				top.remove(child.id);
				children[i]=child;
			}
			n.setChildren(children);
			top.put(n.id,n);allNodes.add(n);
		}
		
		//set outputs
		int j = 0;
		
		for(int n : top.keySet()){
			outputs[j]=top.get(n);j++;
		}
	}
	private LogicNode getAtIndex(int childIndex, HashMap<Integer, LogicNode> top) {
		Iterator<LogicNode> t = top.values().iterator();
		while(childIndex>0){
			t.next();childIndex--;
		}
		return t.next();
	}
	private LogicNode randomNode(int numInputsLow, int numInputsHigh){
		int i = (int)(4*Math.random());
		int inputsDiff = (numInputsHigh - numInputsLow + 1);
		int numInputs = (int)(inputsDiff * Math.random())+numInputsLow;
		if(i==0)return new Xor(numInputs);
		if(i==1)return new And(numInputs);
		if(i==2)return new Or(numInputs);
		if(i==3)return new Nor(numInputs);
		return null;
	}
	/*
	 * get output for input in
	 */
	public boolean[] eval(Board b){
		boolean[] in = getInputs(b);
		for(LogicNode o : outputs){
			o.unEval();//unevaluate
		}
		for(int i = 0; i < inputs.length;i++){
			inputs[i].val=in[i];//Set inputs
		}
		boolean[] out=new boolean[numOutputs];
		for(int i = 0; i < numOutputs; i++)	out[i]=outputs[i].eval();
		return out;
	}
	abstract boolean[] getInputs(Board b);
	private class Leaf extends LogicNode{
		public Leaf(int numChildren) {
			super(0);
			this.setChildren(new LogicNode[0]);
		}
		public boolean val;
		boolean forceEval() {
			return val;
		}
		boolean[] tryEval(boolean b) {
			if(val)return outputTrue;
			return outputFalse;
		}
		
	}
	private class Xor extends LogicNode{
		public Xor(int numChildren) {
			super(numChildren);
		}
		//at least 1 input has value true and 1 has value false
		boolean trueFound = false;
		boolean falseFound = false;
		boolean forceEval() {
			return false;
		}
		boolean[] tryEval(boolean b) {
			if(this.evaluated){
				if(lastVal)return outputTrue;return outputFalse;
			}
			if(b&!trueFound){
				if(falseFound)return outputTrue;
				trueFound=true;
				return cannotEval;
			}
			if(!falseFound){
				if(trueFound) return outputTrue;
				falseFound=true;
				return cannotEval;
			}
			return cannotEval;
		}
	}
	private class And extends LogicNode{
		public And(int numChildren) {
			super(numChildren);
		}
		boolean forceEval() {
			return true;
		}
		boolean[] tryEval(boolean b) {
			if(this.evaluated){
				if(lastVal)return outputTrue;return outputFalse;
			}
			if(!b)return outputFalse; 
			return cannotEval;
		}
	}
	private class Or extends LogicNode{
		public Or(int numChildren) {
			super(numChildren);
		}
		boolean forceEval() {
			return false;
		}
		boolean[] tryEval(boolean b) {
			if(this.evaluated){
				if(lastVal)return outputTrue;return outputFalse;
			}
			if(b)return outputTrue;
			return cannotEval;
		}
		
	}
	private class Nor extends LogicNode{
		public Nor(int numChildren) {
			super(numChildren);
		}
		// none of inputs are true
		boolean forceEval() {
			return true;
		}
		boolean[] tryEval(boolean b) {
			if(this.evaluated){
				if(lastVal)return outputTrue;return outputFalse;
			}
			if(b)return new boolean[]{true,false};
			return cannotEval;
		}
	}
	private abstract class LogicNode{  
		public boolean lastVal;
		public boolean evaluated;
		public int id;
		private LogicNode[] children;
		private int numChildren;
		public LogicNode(int numChildren){
			this.numChildren = numChildren;
			this.id = logicNodeID;logicNodeID++;
		}
		public int getNumInputs(){
			return numChildren;
		}
		public void setChildren(LogicNode[] l){
			children=l;
		}
		abstract boolean[] tryEval(boolean b);
		public void unEval() {
			//if this node was not evaluated none of its children are evaluated
			if(evaluated){
				evaluated=false;
				for(LogicNode n : children)n.unEval();
			}
		}
		abstract boolean forceEval();
		public boolean eval(){
			for(LogicNode c : children){
				boolean b = c.eval();
				boolean[] x = tryEval(b);
				if(x[0]){
					evaluated=true;
					return x[1]; 
				}
			}
			evaluated=true;
			return forceEval();
		}
		
	}

}
