    Ce projet consiste à implémenter la méthode « retrofitting » basée sur un graphe, pour utiliser la relation 
sémantique entre les mots, afin d’améliorer la performance des vecteurs de lexiques.
    
    L'algorithme est évalué sur une tâche de similarité lexicale, puis sur une tâche d'analyse de sentiments sur
un corpus de critiques de films. 

    

REQUIRED FILES

-Vecteurs de mots:
 FR: vectos50
 EN: vectors_datatxt_250_sg_w10_i5_c500_gensim_clean


-Ressources sémantiques:

 EN: PPDB data, ppdb-1.0-xl-lexical (PPDB, paraphrase database)
 FR: wolf-1.0b4.xml (WOLF)

-Pour l'évaluation:

 EN (similarité lexicale) : ws353.txt
 FR (corpus d'analyse de sentiments) : rg65_french.txt
     Pour entraîner un Perceptron (classifieur) : stanford_raw_dev.txt
                                                  stanford_raw_test.txt
                                                  stanford_raw_train.txt


USAGE

javac -cp commons-math3-3.6.1.jar:. Retrofitting.java

java -cp commons-math3-3.6.1.jar:. Retrofitting fr 5 50
(utiliser retrofitting avec des vecteurs de mots en français de dimension 50)

java -cp commons-math3-3.6.1.jar:. Retrofitting en 5 150
(utiliser retrofitting avec des vecteurs de mots en anglais de dimension 150)

RÉFÉRENCE
Retrofitting Word Vectors to Semantic Lexicons https://www.cs.cmu.edu/~hovy/papers/15HLT-retrofitting-word-vectors.pdf

MEMBRES DU PROJET:YI CHUN SUNG / HERMES MARTINEZ