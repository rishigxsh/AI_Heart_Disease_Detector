package team5_imr;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import java.util.ArrayList;

public class logReg {

    static {
        try {
            // Load the OpenCV native library
            System.load(new java.io.File("bin/src/main/resources/lib/opencv_java410.dll").getAbsolutePath());
            System.out.println("OpenCV library loaded successfully.");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Failed to load OpenCV library.");
            e.printStackTrace();
        }
    }

    // Preprocess the image: resize, normalize, and flatten into a feature array
    // Re-write this functionality to accomodate an image file rather than a filepath.
        //Solution: Pass image in as a byte array.
    public static double[] preprocessImage(byte[] imageBytes) {

        MatOfByte matBytes = new MatOfByte(imageBytes);

        Mat image = Imgcodecs.imdecode(matBytes, Imgcodecs.IMREAD_GRAYSCALE);
        if (image.empty()) {
            System.out.println("Could not load image");
            return null;
        }
        
        //Should be able to use the rest of this function without altering it.

        // Resize to 32x32
        Imgproc.resize(image, image, new Size(32, 32));

        // Normalize to 0-1
        Mat normalizedImage = new Mat();
        image.convertTo(normalizedImage, CvType.CV_32F, 1.0 / 255.0);

        // Flatten into a 1D array
        int rows = normalizedImage.rows();
        int cols = normalizedImage.cols();
        double[] features = new double[rows * cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                features[row * cols + col] = normalizedImage.get(row, col)[0];
            }
        }

        System.out.println("Image preprocessed successfully.");
        return features;
    }

    // Create the Weka dataset structure
    //Figure out if this function will need to be rewritten
    public static Instances createDatasetStructure(int featureLength) {
        System.out.println("Creating dataset structure...");

        ArrayList<Attribute> attributes = new ArrayList<>();
        for (int i = 0; i < featureLength; i++) {
            attributes.add(new Attribute("pixel" + i));
        }

        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("No Heart Disease"); // Class0
        classValues.add("Heart Disease");   // Class1
        attributes.add(new Attribute("class", classValues)); // Add class attribute

        Instances dataset = new Instances("ImageDataset", attributes, 0);
        dataset.setClassIndex(dataset.numAttributes() - 1); // Last attribute is the class label

        System.out.println("Dataset structure created successfully.");
        return dataset;
    }

    //Rewrite to accommodate an image instead of a filepath
    public String classifyImage(String modelPath, byte[] imageBytes) {
        try {
            System.out.println("Loading model from: " + modelPath);
            Logistic model = (Logistic) SerializationHelper.read(modelPath);

            // Preprocess the image
            System.out.println("Classifying image");
            double[] features = preprocessImage(imageBytes);
            if (features == null) {
                System.out.println("Image could not be processed.");
                return null;
            }

            // Create dataset structure
            Instances dataset = createDatasetStructure(features.length);
            Instance instance = new DenseInstance(1.0, features);
            instance.setDataset(dataset);

            // Classify the instance
            double result = model.classifyInstance(instance);
            String prediction = dataset.classAttribute().value((int) result);
            System.out.println("Predicted class: " + prediction);
            return prediction;
        } catch (Exception e) {
            System.out.println("An error occurred during classification:");
            e.printStackTrace();
            return null;
        }
    }
}
