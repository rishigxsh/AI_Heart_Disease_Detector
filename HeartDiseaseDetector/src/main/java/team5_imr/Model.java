package team5_imr;

import weka.core.converters.CSVLoader;
import weka.core.Instances;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Resample;
import weka.classifiers.Evaluation;
import weka.core.SerializationHelper;
import java.io.File;
import java.util.Random;

public class Model {
	
	public int useModel(double[] inputValues) {
		 try {
	        // Check if the input array has the correct length
	        if (inputValues.length != 4) {
	            throw new IllegalArgumentException("Input array must have exactly 4 values.");
	        }

	        // Load the trained model
	        String modelFileName = "src/main/resources/mlp_model.model";
	        MultilayerPerceptron mlp = (MultilayerPerceptron) SerializationHelper.read(modelFileName);

	        // Create an empty Instances object with the same structure as the training data
	        CSVLoader loader = new CSVLoader();
	        loader.setSource(new File("src/main/resources/heart.csv"));
	        Instances dataStructure = loader.getDataSet();
	        
	        // Set the class index
	        dataStructure.setClassIndex(4);

	        // Ensure the target column is nominal
	        if (dataStructure.classAttribute().isNumeric()) {
	            weka.filters.unsupervised.attribute.NumericToNominal numericToNominal = new weka.filters.unsupervised.attribute.NumericToNominal();
	            numericToNominal.setOptions(new String[]{"-R", "5"}); // Convert 5th column to nominal
	            numericToNominal.setInputFormat(dataStructure);
	            dataStructure = Filter.useFilter(dataStructure, numericToNominal);
	        }

	        // Create a new Instance object for the input values
	        Instance instance = new DenseInstance(dataStructure.numAttributes());
	        instance.setDataset(dataStructure);

	        // Assign the input values to the instance
	        for (int i = 0; i < inputValues.length; i++) {
	            instance.setValue(i, inputValues[i]); // Assign values to the first 4 attributes
	        }

	        // Use the model to predict the class
	        double predictedClass = mlp.classifyInstance(instance);
	       
	        
	        return (int) predictedClass;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

	
	public void trainModel() {
	    try {
	        // Load CSV file
	        CSVLoader loader = new CSVLoader();
	        loader.setSource(new File("src/main/resources/heart.csv"));
	        Instances data = loader.getDataSet();
	        
	        // Set the class index (target column)
	        data.setClassIndex(4);

	        // Verify class distribution and type
	        System.out.println("Class Attribute Type: " + (data.classAttribute().isNominal() ? "Nominal" : "Numeric"));
	        System.out.println("Class Values: " + data.classAttribute());
	        int[] classCounts = data.attributeStats(data.classIndex()).nominalCounts;
	        System.out.println("Class Distribution: " + java.util.Arrays.toString(classCounts));
	        
	        // Ensure the target column is nominal
	        if (data.classAttribute().isNumeric()) {
	            weka.filters.unsupervised.attribute.NumericToNominal numericToNominal = new weka.filters.unsupervised.attribute.NumericToNominal();
	            numericToNominal.setOptions(new String[]{"-R", "5"}); // Convert 5th column to nominal
	            numericToNominal.setInputFormat(data);
	            data = Filter.useFilter(data, numericToNominal);
	        }


	        // Split data into training and test sets (80%-20%)
	        Resample resample = new Resample();
	        resample.setOptions(new String[]{"-Z", "80"}); // 80% for training
	        resample.setInputFormat(data);

	        Instances trainingData = Filter.useFilter(data, resample);
	        Instances testData = new Instances(data);
	        for (int i = 0; i < trainingData.numInstances(); i++) {
	            testData.delete(0);
	        }
	        testData.setClassIndex(data.classIndex());

	        System.out.println("Training Data Size: " + trainingData.numInstances());
	        System.out.println("Test Data Size: " + testData.numInstances());

	        // Create and configure the neural network
	        MultilayerPerceptron mlp = new MultilayerPerceptron();
	        mlp.setLearningRate(0.3);
	        mlp.setMomentum(0.2);
	        mlp.setTrainingTime(5000);
	        mlp.setHiddenLayers("128, 64, 32");

	        // Train the neural network
	        mlp.buildClassifier(trainingData);

	        // Save the model to a file
	        SerializationHelper.write("mlp_model.model", mlp);
	        System.out.println("Model saved to: mlp_model.model");

	        // Evaluate the model on the test set
	        Evaluation eval = new Evaluation(trainingData);
	        eval.evaluateModel(mlp, testData);

	        // Print evaluation metrics
	        System.out.println("\nEvaluation Metrics:");
	        System.out.println("Accuracy: " + eval.pctCorrect() + "%");
	        System.out.println("Confusion Matrix: ");
	        double[][] confusionMatrix = eval.confusionMatrix();
	        for (double[] row : confusionMatrix) {
	            System.out.println(java.util.Arrays.toString(row));
	        }

	        // Output predictions with probabilities
	        System.out.println("\nPredictions:");
	        for (int i = 0; i < testData.numInstances(); i++) {
	            Instance instance = testData.instance(i);

	            if (!instance.classIsMissing()) {
	                double actualClass = instance.classValue();
	                double predictedClass = mlp.classifyInstance(instance);
	                double[] distribution = mlp.distributionForInstance(instance);

	                System.out.println("Instance " + (i + 1) + ": Actual = " +
	                        instance.classAttribute().value((int) actualClass) +
	                        ", Predicted = " +
	                        instance.classAttribute().value((int) predictedClass) +
	                        ", Probabilities = [Class 0: " + distribution[0] +
	                        ", Class 1: " + distribution[1] + "]");
	            } else {
	                System.out.println("Missing class value for instance " + (i + 1));
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
