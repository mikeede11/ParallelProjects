
package edu.yu.parallel;

/** Naive, and incorrect, implementation of PingPongThread.
 *
 * @author Avraham Leff
 */

        import java.util.Queue;

public class PingPongVolatile extends PingPongThread {
    static volatile Type whosTurn = Type.PING;
    private Type otherType;

    public PingPongVolatile
            (final Type type, final int maxIterations, final Queue<Turn> emitterQueue) {
        super(type, maxIterations, emitterQueue);
        if(type.equals(Type.PING)){
            this.otherType = Type.PONG;
        }
        else{
            this.otherType = Type.PING;
        }

    }

    @Override
    public void acquire() {
        while(whosTurn == otherType) {
        }
    }

    @Override
    public void release() {
        whosTurn = otherType;
    }

    @Override
    public void done() {
        // no-op
            /*while (!super.emitterQueue.isEmpty()) {
                //testQueue.add(super.emitterQueue.poll());
                System.out.println(super.emitterQueue.poll());
            }*/

    }

    /*public static void main(String[] args){
        Queue<Turn> q = new ConcurrentLinkedQueue<>();
        final PingPongThread ppv = new PingPongVolatile(Type.PING, 10, q);
        final PingPongThread ppv2 = new PingPongVolatile(Type.PONG, 10, q);
        ppv.start();
        ppv2.start();
        try {
            ppv.join();
            ppv2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(!q.isEmpty()) {
            System.out.println(q.poll());
        }
    }*/
} // class