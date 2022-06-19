package edu.yu.parallel;

/** Serial implementation of the MatrixOps interface.
 *
 * @author Avraham Leff
 */

public class SerialMatrixOps implements MatrixOps {
    //This class displays Correctness!
    public SerialMatrixOps() {
    }
    //INVARIANT : matrices are n x n. They have same dimensions
    //loop unrolling and cache tricks can speed this up.
    @Override
    public double[][] add(final double[][] a, final double[][] b) {
        int columns = a.length;//all rows have same # of columns (n x n)
        double[][] resultMatrix = new double[a.length][columns];
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < columns; j++){
                resultMatrix[i][j] = a[i][j] + b[i][j];
            }
        }
        return resultMatrix;
    } // add

    @Override
    public double[][] multiply(final double[][] a, final double[][] b) {
        int columns = a[1].length;//all rows have same # of columns (n x n)
        double[][] resultMatrix = new double[a.length][columns];
        double[][] intermediateMatrixA = new double[a.length][columns];
        double[][] intermediateMatrixB = new double[a.length][columns];
            //for (int i = 0; i < a.length; i++) {//this loop makes n matrices
                //these loops make the series of matrices to be added
                for(int k = 0; k < columns; k++) {
                    for(int h = 0; h < columns; h++) {
                        for (int j = 0; j < columns; j++) {
                            intermediateMatrixA[h][j] = a[h][k] * b[k][j];
                            //intermediateMatrixB[i][j] = a[k][i+1] * b[i+1][j];
                            //                    resultMatrix[i][k] += a[i][j] * b[j][k];
                        }
                    }
                    resultMatrix = add(resultMatrix, intermediateMatrixA);
                }
        return resultMatrix;

    } // multiply

}