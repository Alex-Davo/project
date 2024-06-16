package project.apps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import project.neuralnetworkBeta.loader.image.*;
import project.neuralnetworkBeta.NeuralNetwork;
import project.neuralnetworkBeta.loader.*;

public class DataApp {
	public static void main(String[] args) {
		if(args.length == 0 || !new File(args[0]).isDirectory()) {
			System.out.println("Usage: [apps] <MNIST Data Directory>");
			return;
		}
		
		String directory = args[0];
		new DataApp().run(directory);
		
	}
	
	private int convertToInt(double[] label, int offset, int oneHotSize) {
		double maxVal = 0;
		int maxInd = 0;
		
		for(int i = 0; i < oneHotSize; ++i) {
			if (label[offset+i] > maxVal) {
				maxVal = label[offset+i];
				maxInd = i;
			}
		}
		return maxInd;
	}
	
	public void run (String directory) {

		final String trainImage = String.format("%s%s%s", directory, File.separator,"train-images-idx3-ubyte");
		final String trainLabel = String.format("%s%s%s", directory, File.separator,"train-labels-idx1-ubyte");
		final String testImage = String.format("%s%s%s", directory, File.separator,"t10k-images-idx3-ubyte");
		final String testLabel = String.format("%s%s%s", directory, File.separator,"t10k-labels-idx1-ubyte");
		
		int batchSize = 900;
		
		ImageLoader trainL = new ImageLoader(trainImage, trainLabel, batchSize);
		ImageLoader testL = new ImageLoader(testImage, testLabel, batchSize);
		
		trainL.open();
		ImageMetaData mD = testL.open();
		
		NeuralNetwork nn = NeuralNetwork.load("nnSave.net");
		
		int iWidth = mD.getWidth();
		int iHeight = mD.getHeight();
		
		int labelSize = mD.getExpSize();
		
		
		
		for(int i = 0; i < mD.getNumBatch(); ++i) {
			BatchData bD = testL.readBatch();
			int numImages = mD.getItemRead();
			int horizImages = (int)Math.sqrt(numImages);
			
			while(numImages % horizImages != 0) {
				++horizImages;
			}
			int vertImages = numImages/horizImages;
			
			int canvWidth = horizImages * iWidth;
			int canvHeight = vertImages * iHeight;
					
			String montagePath = String.format("montage%d.jpg", i);
			BufferedImage montage = new BufferedImage(canvWidth, canvHeight, BufferedImage.TYPE_INT_RGB);
			
			int iSize = iWidth *iHeight;
			
			double [] pixelData = bD.getInpBatch();
			double[] label = bD.getExpBatch();
			
			boolean[] correct = new boolean[numImages];
			
			for(int n = 0; n < numImages; ++n) {
				double[] image = Arrays.copyOfRange(pixelData,n * iSize, (n + 1) * iSize);
				double[] labelVal = Arrays.copyOfRange(label, n * labelSize, (n + 1) * labelSize);
				double[] predictedLabel = nn.predict(image);
				
				int predicted = convertToInt(predictedLabel, 0, labelSize);
				int actual = convertToInt(labelVal, 0, labelSize);
				correct[n] = predicted == actual;
			}
			
			for(int j = 0; j < pixelData.length; ++j) {
				int iNum = j/iSize;
				int pixNum = j % iSize;
				
				int monRow = iNum / horizImages;
				int monCol = iNum % horizImages;
				
				int pixRow = pixNum / iWidth;
				int pixCol = pixNum % iWidth;
				
				int x = monCol * iWidth + pixCol;
				int y = monRow * iHeight + pixRow;
				
				double pixVal = pixelData[j];
				int RGBVal = (int)(0x100 * pixVal);
				int pixRGB = 0;
				
				if (correct[iNum]) {
					pixRGB = (RGBVal << 16) + (RGBVal << 8) + RGBVal;
				} else {
					pixRGB = (RGBVal << 16);
				}

				montage.setRGB(x, y, pixRGB);
			}
			
			try {
				ImageIO.write(montage, "jpg", new File(montagePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			StringBuilder sb = new StringBuilder();
			
			
			for(int labIndex = 0; labIndex < numImages; ++labIndex) {
				if (labIndex % horizImages == 0) {
					sb.append("\n");
				}
						
				int labelVal = convertToInt(label, labIndex * labelSize, mD.getExpSize());
				sb.append(String.format("%d", labelVal));
			}
			String labelPath = String.format("labels%d.txt", i); 
			
			try {
				FileWriter fW = new FileWriter(labelPath);
				fW.write(sb.toString());
				fW.close();
			} catch (IOException e){
				e.printStackTrace();
			}
			
		}
		
		
		trainL.close();
		testL.close();
	}
}
