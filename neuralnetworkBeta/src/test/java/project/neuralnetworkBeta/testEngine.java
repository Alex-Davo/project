package project.neuralnetworkBeta;

import java.util.Random;

import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.Test;

import project.modifySimpleMatrix.modifySimpleMatrix;

class testEngine {
	Random random = new Random();
	
	@Test
	void testTrainEngine() {
		Engine engine = new Engine();
		engine.add(0, 100, 500);
		engine.add(0, 25);
		engine.add(1, 3);
		engine.setScale(0.01);
		
		CorrectAverages corrAvg = new CorrectAverages(2, 100, (callNumber, averages)->{
			System.out.printf("%d. Loss: %.3f -- Percent correct: %.2f\n", callNumber, averages[0], averages[1]);
		});
		
		
		double learnRate = .05;
		double initRate = learnRate;
		double iter = 1000;
		for (int i = 0; i < iter; ++i) {
			TrainingMatrices tm = modifySimpleMatrix.genTrainingMatrix(500, 3, 32);
			SimpleMatrix inp = tm.getInp();
			SimpleMatrix exp = tm.getOut();
			BatchResults bR = engine.runForwards(inp);
			engine.runBackwards(bR, exp);
			engine.adjust(bR, learnRate);
			engine.evaluate(bR, exp);
			corrAvg.add(bR.getLoss(), bR.getPercentCorrect());
			
			learnRate -= (initRate/iter);
		}
		
		
		//exp.print();
	}
	
	@Test
	void testWeightGradient() {
		SimpleMatrix weights = SimpleMatrix.random_DDRM(5, 4, -2.2, 2.2, random);
		SimpleMatrix inp = modifySimpleMatrix.createInput(4, 1);
		SimpleMatrix exp = modifySimpleMatrix.createExpected(5,1);
		
		SimpleMatrix out = modifySimpleMatrix.softMaxFunct(weights.mult(inp));
		
		SimpleMatrix calcError  = modifySimpleMatrix.subEquilMatrix(out, exp);
		
		SimpleMatrix calcWeightGradients = calcError.mult(inp.transpose());
		
		SimpleMatrix approxWGrad = Approximator.weightGradient(weights, w -> {
			SimpleMatrix output = modifySimpleMatrix.softMaxFunct(w.mult(inp));
			return CrossEntropyFunc.crossEntropy(exp, output);
		});
		assert(calcWeightGradients.isIdentical(approxWGrad, .001));
				
	}
	
	@Test
	void testNetworkEngine() {
		Engine network = new Engine();
		network.add(0, 8, 5);
		network.add(0, 5);
        network.add(1, 4);
        network.setStore(true);
        
        SimpleMatrix input = modifySimpleMatrix.createInput(5, 6);
        SimpleMatrix exp = modifySimpleMatrix.createExpected(4, 6);
        SimpleMatrix approx = Approximator.approximate(input, newMatrix->{
        	BatchResults batchResults = network.runForwards(newMatrix);
			return CrossEntropyFunc.crossEntropy(exp, batchResults.getLastOut());
		});
        
        
        
        BatchResults batchResults = network.runForwards(input);
        network.runBackwards(batchResults, exp);
        
        SimpleMatrix calcError = batchResults.getInputError();
        //approx.print();
        //calcError.print();
        assert(calcError.isIdentical(approx, .001));
        
	}
}
