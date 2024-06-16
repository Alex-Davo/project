package project.neuralnetworkBeta;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.ejml.simple.SimpleMatrix;

import project.modifySimpleMatrix.modifySimpleMatrix;
import project.neuralnetworkBeta.loader.*;

public class NeuralNetwork implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Engine engine;
	
	private int epoch = 15;
	private double learnRate;
	private double initLearnRate = .05;
	
	transient private double finLearnRate = 0.01;
	transient private Object lock = new Object();
	
	private int threads = 16;
 	
	public NeuralNetwork() {
		engine = new Engine();
	}
	
	public void setScale(double scale) {
		engine.setScale(scale);
	}
	
	public void setThreads(int threads) {
		this.threads = threads;
	}
	
	public void setLearn(double x, double y) {
		this.initLearnRate = x;
		this.finLearnRate = y;
	}
	
	public void setEpoch(int x) {
		this.epoch = x;
	}
	
	public void fit(Loader trainLoader, Loader evalLoader) {
		learnRate = initLearnRate;
		
		for(int i = 1; i <= epoch; ++i) {
			System.out.printf("Epoch %3d ", i);
			
			runEpoch(trainLoader, true);
			
			if (evalLoader != null) {
				runEpoch(evalLoader, false);
			}
			
			System.out.println();
			
			learnRate -= (initLearnRate - finLearnRate)/epoch;
		}
	}
	
	private void runEpoch(Loader tLoad, boolean backProp) {
		tLoad.open();
		LinkedList<Future<BatchResults>> queue = createBTasks(tLoad, backProp);
		consumeBTasks(queue, backProp);
		tLoad.close();
	}

	private void consumeBTasks(LinkedList<Future<BatchResults>> queue, boolean backProp) {
		int numBatch = queue.size();
		int index = 0;
		double avgLoss = 0;
		double avgPercCorr = 0;
		for (Future<BatchResults> batch : queue) {
			try {
				BatchResults bR = batch.get();
				
				if (!backProp) {
					avgLoss += bR.getLoss();
					avgPercCorr += bR.getPercentCorrect();
				}
			} catch (Exception e) {
				throw new RuntimeException("Execution Error: ", e);
			}
			int printDash = numBatch/30;
			
			if (backProp && index++ % printDash == 0) {
				System.out.print("-");
			}
		}
		
		if (!backProp) {
			avgLoss /= queue.size();
			avgPercCorr /= queue.size();
			
			System.out.printf("Loss: %3f; Percent Correct: %3f", avgLoss, avgPercCorr);
		}
		
	}
	
	private LinkedList<Future<BatchResults>>  createBTasks(Loader tLoad, boolean backProp) {
		LinkedList<Future<BatchResults>> batches = new LinkedList<>();
		
		MetaData mD = tLoad.getMD();
		int numBatch = mD.getNumBatch();
		
		ExecutorService exe = Executors.newFixedThreadPool(threads);
		
		for(int i = 0; i < numBatch; ++i) {
			batches.add(exe.submit(()->runBatch(tLoad, backProp)));
		}
		
		exe.shutdown();
		
		return batches;
	}

	private BatchResults runBatch(Loader tLoad, boolean backProp) {
		MetaData mD = tLoad.getMetaData();
		BatchData bD = tLoad.readBatch();

		int itemRead = mD.getItemRead();
		
		SimpleMatrix inp = modifySimpleMatrix.genMatrix(mD.getInpSize(),itemRead, bD.getInpBatch());
		SimpleMatrix exp = modifySimpleMatrix.genMatrix(mD.getExpSize(),itemRead, bD.getExpBatch());
		
		
		BatchResults bR = engine.runForwards(inp);
		
		if (backProp) {
			engine.runBackwards(bR, exp);
			synchronized(lock) {
				engine.adjust(bR, learnRate);
			}
		} else {
			engine.evaluate(bR, exp);
		}
		
		return bR;
	}
	
	public void add(int val, double...params) {
		engine.add(val, params);
	}

	public boolean save(String filename) {
		try(ObjectOutputStream ds = new ObjectOutputStream(new FileOutputStream(filename))){
			ds.writeObject(this);
		} catch (IOException e) {
			System.out.println("Unable to save neural network");
		}
		
		
		return true;
	}
	public static NeuralNetwork load(String filename) {
		NeuralNetwork newNet = null;
		try(ObjectInputStream ds = new ObjectInputStream(new FileInputStream(filename))){
			newNet = (NeuralNetwork)ds.readObject();
		} catch (Exception e) {
			System.out.println("Unable to load neural network");
		}
		return newNet;
	}
	
	public Object readResolve() {
		this.lock = new Object();
		return this;
	}
	
	public double[] predict(double[] input) {
		SimpleMatrix inp = modifySimpleMatrix.copyArray(input.length, 1, input);
		BatchResults bR = engine.runForwards(inp);
		return modifySimpleMatrix.convertToArray(bR.getLastOut());
	}
}
