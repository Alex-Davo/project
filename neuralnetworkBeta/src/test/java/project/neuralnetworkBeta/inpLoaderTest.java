package project.neuralnetworkBeta;

import project.modifySimpleMatrix.modifySimpleMatrix;
import project.neuralnetworkBeta.loader.*;

import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.Test;

class inpLoaderTest {

	@Test
	void test() {
		int batchSize = 33;
		
		Loader tL = new TestLoader(120, batchSize);
		
		MetaData mD = tL.open();
		
		int numItems = mD.getNumItems();
		int lastBatchSize = numItems % batchSize;
		int numBatch = mD.getNumBatch();
		
		for(int i = 0; i < mD.getNumBatch(); ++i) {
			BatchData bD = tL.readBatch();
			
			assert(bD != null);
			int itemRead = mD.getItemRead();
			
			SimpleMatrix inp = modifySimpleMatrix.copyArray(mD.getInpSize(),itemRead, bD.getInpBatch());
			SimpleMatrix exp = modifySimpleMatrix.copyArray(mD.getExpSize(),itemRead, bD.getExpBatch());
			
			assert(inp.elementSum() != 0);
			assert(exp.elementSum() == itemRead);
			
			
			if (i == numBatch - 1) {
				assert(itemRead == lastBatchSize);
				
			} else {
				assert(itemRead == batchSize);
			}
		}
	}

}
