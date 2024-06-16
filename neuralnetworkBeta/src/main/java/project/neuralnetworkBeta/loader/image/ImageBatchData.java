package project.neuralnetworkBeta.loader.image;

import project.neuralnetworkBeta.loader.BatchData;

public class ImageBatchData extends BatchData {

	@Override
	public double[] getInpBatch() {
		return this.inpBatch;
	}

	@Override
	public void setInpBatch(double[] inpBatch) {
		this.inpBatch = inpBatch;

	}

	@Override
	public double[] getExpBatch() {
		return this.expBatch;
	}

	@Override
	public void setExpBatch(double[] expBatch) {
		this.expBatch = expBatch;

	}

}
