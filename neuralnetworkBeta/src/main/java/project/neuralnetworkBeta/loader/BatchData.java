package project.neuralnetworkBeta.loader;

public abstract class BatchData {
	public double[] inpBatch;
	public double[] expBatch;
	
	public abstract double[] getInpBatch();

	public abstract void setInpBatch(double[] inpBatch);

	public abstract double[] getExpBatch();

	public abstract void setExpBatch(double[] expBatch);
}
