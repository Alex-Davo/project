package project.gradientCalc;

import java.util.function.DoubleFunction;

public class Calculus {
	private static final double INC = .000001;
	
	public static double func1(double x) {
		return 2.3 * x + 12;
	}
	public static double func2(double x) {
		return x*x;
	}
	
	public static double differentiate(DoubleFunction<Double> func, double x) {
		double output1 = func.apply(x);
		double output2 = func.apply(x+INC);
		
		return (output2-output1)/INC;
	}
	
	public static void main(String[] args) {
		return;

	}
}
