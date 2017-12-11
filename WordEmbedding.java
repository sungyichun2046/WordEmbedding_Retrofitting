import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

/**
 * WordEmbedding Object.
 * An abstract representation of word vectors,
 * It includes similarty methods for measuring the distance between
 * two words.
 *
 */

public class WordEmbedding {

    protected HashMap<String,Double[]> wordEmbedding;
    protected int dimensions;
    protected int vocabularySize;

    WordEmbedding(String wordEmbeddingsFile) throws FileNotFoundException, IOException {
        final DataLoader data = new DataLoader();
        final int[] header = data.loadWordEmbeddingHeader(wordEmbeddingsFile);
        this.wordEmbedding = data.loadWordEmbedding(wordEmbeddingsFile);
        this.dimensions = header[1];
        this.vocabularySize = header[0];
    }

    WordEmbedding(String wordEmbeddingsFile, int customDimensions) throws FileNotFoundException, IOException {
        final DataLoader data = new DataLoader();
        final int[] header = data.loadWordEmbeddingHeader(wordEmbeddingsFile);
        this.wordEmbedding = data.loadWordEmbedding(wordEmbeddingsFile, customDimensions);
        this.dimensions = customDimensions;
        this.vocabularySize = header[0];
    }

    WordEmbedding(WordEmbedding wordEmbeddingToCopy) {
        this.wordEmbedding = new HashMap<String,Double[]>(wordEmbeddingToCopy.wordEmbedding); // Doesn't seem to work
    }

    WordEmbedding(HashMap<String,Double[]> wordEmbeddingRepresentation) {
        this.wordEmbedding = new HashMap<String,Double[]>(wordEmbeddingRepresentation);
    }


    /**
     * containsWord Method
     * Method that returns boolean if word in argument is present in WordEmbedding
     */
    public boolean containsWord(String word) {
        return wordEmbedding.containsKey(word);
    }

    /**
     * getWEHashMap Method
     * Method that returns the HashMap representation of this WordEmbedding 
     */
    public HashMap<String,Double[]> getWEHashMap() {
        return this.wordEmbedding;
    }

    /**
     * getWordEmbeddingSet Method
     * Method that returns the entrySet from the HashMap representation of this WordEmbedding 
     */
    public Set<Map.Entry<String,Double[]>> getWordEmbeddingSet() {
        return this.wordEmbedding.entrySet();
    }

    /**
     * getDimensions Method
     * Method that returns the size in dimensions of this WordEmbedding
     */
    public int getDimensions() {
        return this.dimensions;
    }

    /**
     * getVocabularySize Method
     * Method that returns the size of the vocabulary of this WordEmbedding
     */
    public int getVocabularySize() {
        return this.vocabularySize;
    }

    /**
     * getEmbedding Method
     * Method that returns the vector representation of the word in argument
     */
    public Double[] getEmbedding(String word) {
        return wordEmbedding.get(word);
    }

    /**
     * getVector Method
     * Method that returns the vector representation of the word in argument.
     * Same as getEmbedding
     */
    public Double[] getVector(String word) {
        return wordEmbedding.get(word);
    }

    /**
     * equals Method
     * Method that returns true if this WordEmbedding equals another in argument 
     */
    public boolean equals(WordEmbedding we) {
        return this.wordEmbedding.equals(we.getWordEmbeddingSet());
    }

    public void printVector(String word) {
        if (this.containsWord(word)) {
            System.out.println(Arrays.toString(wordEmbedding.get(word)));
            return;
        }
        Double[] unknownWord = new Double[this.dimensions];
        Arrays.fill(unknownWord, 0.0);
        System.out.println(Arrays.toString(unknownWord));
    }

    /**
     * wordSet Method
     * Method that returns this WordEmbedding HashMap keySet
     */
    public Set<String> wordSet() {
        return this.wordEmbedding.keySet();
    }

    /**
     * addEmbedding Method
     * Method that puts data in arguments inside this WordEmbedding HashMap
     */
    public void addEmbedding(String word, Double[] embedding) {
        this.wordEmbedding.put(word, embedding);
    }

    /**
     * cosineSimilarity Method
     * Distance method between 2 coordinates (vectors)
     */
    public static double cosineSimilarity(Double[] coord1, Double[] coord2) {
        Double produit = 0.0;
        Double magnitude1 = 0.0;
        Double magnitude2 = 0.0;
        Double similarity = 0.0;
        for (int i = 0; i < coord1.length; i++) {
            produit += coord1[i] * coord2[i];
            magnitude1 += Math.pow(coord1[i], 2);
            magnitude2 += Math.pow(coord2[i], 2);
        }
        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);
        if (magnitude1 != 0.0 || magnitude2 != 0.0) {
            similarity = produit / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return similarity;
    }

    private Double ecludianDistance(Double[] coord1, Double[] coord2) {
        Double diff_square_sum = 0.0;
        for (int i = 0; i < coord1.length; i++) {
            diff_square_sum += (coord1[i] - coord2[i]) * (coord1[i] - coord2[i]);
        }
        return Math.sqrt(diff_square_sum);
    }


    public Double getDistance(String firstWord, String secondWord) throws NullPointerException {
        try {
            return cosineSimilarity((Double[]) wordEmbedding.get(firstWord), (Double[]) wordEmbedding.get(secondWord));
        } catch (NullPointerException unknownWord) {
            System.out.println("Nope, there is a problem with a word or two.");
            throw unknownWord;
        }
    }

    public Double getDistanceBetween(String firstWord, String secondWord) throws NullPointerException {
        try {
            return cosineSimilarity((Double[]) wordEmbedding.get(firstWord), (Double[]) wordEmbedding.get(secondWord));
        } catch (NullPointerException unknownWord) {
            System.out.println("Nope, there is a problem with a word or two.");
            throw unknownWord;
        }
    }











    public Double similarityTest(String firstWord, String secondWord) throws NullPointerException {
        try {
            return cosineSimilarity((Double[]) wordEmbedding.get(firstWord), (Double[]) wordEmbedding.get(secondWord));
        } catch (NullPointerException unknownWord) {
            System.out.println("Nope, there is a problem with a word or two.");
            throw unknownWord;
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
    }

}
