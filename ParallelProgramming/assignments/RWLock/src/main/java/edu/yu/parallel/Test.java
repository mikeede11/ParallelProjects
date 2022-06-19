package edu.yu.parallel;

import java.util.ArrayList;
import java.util.Random;

public class Test {
   // static volatile ArrayList<Integer> al = new ArrayList<>();
    static volatile int num = 1;
    Random r = new Random();
    RWLock lock = new RWLock();
    private class Reader extends Thread{
        @Override
        public void run(){
            lock.lockRead();
            try {
                //sleep(1000);
                System.out.println(num + " is the READ value");
           /* } catch (InterruptedException e) {
                e.printStackTrace();*/
            } finally {
                lock.unlock();
            }
        }
    }
    private class Writer extends Thread{
        @Override
        public void run(){
            lock.lockWrite();
             try {
                 int added = r.nextInt(100);
                 //al.add(0, added);
                 num = added;
                 System.out.println(added + " was WRITTEN " + num + " is now the value");
             }
             finally {
                 lock.unlock();
             }

        }
    }
    public void execute(){
        int readerThreads = 5;
        int writerThreads = 5;
        //al.add(0,1);
        for(int i = 0; i < readerThreads; i++){
            new Reader().start();
            new Writer().start();

        }
        /*for(int i = 0; i < writerThreads; i++){
            new Writer().start();
        }*/

    }
    public static void main(String[] args){
        Test t = new Test();
        t.execute();
    }
}
