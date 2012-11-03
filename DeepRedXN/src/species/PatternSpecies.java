package species;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import game.Board;
import game.SuperBoard;
/*
 * @author Lisa
 * example usage: 
 * 		new PatternSpecies(new String[]{"pattern1", "pattern2"})
 */
public class PatternSpecies extends Species{
	private Pattern[] patterns;
	private static Board standard; 
	public PatternSpecies(String[] files) throws IOException{
		if(standard==null)standard=new Board(new Scanner(new BufferedReader(new FileReader("classBoard"))));
		patterns = new Pattern[files.length];
		this.init(standard);
		for(int i = 0; i < files.length; i++)patterns[i]=readPattern(files[i]);
	}
	private Pattern readPattern(String file) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(file));
		StringTokenizer st = new StringTokenizer(f.readLine());
		int rows = Integer.parseInt(st.nextToken());
		int cols = Integer.parseInt(st.nextToken());
		int[][] p = new int[rows][cols];
		for(int r= 0; r < rows; r++){
			st = new StringTokenizer(f.readLine());
			for(int c = 0; c < cols; c++)p[r][c] = Integer.parseInt(st.nextToken());
		}
		return new Pattern(p);
	}
	public double[] getInputs(SuperBoard b, int myID, int opID) {
		double[] in = new double[this.inputSize];
		int i= 0;
		for(Pattern p : patterns)i = p.fill(in,i, myID, opID, b);
		return in;
		
	}
	private void init(Board b) {
		this.inputSize=0;
		for(Pattern p : patterns){
			p.getPossibleStartLocs(b);
			this.inputSize+= p.getPossibleStartLocs().size();
		}
		this.hiddenLayers= new int[this.inputSize*2];
		this.outputSize=1;
	}
}
