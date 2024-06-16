package project.neuralnetworkBeta.loader;

public abstract class MetaData {

	int numItems;
	int inpSize;
	int expSize;
	int numBatch;
	int totItemRead;
	int itemRead;
	
	public int getNumItems() {
		return numItems;
	}
	public void setNumItems(int numItems) {
		this.numItems = numItems;
	}
	public int getInpSize() {
		return inpSize;
	}
	public void setInpSize(int inpSize) {
		this.inpSize = inpSize;
	}
	public int getExpSize() {
		return expSize;
	}
	public void setExpSize(int expSize) {
		this.expSize = expSize;
	}
	public int getNumBatch() {
		return numBatch;
	}
	public void setNumBatch(int numBatch) {
		this.numBatch = numBatch;
	}
	public int getTotItemRead() {
		return totItemRead;
	}
	public void setTotItemRead(int totItemRead) {
		this.totItemRead = totItemRead;
	}
	public int getItemRead() {
		return itemRead;
	}
	public void setItemRead(int itemRead) {
		this.itemRead = itemRead;
	}
}
