package project.neuralnetworkBeta;

import org.ejml.simple.SimpleMatrix;

public class TrainingMatrices {
	private SimpleMatrix inp;
	private SimpleMatrix out;
	
	public TrainingMatrices(SimpleMatrix inp, SimpleMatrix out) {
		this.inp = inp;
		this.out = out;
	}

	public SimpleMatrix getInp() {
		return inp;
	}
	public SimpleMatrix getOut() {
		return out;
	}
	
}
