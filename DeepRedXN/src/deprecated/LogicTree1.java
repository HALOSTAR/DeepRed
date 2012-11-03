package deprecated;

import game.Board;

public class LogicTree1 extends LogicTree{
	
	public LogicTree1(int numOutputs, int numExpansionNodes,
			int inputsLow,int inputsHigh) {
		super(7,numOutputs, numExpansionNodes, inputsLow, inputsHigh);
	}
	boolean[] getInputs(Board b) {//TODO: IMPROVE THIS-- these inputs are kind of arbitrary atm
		boolean[] in = new boolean[this.numInputs];
		int myid = b.getTurn();
		int opid = 3-myid;
		in[0]=b.getMarbles()[myid-1]>0;
		in[1]=b.getMarbles()[opid-1]>0;
		int[] numMarblesInGoal = b.getMarblesInGoal();
		int mine = numMarblesInGoal[myid-1];
		int op = numMarblesInGoal[opid-1];
		in[3] = mine > op;
		in[4] = op > mine;
		int[] marblesInHome = b.getMarblesInHome();
		mine = marblesInHome[myid-1];
		op = marblesInHome[opid-1];
		in[5]=mine>op;
		in[6]=mine<op;
	//	int[] groups = b.getGroups();//get number of groups of non-adjacent marbles
	//	mine = groups[myid-1];
	//	op = groups[opid-1];
	//	in[7]=mine>1;
	//	in[8]=mine>2;
	//	in[9]=op>1;
	//	in[10]=op>2;
	//	in[11]=mine>op;
	//	in[12]=mine<op;
		
		
		
		
		
		return in;
	}

}
