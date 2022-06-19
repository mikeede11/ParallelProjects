package edu.yu.parallel;

import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelWordFrequencies implements WordFrequencies {
        HashMap<String, Integer> masterMap = new HashMap<>();
        String[] curArray;
        Set<String> oneOccurSet = new LinkedHashSet<>();
        Set<String> maxOccurSet = new LinkedHashSet<>();
        int maxVal = 1; //assuming theres at least one character in the text file
        int cores = Runtime.getRuntime().availableProcessors();

        public void doIt(String fileName) throws IOException {
            System.out.println(cores);
            masterMap.clear();
            oneOccurSet.clear();
            maxOccurSet.clear();
            maxVal = 1;
            BufferedReader objReader = null;
            try {
                String strCurrentLine;
                StringBuilder curModLine = new StringBuilder();

                Integer prevVal;


                objReader = new BufferedReader(new FileReader(fileName));
                ArrayList<Thread> threads = new ArrayList<>();
                while ((strCurrentLine = objReader.readLine()) != null) {
                    curModLine.append(strCurrentLine.replaceAll("[^a-zA-Z0-9]", " ")).append(" ");
                }
                curArray = curModLine.toString().split(" ");
                for(int i = 0; i < curArray.length ;i += curArray.length/cores + 1){
                    if(i >= curArray.length - curArray.length/cores){//lastsegment
                        System.out.println(i + "-" + (i + curArray.length / cores) + "!!!" );
                        DoItThread dit = new DoItThread(i, curArray.length - 1);
                        threads.add(dit);
                        dit.start();
                    }
                    else {
                        int end = i + curArray.length / cores;
                        System.out.println(i + "-" +  end);
                        DoItThread dit = new DoItThread(i, end);
                        threads.add(dit);
                        dit.start();
                    }
                }
                for(Thread t: threads){
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for(Map.Entry<String, Integer> word: masterMap.entrySet()){
                    if(word.getValue() == 1){
                        oneOccurSet.add(word.getKey());
                    }
                }
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

    private class DoItThread extends Thread {
            private int start;
            private int end;
            public DoItThread(int start, int end){
                this.start = start;
                this.end = end;
            }

            @Override
            public void run(){
                Integer prevVal = null;
                for(int i = start; i <= end && i < curArray.length; i++) {
                    String word = curArray[i];
                    if (word.equals("")) {
                        continue;
                    }
                    synchronized (ParallelWordFrequencies.class){
                    if ((prevVal = masterMap.putIfAbsent(word, 1)) != null) {
                        if (prevVal + 1 > maxVal) {
                            maxVal = prevVal + 1;
                        }
                        masterMap.put(word, prevVal + 1);
                    }
                    }
                }
            }
    }

}
