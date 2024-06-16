package project.neuralnetworkBeta;

import org.ejml.simple.*;
import java.util.function.Function;

public class Approximator {
	public static SimpleMatrix approximate(SimpleMatrix inp, Function<SimpleMatrix, SimpleMatrix> change) {
		final double ADDVAL = .00001;
		SimpleMatrix loss = change.apply(inp);
		SimpleMatrix result = new SimpleMatrix(inp.getNumRows(), inp.getNumCols());
		for (int i = 0; i < inp.getNumRows(); ++i) {
			for(int j = 0; j < inp.getNumCols(); ++j) {
				SimpleMatrix m = new SimpleMatrix(inp);
				m.set(i, j, m.get(i,j)+ADDVAL);
				SimpleMatrix loss2 = change.apply(m);
				double rateOfChange = (loss2.get(0, j) - loss.get(0,j))/ADDVAL;
				result.set(i, j, rateOfChange);
			}
		}
		
		return result;
	}
	public static SimpleMatrix weightGradient(SimpleMatrix weight, Function<SimpleMatrix, SimpleMatrix> change) {
		final double ADDVAL = .00001;
		SimpleMatrix loss = change.apply(weight);
		SimpleMatrix result = new SimpleMatrix(weight.getNumRows(), weight.getNumCols());
		for (int i = 0; i < weight.getNumRows(); ++i) {
			for(int j = 0; j < weight.getNumCols(); ++j) {
				SimpleMatrix m = new SimpleMatrix(weight);
				m.set(i, j, m.get(i,j)+ADDVAL);
				SimpleMatrix loss2 = change.apply(m);
				double rateOfChange = (loss2.get(0) - loss.get(0))/ADDVAL;
				result.set(i, j, rateOfChange);
			}
		}
		
		return result;
	}
}
	
	

