import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * SemanticLexicon Object.
 * An abstract representation of a semantic lexicon.
 *
 */
public class SemanticLexicon {

    protected HashMap<String,ArrayList<String>> ID2SYNSET;
    protected HashMap<String,HashSet<String>> WORD2CLOSEWORDS;
    HashMap<String,HashSet<String>> PPDB;
    protected int vocabularySize;

    public void loadSemanticLexicon(String textDataFile) {
        if (textDataFile.contains("xml"))
            loadFrenchSemanticLexicon(textDataFile);
        else loadEnglishSemanticLexicon(textDataFile);
    }

    /**
     * loadFrenchSemanticLexicon Method
     * Method that loads WOLF data
     */
    public void loadFrenchSemanticLexicon(String textDataFile) {
        DataLoader data = new DataLoader();
        this.ID2SYNSET = data.loadWOLF(textDataFile);
        this.WORD2CLOSEWORDS = data.expandWOLF(textDataFile, this.ID2SYNSET);
        this.PPDB = null;
    }

    /**
     * loadEnglishSemanticLexicon Method
     * Method that loads PPDB data
     */
    public void loadEnglishSemanticLexicon(String textDataFile) {
        DataLoader data = new DataLoader();
        this.PPDB = data.loadPPDB(textDataFile);
        this.WORD2CLOSEWORDS = null;
        this.ID2SYNSET = null;
    }

    /**
     * getNeighboursOfWord Method
     * Method that returns a list of neighbours of word in argument
     */
    public HashSet<String> getNeighboursOfWord(String word) {
        return (this.PPDB == null) ? this.WORD2CLOSEWORDS.get(word) : this.PPDB.get(word);
    }

    /**
     * containsWord Method
     * Method that returns boolean if word in argument is present in SemanticLexicon
     */
    public boolean containsWord(String word) {
        if (this.PPDB == null) return this.WORD2CLOSEWORDS.containsKey(word);
        return this.PPDB.containsKey(word);
    }

    //public void filterSemanticLexicon(WordEmbedding we) {
        //HashMap<String,HashSet<String>> referenceSL;
        //if (this.PPDB == null)
            //referenceSL = this.WORD2CLOSEWORDS;
        //else
            //referenceSL = this.PPDB;
        //for (Iterator<String> iter = referenceSL.getKeySet().iterator(); iter.hasNext();) {
            //for (String wordInListOfNeighbours : iter.next().getValue()) {
                //if (!we.containsWord(wordInListOfNeighbours))
                    //referenceSL.get(iter.next().getKey()).remove(wordInListOfNeighbours);
            //}
            //if (!we.containsWord(iter.next().getKey())) referenceSL.remove(iter.next().getKey());
            //if (iter.next().getValue().size() == 0) referenceSL.remove(iter.next.get());
        //}

    //}

    public void printWOLF() {
        System.out.println("WOLF:");
        for (int i = 0; i < this.ID2SYNSET.size(); i++) {
            for (Map.Entry<String,HashSet<String>> entry : this.WORD2CLOSEWORDS.entrySet()) {
                System.out.println("WORD: " + entry.getKey() + " NEIGHBOURS:  are not printable yet"  );
            }
        }
    }

    public void printWORD2CLOSEWORDS() {
        System.out.println("ID2SYNSET:");
        for (int i = 0; i < this.WORD2CLOSEWORDS.size(); i++) {
            for (Map.Entry<String,HashSet<String>> entry : this.WORD2CLOSEWORDS.entrySet()) {
                System.out.println("WORD: " + entry.getKey() + " NEIGHBOURS:  are not printable yet"  );
            }
        }
    }


}
