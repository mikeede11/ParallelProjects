package edu.yu.parallel;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TutorThread extends Thread{
    private CyclicBarrier cb;
    private Semaphore s;
    private AtomicInteger studentsInWaitingRoom;
    private Tutor profLeff;
    LinkedBlockingQueue<Thread> queue;
    public TutorThread(CyclicBarrier cb, LinkedBlockingQueue<Thread> queue, AtomicInteger studentsInWaitingRoom, Tutor profLeff){
        this.cb = cb;
        this.s = s;
        this.studentsInWaitingRoom = studentsInWaitingRoom;
        this.profLeff = profLeff;
        this.queue = queue;
    }
    public void run() {
        //Tutor profLeff = new Tutor();
        while (true) {
            try {
                while(queue.size() == 0) {
                    synchronized (TutorOfficeImpl.class) {
                        TutorOfficeImpl.class.wait();
                    }
                }
                profLeff.teachStudent();
                queue.poll().start();
                cb.await();


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            //signal to student im about to teach one of you get the mutex
            //sem1.release();
        }
    }
}
