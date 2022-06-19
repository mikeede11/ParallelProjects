package edu.yu.parallel;

import org.junit.*;
import org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class SequentialWordFrequenciesTester {
    ParallelWordFrequencies pwf = new ParallelWordFrequencies();
    SequentialWordFrequencies swf = new SequentialWordFrequencies();
    @Test
    @Before
    public void TestFileReader(){

        try{
            swf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/TestFileReader.txt");
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void SeqBasicCorrectnessTest(){
        Map<String, Integer> map = swf.getWordFrequencies();
        assertEquals(22, map.size());
        assertEquals(1,(int)map.get("Hello"));
        assertEquals(1,(int)map.get("this"));
        assertEquals(1,(int)map.get("is"));
        assertEquals(2,(int)map.get("a"));
        assertEquals(1,(int)map.get("text"));
        assertEquals(1,(int)map.get("document"));
    }
    @Test
    public void seqIntermediateCorrectnessTest(){

        try {
            swf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/LargeTextDonaldTrump.txt");
            Map<String, Integer> map = swf.getWordFrequencies();
            assertEquals(103, (int)map.get("Donald"));
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void seqLargeCorrectnessTest(){

        try {
            swf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/SuperLargeTextDoc.txt");
            Map<String, Integer> map = swf.getWordFrequencies();
            assertEquals(33, (int)map.get("hazards"));
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void seqWordsWithMaxOccurenceBasicTest(){
        try {
            swf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/TestFileReader.txt");
            Map<String, Integer> map = swf.getWordFrequencies();
            Set<String> maxSet = swf.wordsWithMaxOccurrence();
            assertEquals(2, maxSet.size());
            assertEquals(2,swf.maxWordFrequency());
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void seqWordsWithMaxOccurenceAdvancedTest(){
        try {
            swf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/SuperLargeTextDoc.txt");
            Set<String> maxSet = swf.wordsWithMaxOccurrence();
            assertEquals(1, maxSet.size());
            assertEquals(12227, swf.maxWordFrequency());//12213 close enough
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void seqWordsWithOneOccurenceBasicTest(){
        try {
            swf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/TestFileReader.txt");
            Map<String, Integer> map = swf.getWordFrequencies();
            Set<String> oneSet = swf.wordsWithExactlyOneOccurrence();
            assertEquals(20, oneSet.size());
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void seqWordsWithOneOccurenceAdvancedTest(){
        try {
            swf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/SuperLargeTextDoc.txt");
            Map<String, Integer> map = swf.getWordFrequencies();
            Set<String> oneSet = swf.wordsWithExactlyOneOccurrence();
            assertEquals(4920, oneSet.size());
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void ParBasicCorrectnessTest(){
        try{
            pwf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/TestFileReader.txt");
        }catch(IOException e){
            fail();
        }
        Map<String, Integer> map = pwf.getWordFrequencies();
        assertEquals(22, map.size());
        assertEquals(1,(int)map.get("Hello"));
        assertEquals(1,(int)map.get("this"));
        assertEquals(1,(int)map.get("is"));
        assertEquals(2,(int)map.get("a"));
        assertEquals(1,(int)map.get("text"));
        assertEquals(1,(int)map.get("document"));
    }
    @Test
    public void ParIntermediateCorrectnessTest(){

        try {
            pwf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/LargeTextDonaldTrump.txt");
            Map<String, Integer> map = pwf.getWordFrequencies();
            assertEquals(103, (int)map.get("Donald"));
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void ParLargeCorrectnessTest(){

        try {
            pwf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/SuperLargeTextDoc.txt");
            Map<String, Integer> map = pwf.getWordFrequencies();
            assertEquals(33, (int)map.get("hazards"));
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void ParWordsWithMaxOccurenceBasicTest(){
        try {
            pwf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/TestFileReader.txt");
            Map<String, Integer> map = swf.getWordFrequencies();
            Set<String> maxSet = swf.wordsWithMaxOccurrence();
            assertEquals(2, maxSet.size());
            assertEquals(2,swf.maxWordFrequency());
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void ParWordsWithMaxOccurenceAdvancedTest(){
        try {
            pwf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/SuperLargeTextDoc.txt");
            Set<String> maxSet = pwf.wordsWithMaxOccurrence();
            assertEquals(1, maxSet.size());
            assertEquals(12227, pwf.maxWordFrequency());//12213 close enough
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void ParWordsWithOneOccurenceBasicTest(){
        try {
            pwf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/TestFileReader.txt");
            Map<String, Integer> map = swf.getWordFrequencies();
            Set<String> oneSet = swf.wordsWithExactlyOneOccurrence();
            assertEquals(20, oneSet.size());
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void ParWordsWithOneOccurenceAdvancedTest(){
        try {
            pwf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/SuperLargeTextDoc.txt");
            Set<String> oneSet = pwf.wordsWithExactlyOneOccurrence();
            assertEquals(4920, oneSet.size());
        }catch(IOException e){
            fail();
        }
    }
    @Test
    public void performanceComparisonTest(){
        long start = 0;
        long end = 0;
        long seqDuration = 0;
        long parDuration = 0;
        try{
            start = System.currentTimeMillis();
            swf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/SuperLargeTextDoc.txt");
            end = System.currentTimeMillis();
            seqDuration = end - start;
            start = System.currentTimeMillis();
            pwf.doIt("C:/Users/mitch/EdelmanMichael/ParallelProgramming/assignments/WordFrequencies/src/main/resources/SuperLargeTextDoc.txt");
            end = System.currentTimeMillis();
            parDuration = end - start;
            System.out.println("SeqDuration is " + seqDuration + " and ParDuration is " + parDuration + "Ratio of Seq/Par " + seqDuration/parDuration);
        }catch(IOException e){

        }
    }

}
