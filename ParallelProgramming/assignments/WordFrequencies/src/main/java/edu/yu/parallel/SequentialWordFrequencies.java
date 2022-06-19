package edu.yu.parallel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SequentialWordFrequencies implements WordFrequencies {
    HashMap<String, Integer> masterMap = new HashMap<>();
    int maxVal = 1; //assuming theres at least one character in the text file
    Set<String> oneOccurSet = new LinkedHashSet<>();
    Set<String> maxOccurSet = new LinkedHashSet<>();
    public void doIt(String fileName) throws IOException {
        masterMap.clear();
        oneOccurSet.clear();
        maxOccurSet.clear();
        maxVal = 1;
        BufferedReader objReader = null;
        try {
            String strCurrentLine;
            StringBuilder curModLine = new StringBuilder();
            String[] curArray;
            Integer prevVal;


            objReader = new BufferedReader(new FileReader(fileName));

            while ((strCurrentLine = objReader.readLine()) != null) {
                curModLine.append(strCurrentLine.replaceAll("[^a-zA-Z0-9]", " ")).append(" ");
            }
                curArray = curModLine.toString().split(" ");
                for(String word: curArray) {
                    if(word.equals("")){
                        continue;
                    }
                    if ((prevVal = masterMap.putIfAbsent(word, 1)) != null) {
                        if(prevVal + 1 > maxVal){
                            maxVal = prevVal + 1;
                        }
                        masterMap.put(word, prevVal + 1);
                    }
                }

            //Set<String> oneOccurSet = new LinkedHashSet<>();
            for(Map.Entry<String, Integer> word: masterMap.entrySet()){
                if(word.getValue() == 1){
                    oneOccurSet.add(word.getKey());
                }
            }

            //Set<String> maxOccurSet = new LinkedHashSet<>();
            for(Map.Entry<String, Integer> word: masterMap.entrySet()){
                if(word.getValue() == maxVal){
                    maxOccurSet.add(word.getKey());
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {
                if (objReader != null)
                    objReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Map<String, Integer> getWordFrequencies() {
        return masterMap;//TODO deep copy???
    }

    public Set<String> wordsWithExactlyOneOccurrence() {
        /*Set<String> oneOccurSet = new LinkedHashSet<>();
        for(Map.Entry<String, Integer> word: masterMap.entrySet()){
           if(word.getValue() == 1){
               oneOccurSet.add(word.getKey());
           }
        }*/
        return oneOccurSet;
    }

    public Set<String> wordsWithMaxOccurrence() {
        /*Set<String> maxOccurSet = new LinkedHashSet<>();
        for(Map.Entry<String, Integer> word: masterMap.entrySet()){
            if(word.getValue() == maxVal){
                maxOccurSet.add(word.getKey());
            }
        }*/
        return maxOccurSet;
    }

    public int maxWordFrequency() {
        return maxVal;
    }
}
