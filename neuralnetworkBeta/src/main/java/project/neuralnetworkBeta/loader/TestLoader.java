package project.neuralnetworkBeta.loader;

import project.modifySimpleMatrix.modifySimpleMatrix;
import project.neuralnetworkBeta.TrainingArrays;

public class TestLoader implements Loader{
	private MetaData metaData;
	
	private int numItems;
	private int inpSize = 500;
	private int expSize = 10;
	private int numBatch;
	private int batchSize;
	
	private int totItemRead;
	private int itemRead;
	
	public TestLoader(int numItems, int batchSize) {
		this.numItems = numItems;
		this.batchSize = batchSize;
		this.metaData = new TestMetaData();
		
		metaData.setNumItems(numItems);
		
		numBatch = numItems/batchSize;
		if (numItems % batchSize != 0) {
			numBatch += 1;
		}
		metaData.setNumBatch(numBatch);
		metaData.setInpSize(inpSize);
		metaData.setExpSize(expSize);
	}
	
	public MetaData open() {
		return this.metaData;
	}
	public void close() {
		totItemRead = 0;
	}
	
	public MetaData getMD() {
		return metaData;
	}
	public synchronized BatchData readBatch() {
		if (totItemRead == numItems) {
			return null;
		}
		itemRead = batchSize;
		totItemRead += itemRead;
		int excItems = totItemRead - numItems;
		if (excItems > 0) {
			totItemRead -= excItems;
			itemRead -= excItems;
		}
		TrainingArrays io = modifySimpleMatrix.genTrainingArrays(inpSize, expSize, itemRead);
		TestBatchData bD = new TestBatchData();
		
		bD.setInpBatch(io.getInp());
		bD.setExpBatch(io.getOut());

		metaData.setItemRead(itemRead);
		metaData.setTotItemRead(totItemRead);
		
		return bD;
	}

	@Override
	public MetaData getMetaData() {
		// TODO Auto-generated method stub
		return null;
	}
}
