package edu.yu.parallel;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class StudentThread extends Thread{
    private CyclicBarrier cb;
    private Semaphore s;
    //private AtomicInteger studentsInWaitingRoom;
    private Student student;
    public StudentThread(CyclicBarrier cb, Student student, AtomicInteger studentsInWaitingRoom){
        this.cb = cb;
        //this.s = s;
        //this.studentsInWaitingRoom = studentsInWaitingRoom;
        this.student = student;
    }


    @Override
    public void run() {
        try {

                student.learnFromTutor();
                cb.await();
                student.leaveAfterLearning();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
    }
}
