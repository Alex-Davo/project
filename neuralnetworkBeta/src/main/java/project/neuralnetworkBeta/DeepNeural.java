package project.neuralnetworkBeta;

import org.ejml.simple.SimpleMatrix;

public class DeepNeural extends NeuralNodes {
	
	private static final long serialVersionUID = 1L;

	public DeepNeural(SimpleMatrix x, SimpleMatrix y, int val) {
		this.weights = x;
		this.biases = y;
		this.val = val;
	}
	
	public int getVal() {
		return val;
	}
	
	SimpleMatrix modify(SimpleMatrix m) {
		m = weights.mult(m);
		for (int i = 0; i < m.getNumCols(); ++i) {
			m.setColumn(i, m.getColumn(i).plus(this.biases));
		}
		return m;
		
	}
	
	SimpleMatrix special(SimpleMatrix m) {
		for (int i = 0; i < m.getNumRows(); ++i) {
			for (int j = 0; j < m.getNumCols(); ++j) {
				m.set(i, j, m.get(i,j) > 0.0 ? m.get(i, j) : 0.0);
			}
		}
		return m;
	}
	
	void doprint() {
		System.out.println("RELU");
		System.out.println("Dense");
	}
}
