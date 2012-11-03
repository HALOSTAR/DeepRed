package geneticAlgorithm;

public class BasicMutator extends Mutator{
	
	
	private double vari;
	public BasicMutator(int mi, int ma, double v) {
		super(mi, ma);
		vari = v;
	}

	public double mutate(double m) {
		double d = m+Math.random() * 2*vari - vari;
		return d;
	}

}
