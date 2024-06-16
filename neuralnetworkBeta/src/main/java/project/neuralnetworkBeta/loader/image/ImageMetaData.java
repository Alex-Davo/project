package project.neuralnetworkBeta.loader.image;

import project.neuralnetworkBeta.loader.MetaData;

public class ImageMetaData extends MetaData {
	private int height;
	private int width;
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public void setItemRead(int itemRead) {
		super.setItemRead(itemRead);
		super.setTotItemRead(super.getTotItemRead() + itemRead);
	}
}
