package project.neuralnetworkBeta.loader;

public class TestBatchData extends BatchData {
	private double[] inpBatch;
	private double[] expBatch;
	
	public double[] getInpBatch() {
		return inpBatch;
	}
	public void setInpBatch(double[] inpBatch) {
		this.inpBatch = inpBatch;
	}
	public double[] getExpBatch() {
		return expBatch;
	}
	public void setExpBatch(double[] expBatch) {
		this.expBatch = expBatch;
	}
	
}
