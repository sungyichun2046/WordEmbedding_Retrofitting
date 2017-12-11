import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.text.NumberFormat; 


/***
 *
 *Les ressources:

 - Vecteurs de mots:
FR:  vectors50
EN:  vectors_datatxt_250_sg_w10_i5_c500_gensim_clean

- Ressources sémantiques:
EN: http://www.cis.upenn.edu/~ccb/ppdb/release-1.0/ppdb-1.0-xl-lexical.gz   (PPDB, paraphrase database)
wordnet (à utiliser via nltk)
FR: https://gforge.inria.fr/frs/download.php/file/33496/wolf-1.0b4.xml.bz2 (WOLF)
 *
 */
//  JWNL (Java WordNet Library)  https://sourceforge.net/p/jwordnet/wiki/Home/?SetFreedomCookie

public class Retrofitting {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length != 3) {
            System.out.println("Usage: Retrofitting [language (en, fr)] [iterations] [dimensions]");
            System.exit(127);
        }
        if (args[0].equals("en")) {
            retrofittingForEnglish(Integer.valueOf(args[1]), Integer.valueOf(args[2]));
            System.exit(0);
        }
        retrofittingForFrench(Integer.valueOf(args[1]), Integer.valueOf(args[2]));
        System.exit(0);
    }

    public static void retrofittingForEnglish(int numberOfIterations, int dimensions) throws FileNotFoundException, IOException {
        // Retrofitting du fichier vectors_datatxt_250_sg_w10_i5_c500_gensim_clean en anglais //
        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        System.out.println("Word Embeddings Retrofitting");
        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        System.out.println("Preparing retrofitting mode for file: vectors_datatxt");
        System.out.println("Loading file...");
        final WordEmbedding word2Vector = new WordEmbedding("vectors_datatxt_250_sg_w10_i5_c500_gensim_clean", dimensions);
        SemanticLexicon englishSemanticLexicon = new SemanticLexicon();
        System.out.println("File loaded.");
        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        System.out.println("Loading semantic lexicon from PPDB...");
        englishSemanticLexicon.loadSemanticLexicon("./ppdb-1.0-xl-lexical");
        System.out.println("PPDB file loaded.");
        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        List<Double> scoreList = new ArrayList<Double>();
        // HERE 100 DIMENSIONS USED FOR AVOIDING GC ISSUES
        WordEmbedding word2VectorRetrofitted = new WordEmbedding("vectors_datatxt_250_sg_w10_i5_c500_gensim_clean", dimensions);
        System.out.println("Dimensions to retrofit: " + word2VectorRetrofitted.getDimensions());
        retrofit(numberOfIterations, word2Vector, word2VectorRetrofitted, englishSemanticLexicon, scoreList);

        DataLoader dataWrite = new DataLoader();
        //dataWrite.writeWordEmbeddingFile(word2VectorRetrofitted, "./vectors_datatxt_250_sg_w10_i5_c500_gensim_clean_retrofitted");

        /// EVALUATION //

        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        System.out.println("Evaluation: ");
        Evaluation evaluation = new Evaluation();

        System.out.println("Spearman Correlation: ");
	double initalValue = evaluation.spearmanCorrelationCorrectedForEnglish("./ws353.txt", word2Vector);
	System.out.println("Annotated values - vectors_datatxt distances: " + initalValue) ;
	double retrofittedValue = evaluation.spearmanCorrelationCorrectedForEnglish("./ws353.txt", word2VectorRetrofitted);
	System.out.println("Annotated values - vectors_datatxt_retrofitted distances: " + retrofittedValue) ;


        System.out.println("Evaluation for word embeddings in english: ");
        System.out.println("loading perceptron...");
        Perceptron initalPerceptron = new Perceptron();
        System.out.println("Loading initial perceptron.");
        initalPerceptron.loadDataSet("./stanford_raw_train.txt", "./stanford_raw_test.txt", "./stanford_raw_dev.txt", word2Vector);
        initalPerceptron.setNumberOfEpochs(128);
        System.out.println("Loading initial perceptron done.");
        System.out.println("Learning Perceptron...");
        initalPerceptron.learnUntilNoErrors();
        System.out.println("Initial Perceptron: " + initalPerceptron.evaluate(initalPerceptron.testSet, initalPerceptron.weights));

        Perceptron finalPerceptron = new Perceptron();
        System.out.println("Loading final perceptron.");
        finalPerceptron.loadDataSet ("./stanford_raw_train.txt", "./stanford_raw_test.txt", "./stanford_raw_dev.txt", word2VectorRetrofitted);
        finalPerceptron.setNumberOfEpochs(128);
        System.out.println("Loading final perceptron done.");
        System.out.println("Learning Perceptron...");
        finalPerceptron.learnUntilNoErrors();
        System.out.println("Final Perceptron: "+finalPerceptron.evaluate(finalPerceptron.testSet, finalPerceptron.weights));
        System.out.println("Program terminated.");

    }

    public static void retrofittingForFrench(int numberOfIterations, int dimensions) throws FileNotFoundException, IOException {
        // Retrofitting for the vectors50 word embeddings representation file (French, 50 dimensions) //
        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        System.out.println("Word Embeddings Retrofitting");
        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        System.out.println("Retrofitting mode for file: vectors50");
        System.out.println("Loading file...");
        final WordEmbedding word2Vector = new WordEmbedding("vectors50", 10);
        System.out.println("File loaded.");
        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        System.out.println("Loading semantic lexicon from WOLF...");
        SemanticLexicon frenchSemanticLexicon = new SemanticLexicon();
	frenchSemanticLexicon.loadSemanticLexicon("./wolf-1.0b4.xml");
        System.out.println("WOLF file loaded.");
        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        List<Double> scoreList = new ArrayList<Double>();
        System.out.println("Retrofitting vectors50...");
        WordEmbedding word2VectorRetrofitted = new WordEmbedding("./vectors50", 10);
        System.out.println("Dimensions to retrofit: " + word2VectorRetrofitted.getDimensions());
        retrofit(numberOfIterations, word2Vector, word2VectorRetrofitted, frenchSemanticLexicon, scoreList);
        System.out.println("////////////////////////////////////////////////////////////////////////////////");

        //word2VectorRetrofitted.writeWordEmbeddingFile("./vectors50_retrofitted");

        /// EVALUATION //

        System.out.println("Evaluation: ");
        Evaluation evaluation = new Evaluation();

        System.out.println("Spearman Correlation: ");
	double initalValue = evaluation.spearmanCorrelationCorrected("rg65_french.txt", word2Vector);
	System.out.println("Annotated values - vectors50 distances: " + initalValue) ;
	double retrofittedValue = evaluation.spearmanCorrelationCorrected("rg65_french.txt", word2VectorRetrofitted);
	System.out.println("Annotated values - vectors50_retrofitted distances: " + retrofittedValue) ;
        System.out.println("Program terminated.");


    }

    public static void retrofit(int iterationNumber, WordEmbedding word2Vector, WordEmbedding newWord2Vector, SemanticLexicon word2CloseWords, List<Double> scoreList) {
        double score = 0.0;
        score = retrofittingScore(word2Vector, newWord2Vector, word2CloseWords);
        scoreList.add(score);
        for (int i = 0; i < iterationNumber; i++) {
            updateWordVector(word2Vector, newWord2Vector, word2CloseWords);
            score = retrofittingScore(word2Vector, newWord2Vector, word2CloseWords);
            System.out.print("Iteration " + (i + 1) + " loss: ");
            System.out.format("%03.06f\n", score);
            scoreList.add(score);
        }
    }

    public static void updateWordVector(WordEmbedding word2Vector, WordEmbedding newWord2Vector, SemanticLexicon semanticLexicon) {
        // Create a new vector holder for the sum of neighbouring words
    	Double[] sumOfNeighbouringWords = new Double[newWord2Vector.getDimensions()]; // 1st copy
        // Loop over every word in newWord2Vector
        for (String word :  word2Vector.wordSet()) {
            // Get a reference of the vector of word to modify
            Double[] newRetrofittedVector = newWord2Vector.getVector(word);
            // Fill vector holder with zeroes
            Arrays.fill(sumOfNeighbouringWords, 0.0);
            // If semantic lexicon contains word from word2Vector
            if (semanticLexicon.containsWord(word)) {
                // Counter for the total of neighbours
                int numberOfNeighbours = 0;
                // For every neighbour of word
                for (String neighbour :  semanticLexicon.getNeighboursOfWord(word)) {
                        // if has word has a neighbour
                    	if(newWord2Vector.containsWord(neighbour)){
                                // Counter increases
                    		numberOfNeighbours += 1;
                                // Addition of neighbour vector to vector holder
                    		sumOfArrayOfDoubles(sumOfNeighbouringWords, newWord2Vector.getVector(neighbour));
                    	}
                }
                if (numberOfNeighbours > 0) {
                    // Vector reset before starting the aditions
                    Arrays.fill(newRetrofittedVector, 0.0);
                    // Division to get the average of vector holder if number of neighbour > 0
                    divisionOfDoubleArrayByInteger(sumOfNeighbouringWords, numberOfNeighbours);
                    // The retrofitted referece of the current word gets the averaged added
                    sumOfArrayOfDoubles(newRetrofittedVector, sumOfNeighbouringWords);
                }
                    // Initial vector of word is added to the retrofitted reference
                    sumOfArrayOfDoubles(newRetrofittedVector, word2Vector.getVector(word));
                    // The retrofitted reference is divided by two
                    divisionOfDoubleArrayByInteger(newRetrofittedVector, 2);

            }
        }

    }

    public static Double retrofittingScore(WordEmbedding word2Vector, WordEmbedding newWord2Vector, SemanticLexicon word2CloseWords) {
        // For word in word2Vector
        Double score = 0.0;
        for (String word : word2Vector.wordSet()) {
            // Distance var is set to 0
            Double distance = 0.0;
            // Distance var gets the distance between initial vector of word and retrofitted
            distance = squareOfEuclideanDistance(newWord2Vector.getVector(word), word2Vector.getVector(word));

            // Score var gets the distance added
            score += distance;
            // neighbour score gets initialized to 0.0
            Double neighbourScore = 0.0;
            // If word has a neighbour
            if (word2CloseWords.containsWord(word)) {
                // Get the reference of list of neighbours
                HashSet<String> neighbours = word2CloseWords.getNeighboursOfWord(word);
                // Counter of number of neighbours
                Integer totalNumberOfNeighbours = 0;
                // For neighbour in neighbours
            	for (String neighbour : neighbours){
                    // If word2Vector contains neighbour
            	    if(word2Vector.containsWord(neighbour)){
                        // Counter increases
            		totalNumberOfNeighbours+=1;
                        // neighbour score gets the distance of retrofitted word and retrofitted neighbour added
            		neighbourScore += squareOfEuclideanDistance(newWord2Vector.getVector(word), newWord2Vector.getVector(neighbour));
                    }
                }
                if (totalNumberOfNeighbours > 0) {
                    score += (neighbourScore / (totalNumberOfNeighbours));
            	}
            }

        }
        return score;
    }

    public static Double squareOfEuclideanDistance(Double[] arrayX, Double[] arrayY) {
        Double result = 0.0;
        for (int i = 0; i < arrayX.length; i++) {
            result += (arrayX[i] - arrayY[i]) * (arrayX[i] - arrayY[i]);
        }
        return Math.sqrt(result);
    }

    public static void divisionOfDoubleArrayByInteger(Double[] arrayX, Integer divisorY){
        for (int i = 0; i < arrayX.length; i++) arrayX[i] /= divisorY;
    }

    public static void sumOfArrayOfDoubles(Double[] arrayX, Double[] arrayY) {
        for (int i = 0; i < arrayX.length; i++) arrayX[i] += arrayY[i];
    }



}
