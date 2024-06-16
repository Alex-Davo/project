package project.impMatrixTest;



import org.junit.jupiter.api.Test;
import org.ejml.simple.*;

class matrixSpeedTest {

	@Test
	public void speedTest() {
		SimpleMatrix firstMatrix = SimpleMatrix.filled(500, 50, 12);
		SimpleMatrix secondMatrix = SimpleMatrix.filled(50, 500, 10);
		
		
		double start = System.currentTimeMillis();
		
		for (int i = 0; i < firstMatrix.getNumCols(); ++i) {
			for(int j = 0; j < firstMatrix.getNumRows(); ++j) {
				firstMatrix.get(j, i);
			}
		}
		
		double end = System.currentTimeMillis();
		System.out.printf("%fms", end-start);
		assert(true);
	}

}
