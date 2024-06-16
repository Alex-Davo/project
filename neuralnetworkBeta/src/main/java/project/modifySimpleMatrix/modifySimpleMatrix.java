package project.modifySimpleMatrix;

import java.util.Random;

import org.ejml.simple.*;

import project.neuralnetworkBeta.TrainingArrays;
import project.neuralnetworkBeta.TrainingMatrices;

public class modifySimpleMatrix{
	private static Random random = new Random();
	
	public static SimpleMatrix copyArray(int inpRows, int cols, double[] inp) {
		SimpleMatrix m = new SimpleMatrix(inpRows, cols);
		int count = 0;
		for(int i = 0; i < inpRows; ++i) {
			for(int j = 0; j < cols; ++j) {
				m.set(i, j, inp[count++]);
			}
		}
		return m;
	}
	
	public static SimpleMatrix genMatrix(int inpRows, int cols, double[] inp) {
		SimpleMatrix m = modifySimpleMatrix.copyArray(cols, inpRows, inp);
		m = m.transpose();
		return m;
		
	}
	
	public static TrainingArrays genTrainingArrays(int inpRows, int outRows, int cols) {
		double[] inp = new double[inpRows * cols];
		double[] out = new double[outRows * cols];
		
		int inpPos = 0;
		int outPos = 0;
		
		for (int i = 0; i < cols; ++i) {
			int rad = random.nextInt(outRows);
			double[] values = new double[inpRows];
			double initRad = 0;
			
			for (int j = 0; j < inpRows; ++j) {
				double value = random.nextGaussian();
				values[j] = value;
				initRad += value * value;
			}
			initRad = Math.sqrt(initRad);
			for(int row = 0; row<inpRows; ++row) {
				inp[inpPos++] = values[row]*rad/initRad;
			}
			out[outPos + rad] = 1;
			outPos+= outRows;
		}
		
		return new TrainingArrays(inp, out);
	}
	
	public static TrainingMatrices genTrainingMatrix(int inpRows, int outRows, int cols) {
		
		TrainingArrays io = genTrainingArrays(inpRows, outRows, cols);
		SimpleMatrix inp = modifySimpleMatrix.genMatrix(inpRows, cols, io.getInp());
		SimpleMatrix out = modifySimpleMatrix.genMatrix(outRows, cols, io.getOut());
		
		return new TrainingMatrices(inp, out);
	}

	public static SimpleMatrix subEquilMatrix(SimpleMatrix m, SimpleMatrix x) {
		SimpleMatrix newM = new SimpleMatrix(m.getNumRows(), m.getNumCols());
		for (int i = 0; i < newM.getNumRows(); ++i) {
			for (int j = 0; j < newM.getNumCols(); ++j) {
				newM.set(i, j, m.get(i, j) - x.get(i, j));
			}
		}
		return newM;
	}
	
	public static SimpleMatrix addBias(SimpleMatrix m, SimpleMatrix bias) {
		for (int i = 0; i < m.getNumCols();i++) {
			m.setColumn(i, m.getColumn(i).plus(bias));
		}
		return m;
	}
	
	public static SimpleMatrix doReluFunc(SimpleMatrix m, SimpleMatrix inp) {
		for (int i = 0; i < m.getNumRows(); ++i) {
			for (int j = 0; j < m.getNumCols(); ++j) {
				m.set(i, j, inp.get(i,j) > 0.0 ? m.get(i, j) : 0.0);
			}
		}
		return m;
	}
	
	public static SimpleMatrix doReluFunc(SimpleMatrix x) {
		SimpleMatrix m = new SimpleMatrix(x);
		for (int i = 0; i < m.getNumRows(); ++i) {
			for (int j = 0; j < m.getNumCols(); ++j) {
				m.set(i, j, m.get(i,j) > 0.0 ? m.get(i, j) : 0.0);
			}
		}
		return m;
	}
	
	public static SimpleMatrix sumColumns(SimpleMatrix m) {
		SimpleMatrix col = new SimpleMatrix(1, m.getNumCols());
		for (int i = 0; i < m.getNumCols(); ++i) {
			col.set(0, i, m.getColumn(i).elementSum());
		}
		return col;
	}
	
	public static SimpleMatrix softMaxFunct(SimpleMatrix m) {
		SimpleMatrix x = new SimpleMatrix(m);
		
		for (int i = 0; i < x.getNumRows(); ++i) {
			for (int j = 0; j < x.getNumCols(); ++j) {
				x.set(i, j, Math.exp(x.get(i, j)));
			}
		}
		SimpleMatrix colSum = modifySimpleMatrix.sumColumns(x);
		for (int j = 0; j < x.getNumCols(); ++j) {
			for (int i = 0; i < x.getNumRows(); ++i) {
				x.set(i, j, x.get(i, j)/colSum.get(j));
			}
		}
		
		return x;
	}
	
	public static SimpleMatrix averageColumn(SimpleMatrix m) {
		SimpleMatrix result = new SimpleMatrix(m.getNumRows(), 1);
		for (int i = 0; i < m.getNumRows(); ++i) {
			for(int j = 0; j < m.getNumCols(); ++j) {
				result.set(i, 0, result.get(i,0) + m.get(i,j)/m.getNumCols());
			}
		}
		return result;
	}
	
	public static SimpleMatrix createInput(int rows, int cols) {
		return SimpleMatrix.random_DDRM(rows, cols, -2.2, 2.2, random);
	}
	
	public static SimpleMatrix createExpected(int rows, int cols) {
		SimpleMatrix expected = SimpleMatrix.filled(rows, cols, 0);
		for(int j = 0; j < cols; ++j) {
			int randomRow = random.nextInt(rows);
			expected.set(randomRow, j, 1);
		}
		return expected;
	}

	public static SimpleMatrix getGreatRow(SimpleMatrix inp) {
		int cols = inp.getNumCols();
		SimpleMatrix result = new SimpleMatrix(1, cols);
		
		double[] great = new double[cols];
		for(int i = 0; i < inp.getNumRows(); ++i) {
			for(int j = 0; j < cols; ++j) {
				if (inp.get(i,j) > great[j]) {
					great[j] = inp.get(i,j);
					result.set(0, j, i);
				}
			}
		}
		
		return result;
	}
	
	public static SimpleMatrix createTrainedExpected(int i, SimpleMatrix inp) {
		SimpleMatrix exp = new SimpleMatrix(i, inp.getNumCols());
		
		SimpleMatrix sumC = modifySimpleMatrix.sumColumns(inp);
		
		for (int j = 0; j < sumC.getNumCols(); ++j) {
			int rowI = (int) (i * (Math.sin(sumC.get(0, j)) + 1.0)/2.0);
			
			exp.set(rowI, j, 1);
		}
		
		return exp;
	}
	
	public static double[] convertToArray(SimpleMatrix m) {
		double[] tempArray = new double[m.getNumRows() * m.getNumCols()];
		for(int i = 0; i < m.getNumRows(); ++i) {
			for(int j = 0; j < m.getNumCols(); ++j) {
				tempArray[i * m.getNumCols() + j] = m.get(i, j);
			}
		}
		return tempArray;
	}
}
