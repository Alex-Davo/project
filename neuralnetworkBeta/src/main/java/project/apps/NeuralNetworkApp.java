package project.apps;

import project.neuralnetworkBeta.loader.image.ImageLoader;
import project.neuralnetworkBeta.loader.image.ImageMetaData;

import java.io.File;

import project.neuralnetworkBeta.*;

public class NeuralNetworkApp 
{
    public static void main(String[] args) {
    	
    	final String filename = "nnSave.net";		
		if(args.length == 0 || !new File(args[0]).isDirectory()) {
			System.out.println("Usage: [apps] <MNIST Data Directory>");
			return;
		}
		
		String directory = args[0];
		final String trainImage = String.format("%s%s%s", directory, File.separator,"train-images-idx3-ubyte");
		final String trainLabel = String.format("%s%s%s", directory, File.separator,"train-labels-idx1-ubyte");
		final String testImage = String.format("%s%s%s", directory, File.separator,"t10k-images-idx3-ubyte");
		final String testLabel = String.format("%s%s%s", directory, File.separator,"t10k-labels-idx1-ubyte");
		
		int batchSize = 900;
		
		ImageLoader trainL = new ImageLoader(trainImage, trainLabel, batchSize);
		ImageLoader testL = new ImageLoader(testImage, testLabel, batchSize);
		
		ImageMetaData iMD = trainL.open();
		int inpSize = iMD.getInpSize();
		int outSize = iMD.getExpSize();
		
		NeuralNetwork nn = NeuralNetwork.load(filename);
	     
	    if (nn == null) {
	    	nn = new NeuralNetwork();
	    	System.out.println("Couldn't load NeuralNetwork. Creating new one.");
	    	nn.setEpoch(100);
	        nn.setLearn(.05, 0.001);
	        nn.setThreads(8);
	        nn.setScale(.2);

	        nn.add(0, 128, inpSize);
	 		nn.add(1, outSize);
	    }
	    nn.fit(trainL, testL);
	     
	    if(nn.save(filename)) {
	    	System.out.println("Saved to: " + filename);
	    }
	    return;
    }
}
