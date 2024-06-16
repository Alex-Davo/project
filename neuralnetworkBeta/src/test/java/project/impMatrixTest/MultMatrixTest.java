package project.impMatrixTest;

import org.junit.jupiter.api.Test;

import project.modifySimpleMatrix.modifySimpleMatrix;

import java.util.Random;

import org.ejml.simple.*;

class MultMatrixTest {
	private Random random = new Random();
	
	@Test
	public void genTATest() {
		double[] x = {1, 2, 3, 4, 5, 6, 7, 8 ,9};
		SimpleMatrix m = modifySimpleMatrix.genMatrix(3, 3, x);
		m.print();
	}
	
	@Test
	public void testGetGreatNum() {
		SimpleMatrix firstMatrix = new SimpleMatrix(
			new double[][] {
				new double[] {1d, 5d, 7d},
			    new double[] {2d, 18d, 3d},
			    new double[] {11d ,7d, 7d}
			}
		);
		SimpleMatrix res = modifySimpleMatrix.getGreatRow(firstMatrix);
		//firstMatrix.print();
		//res.print();
	}
	
	@Test
	public void avgColTest() {
		int rows = 3;
		int cols = 4;
		
		SimpleMatrix m = SimpleMatrix.random_DDRM(rows, cols, -2.2, 2.2, random);
		double avgInd = (cols-1)/2.0;
		
		SimpleMatrix exp = SimpleMatrix.filled(rows, 1, avgInd);
		for (int i = 0; i < exp.getNumRows(); ++i) {
			exp.set(i, 0, i * cols - avgInd);
		}
		//m.print();
		
		SimpleMatrix result = modifySimpleMatrix.averageColumn(m);
		//result.print();
	}
	
	@Test
	public void multTest(){
		SimpleMatrix firstMatrix = new SimpleMatrix(
			new double[][] {
			    new double[] {1d, 5d},
			    new double[] {2d, 3d},
			    new double[] {1d ,7d}
			}
		);
		SimpleMatrix secondMatrix = new SimpleMatrix(
			new double[][] {
				new double[] {1d, 2d, 3d, 7d},
				new double[] {5d, 2d, 8d, 1d}
			}
		);
		SimpleMatrix expected = new SimpleMatrix(
			new double[][] {
			    new double[] {26d, 12d, 43d, 12d},
			    new double[] {17d, 10d, 30d, 17d},
			    new double[] {36d, 16d, 59d, 14d}
			}
		);
		
		
		
		assert(firstMatrix.mult(secondMatrix).isIdentical(expected, 0));
		
	}
	@Test
	public void doubleMultTest() {
		SimpleMatrix actual = new SimpleMatrix(
			new double[][] {
			    new double[] {-1d, 5d},
			    new double[] {2d, -3d},
			    new double[] {-1d ,7d}
			}
		);
		double x = 1.5;
		
		SimpleMatrix expected = new SimpleMatrix(
				new double[][] {
				    new double[] {-1.5d, 7.5d},
				    new double[] {3d, -4.5d},
				    new double[] {-1.5d ,10.5d}
				}
			);
		
		SimpleMatrix notExpected = new SimpleMatrix(
			new double[][] {
				new double[] {-1.5d, 7.5d},
				new double[] {3d, -4.5d},
				new double[] {-2d ,10.5d}
			}
		);
		assert(actual.scale(x).isIdentical(expected, 0));
		assert(!actual.scale(x).isIdentical(notExpected, 0));
		
	}
	
	@Test
	public void matrixAddTest() {
		SimpleMatrix firstMatrix = new SimpleMatrix(
			new double[][] {
				new double[] {1d, 5d},
			    new double[] {2d, 3d},
			    new double[] {1d ,7d}
			}
		);
		SimpleMatrix secondMatrix = new SimpleMatrix(
			new double[][] {
				new double[] {1d, 2d},
				new double[] {5d, 2d},
				new double[] {8d, 1d}
			}
		);
		SimpleMatrix expected = new SimpleMatrix(
			new double[][] {
			    new double[] {2d, 7d},
			    new double[] {7d, 5d},
			    new double[] {9d, 8d}
			}
		);
		assert(firstMatrix.plus(secondMatrix).isIdentical(expected, 0));
		
	}
}