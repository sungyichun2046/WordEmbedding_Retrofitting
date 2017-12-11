import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Integer;
import java.lang.Double;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class DataLoader {

    /**
     * loadTextIntegerData Method
     * Method to load a text data file containing a numerical value followed by
     * a sequence of alphanumerical characters.
     * Representated as a Dictionary: key = alphanumerical value : value = numerical value
     */
    public HashMap<String[], Integer> loadTextIntegerData(String textDataFile) throws IOException, FileNotFoundException {
        HashMap<String[], Integer> textData;
        try (BufferedReader exampleDataFileBuffer = new BufferedReader(new FileReader(textDataFile))) {
            textData = new HashMap<String[], Integer>();
            String line;
            while ((line = exampleDataFileBuffer.readLine()) != null) {
                Integer value = new Integer(line.split(" ", 2)[0]);
                String[] phrase = line.split(" ", 2)[1].split(" ");
                textData.put(phrase, value);
            }
            return textData;
        } catch (FileNotFoundException fileNotFound) {
            throw fileNotFound;
        } catch (IOException io) {
            throw io;
        }
    }

    /**
     * loadTextDoubleData Method
     * Method to load a text data file containing a sequence alphanumerical value followed by
     * a numerical value.
     * Representated as a Dictionary: key = alphanumerical value : value = numerical value
     */
    public HashMap<String[], Double> loadTextDoubleData(String textDataFile) throws IOException, FileNotFoundException {
        HashMap<String[], Double> textData;
        try (BufferedReader fileBuffer = new BufferedReader(new FileReader(textDataFile))) {
            textData = new HashMap<String[], Double>();
            String line;
            while ((line = fileBuffer.readLine()) != null) {
                String[] lineSplit = line.split(" ");
                String[] currentPair = {lineSplit[0], lineSplit[1]};
                Double currentValue = new Double(lineSplit[2]);
                textData.put(currentPair, currentValue);
            }
            return textData;
        } catch (FileNotFoundException fileNotFound) {
            throw fileNotFound;
        } catch (IOException io) {
            throw io;
        }
    }




    /**
     * vectorizeTextData Method
     * Method to transform a text data file containing a numerical value followed by
     * a sequence of  alphanumerical value, it transforms each independent alphanumerical value of the sequence
     * into a corresponging vector from the specified WordEmbedding, the final value is the average of the sum of
     * each independent alphanumerical value of the sequence.
     * Representated as a Dictionary: key = vector representation of sequence : value = numerical value.
     */
    public ArrayList<HashMap<Double[], Integer>> vectorizeTextData(String textDataFile, WordEmbedding we) throws IOException {
        ArrayList<HashMap<Double[], Integer>> vectorizedData;
        try (BufferedReader textDataFileBuffer = new BufferedReader(new FileReader(textDataFile))) {
            vectorizedData = new ArrayList<HashMap<Double[], Integer>>();
            String line;
            while ((line = textDataFileBuffer.readLine()) != null) {
                Integer value = new Integer(line.split(" ", 2)[0]);
                String[] phrase = line.split(" ", 2)[1].split(" ");
                Double[] vectorizedPhrase = vectorizeStringArray(phrase, we);
                HashMap<Double[], Integer> vectorizedValuePhrase = new HashMap<Double[], Integer>();
                vectorizedValuePhrase.put(vectorizedPhrase, value);
                vectorizedData.add(vectorizedValuePhrase);
            }
            return vectorizedData;
        } catch (IOException fileNotFound) {
            throw fileNotFound;
        }
    }

    /**
     * vectorizeStringArray Method
     * Method to convert textual data in vectors using a word embedding as
     * base for a numerical representation.
     */
    public Double[] vectorizeStringArray(String[] textData, WordEmbedding we) {
        Double[] sentenceRepresentation = new Double[we.getDimensions()];
        Arrays.fill(sentenceRepresentation, 0.0);
        Double[] currentRepresentation = new Double[we.getDimensions()];
        for (String currentWord : textData) {
            if (!we.containsWord(currentWord)) continue;
            currentRepresentation = we.getVector(currentWord);
            for (int j = 0; j < we.getDimensions(); j++) {
                sentenceRepresentation[j] += currentRepresentation[j];
            }
        }
        for (int i = 0; i < we.getDimensions(); i++) sentenceRepresentation[i] /= textData.length;
        return sentenceRepresentation;
    }

    /**
     * loadWordEmbedding Method.
     * Representated as a Dictionary: key = alphanumerical value : value = vector representation of sequence.
     */
    public HashMap<String, Double[]> loadWordEmbedding(String wordEmbeddingsFile) throws IOException {
        HashMap<String, Double[]> wordEmbedding;
        try (BufferedReader wordEmbeddingFileBuffer = new BufferedReader(new FileReader(wordEmbeddingsFile))) {
            wordEmbedding = new HashMap<String, Double[]>();
            final String firstLine = wordEmbeddingFileBuffer.readLine();
            final String[] wordEmbeddingHeader = firstLine.split(" ");
            final int vocabularySize = Integer.valueOf(wordEmbeddingHeader[0]); // CATCH HEADER ERROR
            final int dimensions = Integer.valueOf(wordEmbeddingHeader[1]);     // CATCH HEADER ERROR
            String line;
            while ((line = wordEmbeddingFileBuffer.readLine()) != null) {
                final String[] currentEmbedding = line.split(" ");
                final String word = line.split(" ", 2)[0];
                final String[] embeddingAsString = line.split(" ", 2)[1].split(" ");
                final Double[] embedding = new ArrayList<Double>() {
                    {for (String value : embeddingAsString) add(new Double(value));}
                }.toArray(new Double[embeddingAsString.length]);
                wordEmbedding.put(word, embedding);
            }
            return wordEmbedding;
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Typo? File not found.");
            throw fileNotFound;
        }
    }

    /**
     * loadWordEmbedding Method.
     * Representated as a Dictionary: key = alphanumerical value : value = vector representation of sequence.
     */
    public HashMap<String, Double[]> loadWordEmbedding(String wordEmbeddingsFile, int customDimensions) throws IOException {
        HashMap<String, Double[]> wordEmbedding;
        try (BufferedReader wordEmbeddingFileBuffer = new BufferedReader(new FileReader(wordEmbeddingsFile))) {
            wordEmbedding = new HashMap<String, Double[]>();
            final String firstLine = wordEmbeddingFileBuffer.readLine();
            final String[] wordEmbeddingHeader = firstLine.split(" ");
            final int vocabularySize = Integer.valueOf(wordEmbeddingHeader[0]); // CATCH HEADER ERROR
            final int dimensions = customDimensions;     // CATCH HEADER ERROR
            String line;
            while ((line = wordEmbeddingFileBuffer.readLine()) != null) {
                final String[] currentEmbedding = line.split(" ");
                final String word = line.split(" ", 2)[0];
                String[] embeddingAsString = new String[dimensions];
                final String[] temporalEmbeddingAsString = line.split(" ", 2)[1].split(" ");
                for (int i = 0; i < dimensions; i++) embeddingAsString[i] = temporalEmbeddingAsString[i];
                final Double[] embedding = new ArrayList<Double>() {
                    {for (String value : embeddingAsString) add(new Double(value));}
                }.toArray(new Double[embeddingAsString.length]);
                wordEmbedding.put(word, embedding);
            }
            return wordEmbedding;
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Typo? File not found.");
            throw fileNotFound;
        }
    }
    /**
     * loadWordEmbeddingHeader Method.
     * Representated as an integer array of size 2, array[0] = vocabulary size, array[1] dimensions.
     */
    public int[] loadWordEmbeddingHeader(String wordEmbeddingsFile) throws IOException {
        int[] header;
        try (BufferedReader wordEmbeddingFileBuffer = new BufferedReader(new FileReader(wordEmbeddingsFile))) {
            header = new int[2];
            final String firstLine = wordEmbeddingFileBuffer.readLine();
            final String[] wordEmbeddingHeader = firstLine.split(" ");
            header[0] = Integer.valueOf(wordEmbeddingHeader[0]); // CATCH HEADER ERROR
            header[1] = Integer.valueOf(wordEmbeddingHeader[1]);     // CATCH HEADER ERROR
            return header;
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Typo? File not found.");
            throw fileNotFound;
        }
    }

    /**
     * writeWordEmbedding Method.
     */
    public void writeWordEmbedding(WordEmbedding we, String outputFile) {
        try (BufferedWriter wordEmbeddingFileBuffer = new BufferedWriter(new FileWriter(outputFile))) {
            wordEmbeddingFileBuffer.write(we.getDimensions() + " " + we.getVocabularySize());
            for (Map.Entry<String,Double[]> currentEmbedding : we.getWordEmbeddingSet())
                wordEmbeddingFileBuffer.write(currentEmbedding.getKey() + " " + currentEmbedding.getValue());
        } catch (IOException error) {
            error.printStackTrace();
        }
    }


    /**
     * loadWOLF Method.
     * Used to load WOLF data used by a SemanticLexicon class
     **/
    public HashMap<String,ArrayList<String>> loadWOLF(String textDataFile) {
        HashMap<String,ArrayList<String>> semanticLexiconList = new HashMap<String,ArrayList<String>>();
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            Document documentFromTextData = builder.parse(textDataFile);
            final Element root = documentFromTextData.getDocumentElement();
            NodeList rootNode = root.getChildNodes();
            for (int i = 0; i < rootNode.getLength(); i++) {
                if (rootNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element SYNSET = (Element) rootNode.item(i);
                    Element ID = (Element) SYNSET.getElementsByTagName("ID").item(0);
                    NodeList literals = SYNSET.getElementsByTagName("LITERAL");
                    ArrayList<String> literalList = new ArrayList<String>();
                    for (int j = 0; j < literals.getLength(); j++) {
                        Element literal = (Element) literals.item(j);
                        if (!literal.getTextContent().equals("_EMPTY_")) {
                            literalList.add(literal.getTextContent());
                        }
                    }
                    if (!literalList.isEmpty())
                        semanticLexiconList.put(ID.getTextContent(), literalList);
                }
            }
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return semanticLexiconList;
    }

    /**
     * loadPPDB Method.
     * Used to load PPDB data used by a SemanticLexicon class
     **/
    public HashMap<String,HashSet<String>> loadPPDB(String textDataFile) {
        HashMap<String,HashSet<String>> semanticLexiconEngSet = new HashMap<String,HashSet<String>>();

        try {
            File f = new File(textDataFile);
            Scanner sc = new Scanner(f);
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] paraphrase = line.split("\\|\\|\\|");
                String word = paraphrase[1].replaceAll("\\s","");
                String neighbour = paraphrase[2].replaceAll("\\s","");
                //if (word.equals("pavement")){
                    //System.out.println(neighbour);
                //} else if (neighbour.equals("pavement")){
                    //System.out.println(word);
                //}
                HashSet<String> closewords_of_word = semanticLexiconEngSet.get(word);
                if (closewords_of_word == null){
                    HashSet<String>close = new HashSet<>();
                    close.add(neighbour);
                    semanticLexiconEngSet.put(word, close);
                }
                else {
                    closewords_of_word.add(neighbour);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return semanticLexiconEngSet;
    }

    /**
     * expandWOLF Method.
     * Used to load WOLF data used by a SemanticLexicon class
     **/
    public HashMap<String,HashSet<String>> expandWOLF(String textDataFile, HashMap<String,ArrayList<String>> previousSemanticLexiconList) {
        HashMap<String,HashSet<String>> semanticLexiconSet = new HashMap<String,HashSet<String>>();
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            Document documentFromTextData = builder.parse(textDataFile); 
            final Element root = documentFromTextData.getDocumentElement();
            NodeList rootNode = root.getChildNodes();
            for (int i = 0; i < rootNode.getLength(); i++) {
                if (rootNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    final Element SYNSET = (Element) rootNode.item(i);
                    NodeList ILRs = SYNSET.getElementsByTagName("ILR");
                    final Element ID = (Element) SYNSET.getElementsByTagName("ID").item(0);
                    if (previousSemanticLexiconList.containsKey(ID.getTextContent())) {
                        ArrayList<String> SYNSET_ofID = previousSemanticLexiconList.get(ID.getTextContent());
                        for (String e : SYNSET_ofID) {
                            HashSet<String> synonym = semanticLexiconSet.get(e);
                            if (synonym == null) {
                                HashSet<String> set = new HashSet<>();
                                for (String f : SYNSET_ofID) {
                                    if (f !=e) set.add(f);
                                }
                                if (!set.isEmpty()) semanticLexiconSet.put(e, set);
                            } else {
                                for (String f : SYNSET_ofID) if (f!=e) synonym.add(f);
                            }
                        }
                        for (int l = 0; l < ILRs.getLength(); l++) {
                            final Element ILR = (Element) ILRs.item(l);
                            if (ILR.getAttribute("type").equals("hypernym") || ILR.getAttribute("type").equals("instance_hypernym")) {
                                if (previousSemanticLexiconList.containsKey(ILR.getTextContent())) {
                                    ArrayList<String> neighbour = previousSemanticLexiconList.get(ILR.getTextContent());
                                    for (String e : SYNSET_ofID) {
                                        HashSet<String> closeWords = semanticLexiconSet.get(e);
                                        if (closeWords == null) {
                                            HashSet<String> set = new HashSet<>();
                                            for (String f : neighbour) set.add(f);
                                            if (!set.isEmpty()) semanticLexiconSet.put(e, set);
                                        } else {
                                            for (String f : neighbour) closeWords.add(f);
                                        }
                                    }
                                    for (String closeNeighbour : neighbour) {
                                        HashSet<String> neighbourValue = semanticLexiconSet.get(closeNeighbour);
                                        if (neighbourValue == null) {
                                            HashSet<String> s = new HashSet<>();
                                            for (String f : SYNSET_ofID) s.add(f);
                                            if (!s.isEmpty()) semanticLexiconSet.put(closeNeighbour, s);
                                        } else {
                                            for (String f:SYNSET_ofID) neighbourValue .add(f);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return semanticLexiconSet;
    }

    /**
     * perceptronDataLoad Method.
     * Used to load Perceptron data used by a Perceptron class
     **/
    public ArrayList<HashMap<Double[], Integer>> perceptronDataLoad(String textDataFile) throws IOException {
        HashMap<String, String> dataMeaning = new HashMap<String, String>();
        dataMeaning.put("republican", "-1");
        dataMeaning.put("democrat", "1");
        dataMeaning.put("?", "3");
        dataMeaning.put("y", "2");
        dataMeaning.put("n", "1");
        ArrayList<HashMap<Double[], Integer>> vectorizedData;
        try (BufferedReader textDataFileBuffer = new BufferedReader(new FileReader(textDataFile))) {
            vectorizedData = new ArrayList<HashMap<Double[], Integer>>();
            String line;
            while ((line = textDataFileBuffer.readLine()) != null) {
                Integer value = new Integer(dataMeaning.get(line.split(",", 2)[0]));
                String[] phrase = line.split(",", 2)[1].split(",");
                Double[] vectorizedPhrase = new Double[phrase.length + 1];
                for (int i = 0; i < phrase.length; i++) vectorizedPhrase[i] = Double.valueOf(dataMeaning.get(phrase[i]));
                vectorizedPhrase[phrase.length] = 1.0;
                HashMap<Double[], Integer> vectorizedValuePhrase = new HashMap<Double[], Integer>();
                vectorizedValuePhrase.put(vectorizedPhrase, value);
                vectorizedData.add(vectorizedValuePhrase);
            }
            return vectorizedData;
        } catch (IOException fileNotFound) {
            throw fileNotFound;
        }
    }

    /**
     * loadPerceptronFeatures Method.
     * Used to load Perceptron data used by a Perceptron class
     **/
    public int loadPerceptronFeatures(String textDataFile) throws IOException {
        try (BufferedReader textDataFileBuffer = new BufferedReader(new FileReader(textDataFile))) {
            String line = textDataFileBuffer.readLine();
            String[] phrase = line.split(",", 2)[1].split(",");
            return phrase.length + 1;
        } catch (IOException error) {
            throw error;
        }
    }





}

