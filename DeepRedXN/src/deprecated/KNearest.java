package deprecated;

//by Lisa. Never finished.
import java.util.ArrayList;
abstract class KNearest {
	public ArrayList<dataPoint> data;
	
	
	
	public static class dataPoint{
		private double[] in;
		private double out;
		public dataPoint(double[] in, double out){
			this.setIn(in);this.setOut(out);
		}
		public void setIn(double[] in) {
			this.in = in;
		}
		public double[] getIn() {
			return in;
		}
		public void setOut(double out) {
			this.out = out;
		}
		public double getOut() {
			return out;
		}
	}
}

