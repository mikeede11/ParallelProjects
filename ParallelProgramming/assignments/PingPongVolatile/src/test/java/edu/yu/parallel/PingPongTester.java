package edu.yu.parallel;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PingPongTester {
    @Test
    public void pingPongAlternateTest(){
        Queue<PingPongThread.Turn> q = new ConcurrentLinkedQueue<>();
        final PingPongVolatile ppv = new PingPongVolatile(PingPongThread.Type.PING, 10, q);
        final PingPongVolatile ppv2 = new PingPongVolatile(PingPongThread.Type.PONG, 10, q);
        ppv.start();
        ppv2.start();
        try {
            ppv.join();
            ppv2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PingPongThread.Turn prevTurn = ppv.getEmitterQueue().poll();
        PingPongThread.Turn currTurn = ppv.getEmitterQueue().poll();
        while(!q.isEmpty()) {
            assertNotEquals(prevTurn, currTurn);
            prevTurn = currTurn;
            currTurn = q.poll();
        }
    }
}
