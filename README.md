# WordEmbedding Retrofitting

## Description

    This project is to implement the method « retrofitting » which use semantic relationship between pairs of words, for improving performance of word vectors. This algorithm is evaluated on two tasks : 
    - semantic similarity between words (for english word vectors)
    - opinion mining on movie reviews (for french word vectors)

## Organization of files

  -Vectors of words:
  
    FR:  `vectos50` 
          (https://dl.dropboxusercontent.com/u/108240016/vectors50.bz2)
    EN:  `vectors_datatxt_250_sg_w10_i5_c500_gensim_clean` 
          (https://dl.dropboxusercontent.com/u/108240016/vectors_datatxt_250_sg_w10_i5_c500_gensim_clean.tar.bz2)

  -Semantic relation:

    EN: `ppdb-1.0-xl-lexical`
         (http://www.cis.upenn.edu/~ccb/ppdb/release-1.0/ppdb-1.0-xl-lexical.gz)   
    FR: `wolf-1.0b4.xml`
         (https://gforge.inria.fr/frs/download.php/file/33496/wolf-1.0b4.xml.bz2)

  -Evaluation:

    EN: `ws353.txt`
    FR: `rg65_french.txt`

  -Data for training a perceptron classifier 

    -`stanford_raw_dev.txt`
    -`stanford_raw_test.txt`
    -`stanford_raw_train.txt`

## How to test

    - run `javac -cp commons-math3-3.6.1.jar:. Retrofitting.java`

    - run `java -cp commons-math3-3.6.1.jar:. Retrofitting fr 5 50` 
      (use retrofitting with vectors of words for french of dimension 50)

    - run `java -cp commons-math3-3.6.1.jar:. Retrofitting en 5 150` 
      (use retrofitting with vectors of words for english of dimension 150)

## What needs to be done

    - change values if parameters α, β in algorithm

## Reference

    - Retrofitting Word Vectors to Semantic Lexicons https://www.cs.cmu.edu/~hovy/papers/15HLT-retrofitting-word-vectors.pdf
