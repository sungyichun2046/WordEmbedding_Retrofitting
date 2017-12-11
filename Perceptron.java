import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Integer;
import java.lang.Double;

//

/**
 * Basic Perceptron Object.
 * An abstract representation of a binary perceptron classifier,
 *
 */

public class Perceptron {
///////////////////////////////////////////////////////////////////////////////
// DEFINITION //
///////////////////////////////////////////////////////////////////////////////

    ArrayList<HashMap<Double[], Integer>> trainingSet;
    ArrayList<HashMap<Double[], Integer>> testSet;
    ArrayList<HashMap<Double[], Integer>> developmentSet;
    protected int FEATURES_SIZE;
    protected int EPOCHS;
    protected int MAX_ITERATIONS = 2000;
    Double[] weights;

    /**
     * setParameters Method
     * Method to set parameters required to learn a PERCEPTRON
     **/
    public void setParameters(int numberOfFeatures, int epochs) {
        this.FEATURES_SIZE = numberOfFeatures;
        this.EPOCHS = epochs;
    }

    /**
     * setFeatureSize Method
     * Method to set the size of features required for a PERCEPTRON
     **/
    public void setFeatureSize(int numberOfFeatures) {
        this.FEATURES_SIZE = numberOfFeatures;
    }

    /**
     * setNumberOfEpochs Method
     * Method to set the number of epochs required for a PERCEPTRON
     **/
    public void setNumberOfEpochs(int epochs) {
        this.EPOCHS = epochs;
    }

    /**
     * setWeights Method
     * Method to set weights required to learn a PERCEPTRON
     **/
    public void setWeights(Double[] w) {
        this.weights = w;
    }

    /**
     * getWeights Method
     * Method to get weights data assigned to the current instance of a PERCEPTRON
     **/
    public Double[] getWeights() {
        return this.weights;
    }

    /**
     * loadDataSet Method
     * Method to load the data required to learn a Perceptron, the order required of the sets to lean this
     * instance of the classifier is Training, Test, Development and finally the WordEmbedding representation instance
     * in order to vectorize the data 
     **/
    public void loadDataSet(String training, String test, String development, WordEmbedding we) throws IOException {
        try {
            DataLoader data = new DataLoader();
            this.trainingSet = data.vectorizeTextData(training, we);
            this.testSet = data.vectorizeTextData(test, we);
            this.developmentSet = data.vectorizeTextData(development, we);
            this.FEATURES_SIZE =  we.getDimensions();
            this.weights = new Double[FEATURES_SIZE];
            Arrays.fill(weights, 0.0);
        } catch (IOException error) {
            throw error;
        }
    }

    /**
     * loadBasicDataSet Method
     * Method to load the data sample to verify the accuracy of this Perceptron implementation. 
     * This method has nothing to do with the Retrofitting project, but with the debugging of the Perceptron class itself
     **/
    public void loadBasicDataSet(String train, String test) throws IOException {
        try {
            DataLoader data = new DataLoader();
            this.trainingSet = data.perceptronDataLoad(train);
            this.testSet = data.perceptronDataLoad(test);
            this.developmentSet = new ArrayList<HashMap<Double[], Integer>>(trainingSet.subList(0, trainingSet.size() / 10));
            this.FEATURES_SIZE = data.loadPerceptronFeatures(train);
        } catch (IOException error) {
            throw error;
        }
    }

    /**
     * printTrainingSet Method
     * Method to debug the Perceptron class.
     * This method has nothing to do with the Retrofitting project, but with the debugging of the Perceptron class itself
     **/
    public void printTrainingSet() {
        System.out.println("Training set data:");
        for (int i = 0; i < trainingSet.size(); i++) {
            for (Map.Entry<Double[], Integer> entry : trainingSet.get(i).entrySet()) {
                System.out.println("Vector: " + Arrays.toString(entry.getKey()) + " Label: " + String.format("% 2d", entry.getValue()));
            }
        }
    }

    /**
     * printTrainingSet Method
     * Method to debug the Perceptron class.
     * This method has nothing to do with the Retrofitting project, but with the debugging of the Perceptron class itself
     **/
    public void printTrainingSet(int numberOfResults) {
        System.out.println("Training set data.");
        System.out.println(numberOfResults + " Data points:");
        ArrayList<HashMap<Double[], Integer>> resultSet =
            new ArrayList<HashMap<Double[], Integer>>(trainingSet.subList(0, numberOfResults));
        for (int i = 0; i < resultSet.size(); i++) {
            for (Map.Entry<Double[], Integer> entry : resultSet.get(i).entrySet()) {
                System.out.println("Vector: " + Arrays.toString(entry.getKey()) + " Label: " + String.format("% 2d", entry.getValue()));
            }
        }
    }

    /**
     * printTestSet Method
     * Method to debug the Perceptron class.
     * This method has nothing to do with the Retrofitting project, but with the debugging of the Perceptron class itself
     **/
    public void printTestSet() {
        System.out.println("Test set data:");
        for (int i = 0; i < testSet.size(); i++) {
            for (Map.Entry<Double[], Integer> entry : testSet.get(i).entrySet()) {
                System.out.println("Vector: " + Arrays.toString(entry.getKey()) + " Label: " + String.format("% 2d", entry.getValue()));
            }
        }
    }

    /**
     * printTestSet Method
     * Method to debug the Perceptron class.
     * This method has nothing to do with the Retrofitting project, but with the debugging of the Perceptron class itself
     **/
    public void printTestSet(int numberOfResults) {
        System.out.println("Test set data.");
        System.out.println(numberOfResults + " Data points:");
        ArrayList<HashMap<Double[], Integer>> resultSet =
            new ArrayList<HashMap<Double[], Integer>>(testSet.subList(0, numberOfResults));
        for (int i = 0; i < resultSet.size(); i++) {
            for (Map.Entry<Double[], Integer> entry : resultSet.get(i).entrySet()) {
                System.out.println("Vector: " + Arrays.toString(entry.getKey()) + " Label: " + String.format("% 2d", entry.getValue()));
            }
        }
    }

    /**
     * printDevelopmentSet Method
     * Method to debug the Perceptron class.
     * This method has nothing to do with the Retrofitting project, but with the debugging of the Perceptron class itself
     **/
    public void printDevelopmentSet() {
        System.out.println("Development set data:");
        for (int i = 0; i < testSet.size(); i++) {
            for (Map.Entry<Double[], Integer> entry : developmentSet.get(i).entrySet()) {
                System.out.println("Vector: " + Arrays.toString(entry.getKey()) + " Label: " + String.format("% 2d", entry.getValue()));
            }
        }
    }

    /**
     * printDevelopmentSet Method
     * Method to debug the Perceptron class.
     * This method has nothing to do with the Retrofitting project, but with the debugging of the Perceptron class itself
     **/
    public void printDevelopmentSet(int numberOfResults) {
        System.out.println("Development set data.");
        System.out.println(numberOfResults + " Data points:");
        ArrayList<HashMap<Double[], Integer>> resultSet =
            new ArrayList<HashMap<Double[], Integer>>(developmentSet.subList(0, numberOfResults));
        for (int i = 0; i < resultSet.size(); i++) {
            for (Map.Entry<Double[], Integer> entry : resultSet.get(i).entrySet()) {
                System.out.println("Vector: " + Arrays.toString(entry.getKey()) + " Label: " + String.format("% 2d", entry.getValue()));
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////
// CORE ALGORITHM //
///////////////////////////////////////////////////////////////////////////////

    /**
     * classify Method
     * Core method to classify an example using this Perceptron class
     **/
    public int classify(Double[] arrayX,  Double[] arrayY) {
        return (dotProduct(arrayX, arrayY) > 0) ? 1 : -1;
    }

    /**
     * dotProduct Method
     * Methematical formula for dotProduct between two array of floats
     **/
    public double dotProduct(Double[] arrayX, Double[] arrayY) {
        double sum = 0.0;
        for (int i = 0; i < arrayY.length; i++) {
            sum += arrayX[i] * arrayY[i];
        }
        return sum;
    }

    /**
     * arraySum Method
     * Methematical formula for the sum between two array of floats
     **/
    public Double[] arraySum(Double[] arrayX, Double[] arrayY) {
        Double[] sum = new Double[arrayX.length];
        for (int i = 0; i < arrayX.length; i++) {
            sum[i] = arrayX[i] + arrayY[i];
        }
        return sum;
    }

    /**
     * sumOfDoubleArrays Method
     * Methematical formula for the sum between two array of floats
     **/
    public Double[] sumOfDoubleArrays(Double[] arrayX, Double[] arrayY) {
        Double[] resultedArray = new Double[arrayX.length];
        for (int i = 0; i < arrayX.length; i++) resultedArray[i] = arrayX[i] + arrayY[i];
        return resultedArray;
    }

    /**
     * sumToArray Method
     * Methematical formula for the sum of an array of doubles with an int 
     **/
    public Double[] sumToArray(Double[] arrayX, int x) {
        Double[] sum = new Double[arrayX.length];
        for (int i = 0; i < arrayX.length; i++) sum[i] = arrayX[i] + x;
        return sum;
    }

    /**
     * productToArray Method
     * Methematical formula for the product of an array of doubles with an int 
     **/
    public Double[] productToArray(Double[] arrayX, int x) {
        Double[] sum = new Double[arrayX.length];
        for (int i = 0; i < arrayX.length; i++) sum[i] = arrayX[i] * x;
        return sum;
    }

    /**
     * evaluate Method
     * Evaluates the accuracy of this perceptron using some test set and the learned Perceptron weights
     **/
    public double evaluate(ArrayList<HashMap<Double[], Integer>> data, Double[] weights) {
        double accuracy = 0.0;
        for (int i = 0; i < data.size(); i++) {
            for (Map.Entry<Double[], Integer> entry : data.get(i).entrySet()) {
                if (classify(entry.getKey(), weights) == entry.getValue()) {
                    accuracy += 1.0;
                }
            }
        }
        return accuracy / data.size();
    }

    /**
     * learn Method
     * core learing method of the Perceptron, it uses a number of iterations as a stop condition
     **/
    public void learn() {
        Double[] w = new Double[FEATURES_SIZE];
        Arrays.fill(w, 0.0);
        int up = 0;
        for (int e = 0; e < EPOCHS; e++) {
            Collections.shuffle(trainingSet);
            for (int i = 0; i < trainingSet.size(); i++) {
                for (Map.Entry<Double[], Integer> entry : trainingSet.get(i).entrySet()) {
                    if (classify(entry.getKey(), w) != entry.getValue()) {
                        Double[] step = productToArray(entry.getKey(), entry.getValue());
                        w = arraySum(this.weights, step);
                        up += 1;
                    }
                }
            }
        }
        this.weights = w;
        return;
    }

    /**
     * learnUntilNoErrors Method
     * core learing method of the Perceptron, it uses a number stops after it finds no error or it encouters 2000 iterations
     **/
    public void learnUntilNoErrors() {
        this.weights = new Double[FEATURES_SIZE];
        Arrays.fill(this.weights, 0.0);
        for (int h = 0; h < MAX_ITERATIONS; h++) {
            Collections.shuffle(trainingSet);
            int up = 0;
            for (int i = 0; i < trainingSet.size(); i++) {
                for (Map.Entry<Double[], Integer> entry : trainingSet.get(i).entrySet()) {
                    if (classify(entry.getKey(), this.weights) != entry.getValue()) {
                        Double[] step = productToArray(entry.getKey(), entry.getValue());
                        this.weights = arraySum(this.weights, step);
                        up += 1;
                    }
                }
            }
            if (up == 0) {
                return;
            }
        }
        System.out.println("Unsplittable data.");
        return;
    }

    /**
     * learnWithDevSet Method
     * core learing method of the Perceptron, it uses a development set
     **/
    public void learnWithDevSet() {
        this.weights = new Double[FEATURES_SIZE];
        Arrays.fill(this.weights, 0.0);
        double last_accuracy = 0.0;
        //this.developmentSet = new ArrayList<HashMap<Double[], Integer>>(trainingSet.subList(0, trainingSet.size() / 10));
        //this.trainingSet = new ArrayList<HashMap<Double[], Integer>>
            //(trainingSet.subList(trainingSet.size() / 10, trainingSet.size()));
        for (int h = 0; h < MAX_ITERATIONS; h++) {
            Collections.shuffle(trainingSet);
            int up = 0;
            for (int i = 0; i < trainingSet.size(); i++) {
                for (Map.Entry<Double[], Integer> entry : trainingSet.get(i).entrySet()) {
                    if (classify(entry.getKey(), this.weights) != entry.getValue()) {
                        Double[] step = productToArray(entry.getKey(), entry.getValue());
                        this.weights = arraySum(this.weights, step);
                        up += 1;
                    }
                }
            }
            double new_accuracy = evaluate(developmentSet, this.weights);
            if (new_accuracy <= last_accuracy) {
                System.out.println("At iteration " + h + " precision ceased to decrease.");
                return;
            }
        last_accuracy = new_accuracy;
        }
        return;
    }


    /**
     * olineTest Method
     * Basic test function to test a phrase using learned data
     **/
    public void onlineTest(String testText, WordEmbedding we) {
        DataLoader loader = new DataLoader();
        Double[] vectorizedTestText = loader.vectorizeStringArray(testText.split(" "), we);
            if (classify(vectorizedTestText, this.weights) == 1) {
                System.out.println("Input \"" + testText + "\" is considered positive. " +
                        dotProduct(vectorizedTestText, this.weights));
            } else { System.out.println("Input \"" + testText + "\" is considered negative. " +
                        dotProduct(vectorizedTestText, this.weights));
            }
    }


///////////////////////////////////////////////////////////////////////////////
// TESTS //
///////////////////////////////////////////////////////////////////////////////

    public static void basicTest() throws IOException {
        String house = "/Users/Hermes/Temp/Test/tp4_perceptron/house-votes-84.data";
        String train = "/Users/Hermes/Temp/Test/tp4_perceptron/train";
        String test = "/Users/Hermes/Temp/Test/tp4_perceptron/test";
        Perceptron p = new Perceptron();
        //DataLoader d = new DataLoader();
        p.loadBasicDataSet(train, test);
        Double[] someW = {25., -12., 67., -104., -43., 46., -18., -10., 45.,-33., 54., -39., 43., -19., 5., -2., 55.};
        p.setWeights(someW);
        System.out.println("Partie 1:");
        System.out.println("Exactitude avec les paramètres donnés: " + p.evaluate(p.testSet, someW));
        int[] epochs = {1, 2, 4, 8, 16, 32, 64, 128};

        for (int epoch : epochs) {
            p.setParameters(someW.length, epoch);
            p.learn();
            System.out.println("nombre d'itérations " + epoch + " accuracy train " + p.evaluate(p.trainingSet, someW)
                    + " accuracy test " + p.evaluate(p.testSet, p.weights));
        }
        p.learnUntilNoErrors();
        System.out.println("Exactitude méthode 2 (no error on train set): " + p.evaluate(p.testSet, p.weights));

        p.learnWithDevSet();
        System.out.println("Exactitude méthode 3 (dev set): " + p.evaluate(p.testSet, p.weights));
    }

    public static void sentimentAnalysisTest() throws IOException {
        final String vectors50 = "./vectors50";
        final String vector_dt = "./vectors_datatxt_250_sg_w10_i5_c500_gensim_clean";
        final String StanfordSATrain = "./stanford_raw_train.txt";
        final String StanfordSATest = "./stanford_raw_test.txt";
        final String StanfordSADev = "./stanford_raw_dev.txt";
        Perceptron p = new Perceptron();
        System.out.println("Loading word embeddings..");
        WordEmbedding we = new WordEmbedding(vector_dt);
        System.out.println("Word embeddings loaded.");
        p.loadDataSet(StanfordSATrain, StanfordSATest, StanfordSADev, we);
        //p.printTestSet(10);
        p.setNumberOfEpochs(128);
        System.out.println("Learning perceptron");
        p.learnUntilNoErrors();
        System.out.println("Perceptron learned, weights: ");
        System.out.println(Arrays.toString(p.weights));

        System.out.println("EVALUATION " + p.evaluate(p.testSet, p.weights));
        Scanner sc = new Scanner(System.in);

        System.out.println("Online Test.");

        while (true) {
            System.out.print("> ");
            String input = sc.nextLine();
            p.onlineTest(input, we);
        }
    }

    public static void main(String[] args) throws IOException {
        //basicTest();
        sentimentAnalysisTest();
    }

}
