package geneticAlgorithm;

/**
 * Mutator is a generic class for a type of object that can change the value of doubles randomly.
 * @author Jonathan
 *
 */
public class Mutator {
	
	private int max;
	private int min;
	
	
	public Mutator(int mi, int ma){
		max = ma;
		min = mi;
	}
	public double mutate(double m){
		if(m>max)return max;
		if(m<min)return min;
		return m;
	}
	

}