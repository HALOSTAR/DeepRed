package geneticAlgorithm;

public class RelaxingMutator extends Mutator {
	
	public RelaxingMutator(){
		super(-10,10);
	}

	public double mutate(double in){
		double preFactor = Math.random()*1;
		return mutate(preFactor*in);
	}
	

}
