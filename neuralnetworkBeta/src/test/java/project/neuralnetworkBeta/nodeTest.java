package project.neuralnetworkBeta;

import org.junit.jupiter.api.Test;

import project.modifySimpleMatrix.modifySimpleMatrix;

import java.util.Random;
import org.ejml.simple.*;


class nodeTest {
	private Random random = new Random();
	
	@Test
	void testEntropy() {
		SimpleMatrix expected = new SimpleMatrix(
			new double[][] {
			    new double[] {1, 0, 0},
			    new double[] {0, 0, 1},
			    new double[] {0, 1, 0}
			}
		);
		
		SimpleMatrix actual = new SimpleMatrix(
			new double[][] {
			    new double[] {.05*(1*1), .05*(2*2), .05*(3*3)},
			    new double[] {.05*(4*4), .05*(5*5), .05*(6*6)},
			    new double[] {.05*(7*7), .05*(8*8), .05*(9*9)}
			}
		);
		actual = modifySimpleMatrix.softMaxFunct(actual);
		//actual.print();
		
		SimpleMatrix m = CrossEntropyFunc.crossEntropy(expected, actual);
		//m.print();
		
		for (int i = 0; i < actual.getNumCols(); ++i) {
			for (int j = 0; j < actual.getNumRows(); ++j) {
				double loss = m.get(0, i);
				if (expected.get(j, i) > .9) {
					assert(Math.abs(Math.log(actual.get(j,i))+loss) < .001);
				}
			}
		}
		
	}
	
	@Test
	void testAddBias() {
		SimpleMatrix inp = new SimpleMatrix(
			new double[][] {
			    new double[] {1, 2, 3},
			    new double[] {4, 5, 6},
			    new double[] {7, 8, 9}
			}
		);
		SimpleMatrix weights = new SimpleMatrix(
			new double[][] {
			    new double[] {1, 2, 3},
			    new double[] {4, 5, 6},
			    new double[] {7, 8, 9}
			}
		);
		
		SimpleMatrix bias = new SimpleMatrix(
			new double[][] {
			    new double[] {1},
			    new double[] {2},
			    new double[] {3}
			}
		);

		SimpleMatrix Res = weights.mult(inp);
		modifySimpleMatrix.addBias(Res, bias);
		SimpleMatrix expected = new SimpleMatrix(
			new double[][] {
			    new double[] {31, 37, 43},
			    new double[] {68, 83, 98},
			    new double[] {105, 129, 153}
			}
		);

		assert(Res.isIdentical(expected, 0));
	}
	
	@Test
	void testReLuFunction() {
		
		final int numberNeurons = 5;
		final int numberInputs = 6;
		final int inputSize = 4;
		
		SimpleMatrix inp = SimpleMatrix.random_DDRM(inputSize, numberInputs, 0, 1, random);
		SimpleMatrix weights = SimpleMatrix.random_DDRM(numberNeurons, inputSize, -3.0, 3.0, random);
		SimpleMatrix bias = SimpleMatrix.random_DDRM(numberNeurons, 1, -1.0, 1.0, random);

		SimpleMatrix Res = weights.mult(inp);
		for (int i = 0; i < Res.getNumCols();i++) {
			Res.setColumn(i, Res.getColumn(i).plus(bias));
		}
		SimpleMatrix Res2 = new SimpleMatrix(Res);
		
		Res = modifySimpleMatrix.doReluFunc(Res);
		
		for (int i = 0; i < Res.getNumRows(); ++i) {
			for (int j = 0; j < Res.getNumCols(); ++j) {
				if (Res2.get(i, j) > 0) {
					assert(Res.get(i, j) == Res2.get(i, j));
				}
				else if (Res2.get(i, j) <= 0) {
					assert(Res.get(i, j) == 0.0);
				}
			}
		}
	}
	
	@Test
	public void backPropTest() {
		SimpleMatrix m = SimpleMatrix.random_DDRM(4, 5, -2.2, 2.2, random);
		SimpleMatrix exp = SimpleMatrix.filled(4, 5, 0);
		
		SimpleMatrix w = SimpleMatrix.random_DDRM(4, 4, -2.2, 2.2, random);
		SimpleMatrix b = SimpleMatrix.random_DDRM(4, 1, -2.2, 2.2, random);
		
		for(int j = 0; j < exp.getNumCols(); ++j) {
			int randomRow = random.nextInt(4);
			exp.set(randomRow, j, 1);
		}
		
		
		SimpleMatrix newInp =  new SimpleMatrix(m);
		m = modifySimpleMatrix.doReluFunc(m);
		m = modifySimpleMatrix.addBias(w.mult(m), b);
		SimpleMatrix softM = modifySimpleMatrix.softMaxFunct(m);
		SimpleMatrix approx = Approximator.approximate(newInp, newMatrix->{
			SimpleMatrix temp = modifySimpleMatrix.doReluFunc(newMatrix);
			temp = modifySimpleMatrix.addBias(w.mult(temp), b);
			return CrossEntropyFunc.crossEntropy(exp, modifySimpleMatrix.softMaxFunct(temp));
		});
		
		SimpleMatrix calc = new SimpleMatrix(m.getNumRows(), m.getNumCols());
		
		for (int i = 0; i < m.getNumRows(); ++i) {
			for(int j = 0; j < m.getNumCols(); ++j) {
				calc.set(i, j, softM.get(i,j)-exp.get(i,j));
			}
		}
		
		calc = w.transpose().mult(calc);
		calc = modifySimpleMatrix.doReluFunc(calc, newInp);
		//calc.print();
		//approx.print();
		assert(calc.isIdentical(approx, .1));
	}
	
	@Test
	public void sumColTest() {
		SimpleMatrix inp = new SimpleMatrix(
			new double[][] {
			    new double[] {1, 2, 3},
			    new double[] {4, 5, 6},
			    new double[] {7, 8, 9}
			}
		);
		SimpleMatrix x = modifySimpleMatrix.sumColumns(inp);

		for(int i = 0; i < inp.getNumCols(); ++i) {
			assert(x.get(0,i) == inp.getColumn(i).elementSum());
		}
	}
	@Test
	public void softMaxTest() {
		SimpleMatrix inp = SimpleMatrix.random_DDRM(5, 8, -3.0, 3.0, random);
		SimpleMatrix x = modifySimpleMatrix.softMaxFunct(inp);
		
		x = modifySimpleMatrix.sumColumns(x);
		for (int i = 0; i < x.getNumCols(); ++i) {
			assert(x.get(0, i) - 1.0 <= .00001);
		}
	}
	@Test
	public void tempTest() {
		int inputSize = 5;
		int layer1Size = 6;
		int layer2Size = 8;
		
		SimpleMatrix inp = SimpleMatrix.random_DDRM(inputSize, 1, -2.0, 2.0, random);
		SimpleMatrix weightsOne = SimpleMatrix.random_DDRM(layer1Size, inp.getNumRows(), -3.0, 3.0, random);
		SimpleMatrix biasOne = SimpleMatrix.random_DDRM(layer1Size, 1, -2.0, 2.0, random);
		
		SimpleMatrix weightsTwo = SimpleMatrix.random_DDRM(layer2Size, weightsOne.getNumRows(), -3.0, 3.0, random);
		SimpleMatrix biasTwo = SimpleMatrix.random_DDRM(layer2Size, 1, -2.0, 2.0, random);
		
		SimpleMatrix out = weightsOne.mult(inp);
		
		out = modifySimpleMatrix.addBias(out, biasOne);
		out = modifySimpleMatrix.doReluFunc(out);
		//out.print();
		
		out = weightsTwo.mult(out);
		
		out = modifySimpleMatrix.addBias(out, biasTwo);
		out = modifySimpleMatrix.doReluFunc(out);
		
		//out.print();
		out = modifySimpleMatrix.softMaxFunct(out);
		//out.print();
	}
}
