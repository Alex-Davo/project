package project.neuralnetworkBeta;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.ejml.simple.*;

import project.modifySimpleMatrix.modifySimpleMatrix;

public class Engine implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private LinkedList<NeuralNodes> layers = new LinkedList<>();
	private boolean store;
	private int size;
	private double scale = 1;
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	private void setsize(int size) {
		this.size = size;
	}
	public void setStore(boolean store) {
		this.store = store;
	}
	
	public BatchResults runForwards(SimpleMatrix input) {
		BatchResults batchResult = new BatchResults();
		SimpleMatrix m = input;
		batchResult.addOutput(m);
		for(NeuralNodes i: layers) {
			batchResult.addWeightInp(m);
			m = i.modify(m);
			batchResult.addOutput(m);
			m = i.special(m);
			batchResult.addOutput(m);	
		}
		return batchResult;
	}
	
	public void evaluate(BatchResults batchResult, SimpleMatrix exp) {
		double loss = modifySimpleMatrix.averageColumn(CrossEntropyFunc.crossEntropy(exp, batchResult.getLastOut())).get(0);
		batchResult.setLoss(loss);
		
		SimpleMatrix pred = modifySimpleMatrix.getGreatRow(batchResult.getLastOut());
		SimpleMatrix act = modifySimpleMatrix.getGreatRow(exp);
		
		int correct = 0;
		
		for(int i = 0; i < pred.getNumCols(); ++i) {
			if (act.get(0,i) - pred.get(0, i) < .001) {
				++correct;
			}
		}
		double perc = (100 * correct / act.getNumCols());
		batchResult.setPercentCorrect(perc);
	}
	
	public void adjust(BatchResults batchResult, double rateLearn) {
		LinkedList<SimpleMatrix> weightInp = batchResult.getWeightInp();
		LinkedList<SimpleMatrix> weightErr = batchResult.getWeightError();
		
		
		for (int i = 0; i < layers.size(); ++i) {
			SimpleMatrix w = layers.get(i).weights;
			SimpleMatrix b = layers.get(i).biases;
			SimpleMatrix error = weightErr.get(i);
			SimpleMatrix inp = weightInp.get(i);
			
			
			SimpleMatrix weightAdj = error.mult(inp.transpose());
			SimpleMatrix biasAdj = modifySimpleMatrix.averageColumn(error);
			
			double rate = rateLearn/inp.getNumCols();
			
			for (int j = 0; j < w.getNumRows(); ++j) {
				for(int x = 0; x < w.getNumCols(); ++x) {
					w.set(j, x, w.get(j, x) - rate *weightAdj.get(j,x));
				}
			}
			for (int j = 0; j < b.getNumRows(); ++j) {
				b.set(j, 0, b.get(j, 0) - biasAdj.get(j,0));
			}
			
		}
		
	}
	
	public void runBackwards(BatchResults batchResult, SimpleMatrix exp) {
		Iterator<NeuralNodes> layersIter = layers.descendingIterator();
		Iterator<SimpleMatrix> batchIter = batchResult.getOutput().descendingIterator();
		
		SimpleMatrix softM = batchIter.next();
		SimpleMatrix err = modifySimpleMatrix.subEquilMatrix(softM, exp);
		
		while (layersIter.hasNext()) {
			NeuralNodes neuralNode = layersIter.next();
			SimpleMatrix input = batchIter.next();
			if (neuralNode.val == 1) {
				input = batchIter.next();
			}
			else {
				err = modifySimpleMatrix.doReluFunc(err, input);
				input = batchIter.next();
			}
			batchResult.addWeightError(err);
			if (layersIter.hasNext() || store) {
				SimpleMatrix weights = neuralNode.weights;
				err = weights.transpose().mult(err);
			}
		}
		if (store) {
			batchResult.setInputError(err);
		}
	}
	
	
	public void add(int val, double... params) {
		Random random = new Random();
		int numberNeurons = (int)params[0];
		int weightsPer = layers.size() == 0 ? (int)params[1]: this.size;
		if (val == 0) {
			SimpleMatrix weight = SimpleMatrix.random_DDRM(numberNeurons, weightsPer, -2.2*scale, 2.2 *scale, random);
			SimpleMatrix bias = SimpleMatrix.filled(numberNeurons, 1, 0);
			layers.add(new DeepNeural(weight, bias, 0));
			setsize(numberNeurons);
		} else if (val == 1) {
			SimpleMatrix weight = SimpleMatrix.random_DDRM(numberNeurons, weightsPer, -2.2 * scale, 2.2 * scale, random);
			SimpleMatrix bias = SimpleMatrix.filled(numberNeurons, 1, 0);
			layers.add(new OutNeural(weight, bias, 1));
		}
	}
	
}
