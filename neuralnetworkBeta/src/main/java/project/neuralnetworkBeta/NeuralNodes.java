package project.neuralnetworkBeta;

import java.io.Serializable;

import org.ejml.simple.*;

abstract class NeuralNodes implements Serializable{
	private static final long serialVersionUID = 1L;
	SimpleMatrix weights;
	SimpleMatrix biases;
	int val;
	
	abstract int getVal();
	
	abstract SimpleMatrix modify(SimpleMatrix m);
	
	abstract SimpleMatrix special(SimpleMatrix m);
	
	abstract void doprint();
}

