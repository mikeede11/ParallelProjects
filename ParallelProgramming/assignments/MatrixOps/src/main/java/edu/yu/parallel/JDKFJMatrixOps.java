package edu.yu.parallel;

/** Implements Matrix "add" and "multiply" using the JDK 7 "fork/join"
 * framework to implement a "divide-and-conquer" approach.
 *
 * @author Avraham Leff
 */

import java.io.InputStreamReader;
import java.util.concurrent.*;

public class JDKFJMatrixOps implements MatrixOps {

    public JDKFJMatrixOps() {
        // ForkJoinPool ivar initialized at declaration point
    }

    @Override
    public double[][] add(final double[][] a, final double[][] b)
            throws InterruptedException, ExecutionException
    {
        double[][] resMatrix = new double[a.length][a.length];
        int parallelism = Runtime.getRuntime().availableProcessors();
        FJAddTask task = new FJAddTask(a,b, 0 , a.length, resMatrix);
        final ForkJoinPool fjpool = new ForkJoinPool(parallelism);
        fjpool.invoke(task);
        fjpool.shutdown();

        return resMatrix;
    }

    @Override
    public double[][] multiply(final double[][] a, final double[][] b)
            throws InterruptedException, ExecutionException
    {
        double[][] resMatrix = new double[a.length][a.length];
        int parallelism = Runtime.getRuntime().availableProcessors();
        FJMultTask task = new FJMultTask(a,b, 0 , a.length,  new double[a.length][a.length]);
        final ForkJoinPool fjpool = new ForkJoinPool(parallelism);
        resMatrix = fjpool.invoke(task);
        fjpool.shutdown();

        return resMatrix;
    }

    class FJAddTask extends RecursiveAction {
        private final double[][] a;
        private final double[][] b;
        private int startingRowIndex;
        private int startingColIndex;
        private int endingRowIndex;
        private int endingColIndex;
        //private int dims;
        private int threshold;
        private double[][] resultMatrix;
        FJAddTask(final double[][] a, final double[][] b, int startingRowindex, int endingRowIndex,  double[][] resultMatrix){
            this.a = a;
            this.b = b;
            this.resultMatrix = resultMatrix;
            //this.row = row;
            this.startingRowIndex = startingRowindex;
            this.startingColIndex = startingColIndex;
            this.endingRowIndex = endingRowIndex;
            this.endingColIndex = endingColIndex;
            //this.dims = dims;
            this.threshold = Integer.parseInt(System.getProperty(addThreshold));

        }

        @Override
        protected void compute() {
            if(endingRowIndex - startingRowIndex <= threshold){
                add(a,b, this.startingRowIndex,  this.endingRowIndex,  resultMatrix);
            }
            else{
                /*FJAddTask ulCorner = new FJAddTask(a,b, this.startingRowIndex, this.startingColIndex, (endingRowIndex - startingRowIndex)/2, (endingColIndex - startingColIndex)/2, resultMatrix);//dims is acting as size
                FJAddTask urCorner = new FJAddTask(a,b, this.startingRowIndex, (endingColIndex - startingColIndex)/2,(endingRowIndex - startingRowIndex)/2 , endingColIndex, resultMatrix);//dims is acting as starting index //FIXME does double work make a row ending variable
                FJAddTask blCorner = new FJAddTask(a,b, (endingRowIndex - startingRowIndex)/2, this.startingColIndex, endingRowIndex , (endingColIndex - startingColIndex)/2,resultMatrix);
                FJAddTask brCorner = new FJAddTask(a,b,startingRowIndex,(endingColIndex - startingColIndex)/2, endingRowIndex, endingColIndex, resultMatrix);
                invokeAll(ulCorner,urCorner,blCorner, brCorner);*/
                FJAddTask topHalf = new FJAddTask(a,b, startingRowIndex, startingRowIndex + ((endingRowIndex - startingRowIndex)/2), resultMatrix);
                FJAddTask bottomHalf = new FJAddTask(a,b, startingRowIndex + ((endingRowIndex - startingRowIndex)/2) ,endingRowIndex,resultMatrix);
                invokeAll(topHalf,bottomHalf);
                //no need to join were dealing with same 2d array
            }

        }


        protected void add(final double[][] a, final double[][] b, int startingRowIndex, int endingRowIndex,  double[][] resultMatrix) {
            for(int i = startingRowIndex; i < endingRowIndex; i++){
                for(int j = 0; j < a.length; j++){
                    resultMatrix[i][j] = a[i][j] + b[i][j];
                }
            }
        } // add
    }

    class FJMultTask extends RecursiveTask<double[][]>{
        public FJMultTask(double[][] a, double[][] b, int startingIndex, int endingIndex, double[][] resultMatrix) {
            this.a = a;
            this.b = b;
            this.startingIndex = startingIndex;
            this.endingIndex = endingIndex;
            this.resultMatrix = resultMatrix;
            this.threshold = Integer.parseInt(System.getProperty(multiplyThreshold));
        }

        private final double[][] a;
        private final double[][] b;
        private int startingIndex;
        private int endingIndex;
        private double[][] resultMatrix;
        private int threshold;


        @Override
        protected double[][] compute() {
            if(endingIndex-startingIndex <= threshold){
               return multiply(a,b,startingIndex, endingIndex, new double[a.length][a.length]);
            }
            else {
                FJMultTask left = new FJMultTask(a,b,startingIndex, startingIndex + ((endingIndex - startingIndex)/2), resultMatrix);
                FJMultTask right = new FJMultTask(a,b,startingIndex + ((endingIndex - startingIndex)/2),endingIndex,resultMatrix);
                left.fork();
                double[][] rightRes = right.compute();
                double[][] leftRes = left.join();
                try {
                    return add(leftRes,rightRes);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        protected double[][] multiply(final double[][] a, final double[][] b,int startingIndex, int endingIndex, double[][] resultMatrix) {
            int columns = a.length;//all rows have same # of columns (n x n)
           // double[][] resultMatrix = new double[a.length][columns];//pass in
            double[][] intermediateMatrixA = new double[a.length][columns];//passin amatrix to work with NO instead pass in a new double[][] evryone has their own
            //these loops make the series of matrices to be added
            for(int k = startingIndex; k < endingIndex; k++) {
                for(int h = 0; h < columns; h++) {
                    for (int j = 0; j < columns; j++) {
                        intermediateMatrixA[h][j] = a[h][k] * b[k][j];
                        //intermediateMatrixB[i][j] = a[k][i+1] * b[i+1][j];
                        //                    resultMatrix[i][k] += a[i][j] * b[j][k];
                    }
                }
                try {
                    resultMatrix = add(resultMatrix, intermediateMatrixA);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return resultMatrix;

        } // multiply
    }
} // JDKFJMatrixOps