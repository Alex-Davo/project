package project.neuralnetworkBeta;

import java.util.stream.DoubleStream;

public class CorrectAverages {
	
	private int calls = 0;
	private double[][] vals;
	private Callback callback;
	private int pos = 0;
	
	public interface Callback {
		public void apply(int callNum, double[] averages);
			
	}
	
	public CorrectAverages(int numAvg, int windowSize, Callback callback) {
		this.callback = callback;
		vals = new double[numAvg][windowSize];
		
	}
	
	public void add(double...args) {
		for (int i = 0; i < vals.length; ++i) {
			vals[i][pos] = args[i];
		}
		if (++pos == vals[0].length) {
			double[] averages = new double [vals.length];
			for(int i = 0; i < vals.length; ++i) {
				averages[i] = DoubleStream.of(vals[i]).average().getAsDouble();
			}
			
			callback.apply(++calls, averages);
			
			pos = 0;
			
		}
	}
}
