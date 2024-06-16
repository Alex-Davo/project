package project.neuralnetworkBeta;

public class TrainingArrays {
	private double[] inp;
	private double[] out;
	
	public TrainingArrays(double[] inp, double[] out) {
		this.inp = inp;
		this.out = out;
	}

	public double[] getInp() {
		return inp;
	}
	public double[] getOut() {
		return out;
	}
	
}
