package project.neuralnetworkBeta.loader;

public interface Loader {

	MetaData open();
	void close();

	MetaData getMD();
	BatchData readBatch();
	MetaData getMetaData();
}
