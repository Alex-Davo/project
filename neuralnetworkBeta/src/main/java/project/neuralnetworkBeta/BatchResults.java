package project.neuralnetworkBeta;

import java.util.LinkedList;

import org.ejml.simple.*;


public class BatchResults {
	private LinkedList<SimpleMatrix> outputBatch = new LinkedList<>();
	private LinkedList<SimpleMatrix> weightError = new LinkedList<>();
	private LinkedList<SimpleMatrix> weightInp = new LinkedList<>();
	private SimpleMatrix inputError;
	private double loss;
	private double perc;
	
	public LinkedList<SimpleMatrix> getOutput(){
		return outputBatch;
	}
	
	public void addOutput(SimpleMatrix m) {
		outputBatch.add(m);
	}
	
	public SimpleMatrix getLastOut() {
		return outputBatch.getLast();
	}
	
	public LinkedList<SimpleMatrix> getWeightInp() {
		return weightInp;
	}

	public void addWeightInp(SimpleMatrix m) {
		this.weightInp.add(m);
	}

	public LinkedList<SimpleMatrix> getWeightError() {
		return weightError;
	}

	public void addWeightError(SimpleMatrix m) {
		this.weightError.addFirst(m);
	}

	public SimpleMatrix getInputError() {
		return inputError;
	}

	public void setInputError(SimpleMatrix inputError) {
		this.inputError = inputError;
	}
	
	public void setLoss(double loss) {
		this.loss = loss;
	}
	
	public double getLoss() {
		return loss;
	}

	public void setPercentCorrect(double perc) {
		this.perc = perc;
	}
	
	public double getPercentCorrect() {
		return perc;
	}
}
