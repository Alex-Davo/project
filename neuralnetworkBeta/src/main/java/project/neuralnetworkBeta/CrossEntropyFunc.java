package project.neuralnetworkBeta;

import org.ejml.simple.*;

import project.modifySimpleMatrix.modifySimpleMatrix;

public class CrossEntropyFunc {
	
	public static SimpleMatrix crossEntropy(SimpleMatrix exp, SimpleMatrix act){
		int rows = exp.getNumRows();
		int cols = exp.getNumCols();
		
		SimpleMatrix result = SimpleMatrix.filled(rows, cols, 1);
		for (int i = 0; i < cols; ++i) {
			for(int j = 0; j < rows; ++j) {
				result.set(j, i, -(exp.get(j, i)* Math.log(act.get(j, i))));
			}
		}
		result = modifySimpleMatrix.sumColumns(result);
		return result;
	}
}
