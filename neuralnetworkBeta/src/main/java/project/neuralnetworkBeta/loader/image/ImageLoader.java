package project.neuralnetworkBeta.loader.image;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import project.neuralnetworkBeta.loader.BatchData;
import project.neuralnetworkBeta.loader.Loader;

public class ImageLoader implements Loader {
	private String imageFileName;
	private String labelFileName;
	private int batchSize;
	
	private ImageMetaData iMD;
	
	private DataInputStream dsImage;
	private DataInputStream dsLabel;
	
	private Lock readL = new ReentrantLock();
	
	public ImageLoader(String imageFileName, String labelFileName, int batchSize) {
		this.imageFileName = imageFileName;
		this.labelFileName = labelFileName;
		this.batchSize = batchSize;
	}
	
	public ImageMetaData getMetaData() {
		return iMD;
	}
	
	public ImageMetaData open() {
		try {
			this.dsImage = new DataInputStream(new FileInputStream(imageFileName));
		} catch (Exception e) {
			throw new LoaderException("Can't open Image file", e);
		}
		try {
			this.dsLabel = new DataInputStream(new FileInputStream(labelFileName));
		} catch (Exception e) {
			throw new LoaderException("Can't open Label file", e);
		}
		iMD = readMetaData();
		return iMD;
	}

	@Override
	public void close() {
		iMD = null;
		try {
			dsImage.close();
		} catch (Exception e) {
			throw new LoaderException("Can't close Image file", e);
		}
		try {
			dsLabel.close();
		} catch (Exception e) {
			throw new LoaderException("Can't close Label file", e);
		}
		
	}
	
	private ImageMetaData readMetaData() {
		iMD = new ImageMetaData();
		int numItems = 0;
		try{
			int magLabelNum = dsLabel.readInt();
			if (magLabelNum != 2049) {
				throw new LoaderException("Label File has wrong format.");
			}
			numItems = dsLabel.readInt();
			iMD.setNumItems(numItems);
			
		} catch (IOException e) {
			throw new LoaderException("Can't read the Label file", e);
		}
		try{
			int magImageNum = dsImage.readInt();
			if (magImageNum != 2051) {
				throw new LoaderException("Image file has wrong format.");
			}
			if (dsImage.readInt() != numItems) {
				throw new LoaderException("Image has different number of items to label");
			}
			int height = dsImage.readInt();
			int width = dsImage.readInt();
			iMD.setHeight(height);
			iMD.setWidth(width);
			iMD.setInpSize(width*height);
			
		} catch (IOException e) {
			throw new LoaderException("Can't read the Image file", e);
		}
		
		iMD.setExpSize(10);
		iMD.setNumBatch((int)Math.ceil((double)numItems)/batchSize);
		return iMD;
	}
	
	@Override
	public ImageMetaData getMD() {
		return iMD;
	}

	@Override
	public BatchData readBatch() {
		readL.lock();
		try {
			ImageBatchData iBD = new ImageBatchData();
			int inpItemRead = readInputBatch(iBD);
			int expItemRead = readExpBatch(iBD);
			
			if (inpItemRead != expItemRead) {
				throw new LoaderException("Issues with amount of Images and Labels");
			}
			
			iMD.setItemRead(inpItemRead);
			return iBD;
		} finally {
			readL.unlock();
		}
	}



	private int readExpBatch(ImageBatchData iBD) {
		try {
			int totItemRead = iMD.getTotItemRead();
			int numItems = iMD.getNumItems();
			
			int readNum = Math.min(numItems-totItemRead, batchSize);
			
			byte[] label = new byte[readNum];
			
			int expSize = iMD.getExpSize();
			
			int actNum = dsLabel.read(label, 0, readNum);
			
			if (actNum != readNum) {
				throw new LoaderException("Mismatched Bytes in label data.");
			}
			double[] data = new double[readNum * expSize];
			for (int i = 0; i < readNum; ++i) {
				byte charL = label[i];
				
				data[i * expSize + charL] = 1;
			}
			iBD.setExpBatch(data);
			
			return readNum;
		} catch(IOException e){
			throw new LoaderException("Couldn't read image data.", e);
		}
		
	}



	private int readInputBatch(ImageBatchData iBD) {
		try {
			int totItemRead = iMD.getTotItemRead();
			int numItems = iMD.getNumItems();
			
			int readNum = Math.min(numItems-totItemRead, batchSize);
			
			int numBytes = readNum * iMD.getInpSize();
			
			byte[] image = new byte[numBytes];
			
			int actRead = dsImage.read(image, 0, numBytes);
			
			if (actRead != numBytes) {
				throw new LoaderException("Mismatched Bytes in image data");
			}
			double[] data = new double[numBytes];
			for (int i = 0; i < numBytes; ++i) {
				data[i] = (image[i] & 0xFF)/256.0;
			}
			iBD.setInpBatch(data);
			return readNum;
		} catch(IOException e){
			throw new LoaderException("Couldn't read image data.", e);
		}
		
	}

}
