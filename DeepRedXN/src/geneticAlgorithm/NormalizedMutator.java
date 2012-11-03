package geneticAlgorithm;

public class NormalizedMutator extends Mutator{
	
	
	private double vari;
	private int n;
	public NormalizedMutator(int mi, int ma, double v, int n) {
		super(mi, ma);
		vari = v;
		this.n = n;
	}

	public double mutate(double m) {
		double s = 0;
		for(int i = 0; i < n; i++){
			double d = m+Math.random() * 2*vari - vari;
			s+=d;
		}
		return s/((double)n);
	}
}
