package project.neuralnetworkBeta;

import org.ejml.simple.SimpleMatrix;

public class OutNeural extends NeuralNodes{
	
	private static final long serialVersionUID = 1L;

	public OutNeural(SimpleMatrix x, SimpleMatrix y, int val) {
		this.weights = x;
		this.biases = y;
		this.val = val;
	}
	
	public int getVal() {
		return val;
	}
	
	
	private SimpleMatrix sumColumns(SimpleMatrix m) {
		SimpleMatrix col = new SimpleMatrix(1, m.getNumCols());
		for (int i = 0; i < m.getNumCols(); ++i) {
			col.set(0, i, m.getColumn(i).elementSum());
		}
		return col;
	}
	
	private SimpleMatrix softMaxFunct(SimpleMatrix m) {
		SimpleMatrix temp = new SimpleMatrix(m);
		
		for (int i = 0; i < temp.getNumRows(); ++i) {
			for (int j = 0; j < temp.getNumCols(); ++j) {
				temp.set(i, j, Math.exp(temp.get(i, j)));
			}
		}
		SimpleMatrix colSum = this.sumColumns(temp);
		for (int j = 0; j < temp.getNumCols(); ++j) {
			for (int i = 0; i < temp.getNumRows(); ++i) {
				temp.set(i, j, temp.get(i, j)/colSum.get(j));
			}
		}
		
		return temp;
	}
	
	SimpleMatrix modify(SimpleMatrix m) {
		m = weights.mult(m);
		for (int i = 0; i < m.getNumCols(); ++i) {
			m.setColumn(i, m.getColumn(i).plus(biases));
		}
		return m;
	}
	
	SimpleMatrix special(SimpleMatrix m) {
		return softMaxFunct(m);
	}
	
	void doprint() {
		System.out.println("Softmax");
		System.out.println("Dense");
	}
}
