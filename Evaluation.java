import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Double;

public class Evaluation {

        //String evaluationFile = "rg65_french.txt";
        //String comparisonOutput = "comparison_rg65_french.txt";
        final static SpearmansCorrelation SC = new SpearmansCorrelation();


    public double spearmanCorrelation(String evaluationFile, WordEmbedding we) throws IOException {
        DataLoader data = new DataLoader();
        List<String> listOfWordPairs;
        List<Double> evaluationValues;
        List<Double> vectorComparisonValues;
        try (BufferedReader fileBuffer = new BufferedReader(new FileReader(evaluationFile))) {
            listOfWordPairs = new ArrayList<String>();
            evaluationValues =  new ArrayList<Double>();
            vectorComparisonValues = new ArrayList<Double>();
            String line;
            while ((line = fileBuffer.readLine()) != null) {
                String[] lineSplit = line.split(" ");
                String[] currentPair = {lineSplit[0], lineSplit[1]};
                Double currentValue = new Double(lineSplit[2]);
                if (we.containsWord(lineSplit[0]) && we.containsWord(lineSplit[1])) {
                    listOfWordPairs.add(lineSplit[0] + " " + lineSplit[1]);
                    evaluationValues.add(currentValue);
                    vectorComparisonValues.add(we.similarityTest(lineSplit[0], lineSplit[1]));

                } else {
                    listOfWordPairs.add(lineSplit[0] + " " + lineSplit[1]);
                    vectorComparisonValues.add(0.0);
                }
            }
            double[] value1 = new double [evaluationValues.size()];
            double[] value2 = new double [evaluationValues.size()];
            for (int i = 0; i < evaluationValues.size(); i++) {
                value1[i] = evaluationValues.get(i);
                value2[i] = vectorComparisonValues.get(i);
            }
            double corr = this.SC.correlation(value1, value2);
            return corr;
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("File not found.");
            throw fileNotFound;
        }
    }

    public double spearmanCorrelationCorrected(String evaluationFile, WordEmbedding we) throws IOException {
        List<String> listOfWordPairs;
        List<Double> evaluationValues;
        List<Double> vectorComparisonValues;
        try (BufferedReader fileBuffer = new BufferedReader(new FileReader(evaluationFile))) {
            listOfWordPairs = new ArrayList<String>();
            evaluationValues =  new ArrayList<Double>();
            vectorComparisonValues = new ArrayList<Double>();
            String line;
            while ((line = fileBuffer.readLine()) != null) {
                String[] lineSplit = line.split(" ");
                String[] currentPair = {lineSplit[0], lineSplit[1]};
                Double currentValue = new Double(lineSplit[2]);
                if (we.containsWord(lineSplit[0]) && we.containsWord(lineSplit[1])) {
                    listOfWordPairs.add(lineSplit[0] + " " + lineSplit[1]);
                    evaluationValues.add(currentValue);
                    vectorComparisonValues.add(we.similarityTest(lineSplit[0], lineSplit[1]));
                }
            }
            double[] value1 = new double [evaluationValues.size()];
            double[] value2 = new double [evaluationValues.size()];
            for (int i = 0; i < evaluationValues.size(); i++) {
                value1[i] = evaluationValues.get(i);
                value2[i] = vectorComparisonValues.get(i);
            }
            double corr = this.SC.correlation(value1, value2);
            return corr;
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("File not found.");
            throw fileNotFound;
        }
    }

    public double spearmanCorrelationCorrectedForEnglish(String evaluationFile, WordEmbedding we) throws IOException {
        List<String> listOfWordPairs;
        List<Double> evaluationValues;
        List<Double> vectorComparisonValues;
        try (BufferedReader fileBuffer = new BufferedReader(new FileReader(evaluationFile))) {
            listOfWordPairs = new ArrayList<String>();
            evaluationValues =  new ArrayList<Double>();
            vectorComparisonValues = new ArrayList<Double>();
            String line;
            while ((line = fileBuffer.readLine()) != null) {
                String[] lineSplit = line.split("\t");
                String[] currentPair = {lineSplit[0], lineSplit[1]};
                Double currentValue = new Double(lineSplit[2]);
                if (we.containsWord(lineSplit[0]) && we.containsWord(lineSplit[1])) {
                    listOfWordPairs.add(lineSplit[0] + " " + lineSplit[1]);
                    evaluationValues.add(currentValue);
                    vectorComparisonValues.add(we.similarityTest(lineSplit[0], lineSplit[1]));
                }
            }
            double[] value1 = new double [evaluationValues.size()];
            double[] value2 = new double [evaluationValues.size()];
            for (int i = 0; i < evaluationValues.size(); i++) {
                value1[i] = evaluationValues.get(i);
                value2[i] = vectorComparisonValues.get(i);
            }
            double corr = this.SC.correlation(value1, value2);
            return corr;
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("File not found.");
            throw fileNotFound;
        }
    }

}

