package edu.yu.parallel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/** Extends TutorOffice to implement a thread-safe and performant driver
 * program in which a Tutor interacts with multiple Student instance such that
 * the the constraints and invariants of the BusyTutor requirements document
 * are maintained.
 *
 * Students must implement the task methods that are not implemented in the
 * base class.  Students are free to add whatever state and methods they feel
 * are needed for the implementation.
 *
 * @author Avraham Leff
 */


public class TutorOfficeImpl extends TutorOffice {
    private final AtomicInteger studentsInWaitingRoom = new AtomicInteger();
    private Semaphore sem;
    //private Semaphore tutorSession;
    private CyclicBarrier cb;
    //protected static boolean teaching;
    LinkedBlockingQueue<Thread> queue;
    TutorThread profLeff;

    public TutorOfficeImpl() {
        // your code goes here
        super();
        sem = new Semaphore(MAX_STUDENTS_IN_OFFICE);//if its in the office the student already said notify - once it has a
        //tutorSession = new Semaphore(2);
        cb = new CyclicBarrier(2, () -> {
            System.out.println("Taught one student");
        });
        //teaching = false;
        queue = new LinkedBlockingQueue<Thread>();

    }

    @Override
    public Void startTutoringService() {
        // your code goes here
        Tutor tutor = new Tutor();
        profLeff = new TutorThread(cb, queue, studentsInWaitingRoom,tutor);
        profLeff.start();
        return null;
    }

    @Override
    public Void newStudentTask() {
        // your code goes here
        Student student = new Student();

            student.enter();//log

            if (queue.size() < MAX_STUDENTS_IN_OFFICE) {//room in classrom?
                student.notTurnedAway();//yes!
                //studentsInWaitingRoom.getAndIncrement();//im in the waiting room!
                queue.add(new StudentThread(cb, student, studentsInWaitingRoom));
                synchronized (TutorOfficeImpl.class) {
                    TutorOfficeImpl.class.notify();//
                }
            } else {
                student.leaveBecauseNoRoom();
            }
        return null;
    }
    public static void main(String args[]){
        TutorOfficeImpl toi = new TutorOfficeImpl();
        Logger logger = LogManager.getLogger(TutorOfficeImpl.class);
        logger.info("hi");
        toi.startTutoringService();
        for(int i = 0; i < 50; i++){
            toi.newStudentTask();
        }

        //toi.startTutoringService();
    }
}


