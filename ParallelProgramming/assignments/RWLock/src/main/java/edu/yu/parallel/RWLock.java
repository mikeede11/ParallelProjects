package edu.yu.parallel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RWLock implements RWLockInterface {
    private static boolean writing = false;
    private Object writeLock = new Object();
    //private boolean reading;
    private static volatile int readingCounter = 0;
    private static volatile int waitingCounter = 0;
    //private int ticket;//whole impl. depends on each thread owning a seperate version.
    //private static int nextWriteTicket;

    private static Queue<Integer> writeOrder = new LinkedList<>();
    //private static int nextTicket;// = writeOrder.peek();


    public RWLock(){
        super();
    }
    @Override
    public void lockRead() {
        synchronized (writeLock) {//this can be put here b/c even if two threads call this at same time you dont want them to interleave these statements with a wrtie lock
            //this code is just organizing it so multiple reads can happenAFTER this method call.
            int ticket = ++waitingCounter;
            while (writing || (writeOrder.peek() != null && ticket > writeOrder.peek())) {
                try {
                    writeLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            readingCounter++;
        }
//wait if writing is going on
    }

    @Override
    public void lockWrite() {
synchronized (writeLock) {//this just tells me no one else is writing.I still need to ensure no one is reading
    int ticket = ++waitingCounter;
    writeOrder.add(ticket);
    while (readingCounter > 0 || ticket != writeOrder.peek()) {

        try {
            writeLock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    writeOrder.remove();
    writing = true;
}
    }

    @Override
    public void unlock() {
        synchronized(writeLock) {
            if (readingCounter > 0) {
                readingCounter--;
            } else {
                writing = false;
            }
            //waitingCounter--;
            writeLock.notifyAll();
        }
        //if write unlock - notifyAll or just notify all other next tread threads in the queue.
        //if read unlock - same thing (or just next one if its a write)
    }
}
