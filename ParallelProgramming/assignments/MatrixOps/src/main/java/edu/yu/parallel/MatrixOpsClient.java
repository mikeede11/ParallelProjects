package edu.yu.parallel;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MatrixOpsClient {
    public static void main(String[] args) {
        int n = 4;
        System.setProperty(MatrixOps.addThreshold, "512");
        System.setProperty(MatrixOps.multiplyThreshold, "512");
        SerialMatrixOps smo = new SerialMatrixOps();
        JDKFJMatrixOps fjmo = new JDKFJMatrixOps();
        int dims = 2048;
        Random random = new Random();
        //rand = random.nextInt(6);
        double[][] testMatrixA = new double[dims][dims];
        for (int i = 0; i < dims; i++) {
            for (int j = 0; j < dims; j++) {
                testMatrixA[i][j] = random.nextInt(6);
            }
        }
        double[][] testMatrixB = new double[dims][dims];
        for (int i = 0; i < dims; i++) {
            for (int j = 0; j < dims; j++) {
                testMatrixB[i][j] = random.nextInt(6);
            }
        }
        long startTime = 0;
        long endTime = 0;
        long duration = 0;
        startTime = System.currentTimeMillis();
        try {
            fjmo.multiply(testMatrixA, testMatrixB);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println(duration);
        startTime = System.currentTimeMillis();

        smo.multiply(testMatrixA, testMatrixB);
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println(duration);

        /*double[][] matrixA = {
                {3,2,0,3},
                {7,2,1,4},
                {5,4,3,2},
                {1,2,3,4}
        };
        double[][] matrixB = {
                {1,3,2,1},
                {3,3,2,2},
                {6,0,1,1},
                {3,2,2,1}
        };
        *//*double[][] matrixA = {
                {1,2},
                {3,4}
        };
        double[][] matrixB = {
                {2,1},
                {4,3}
        };*//*
        //double[][] rma = smo.add(matrixA,matrixB);
        double[][] rma = null;
        double[][] rmm = null;
        try {
            rma = fjmo.add(matrixA,matrixB);
            //rmm = fjmo.multiply(matrixA,matrixB);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Interrupted Exception occurred!");
            e.printStackTrace();
        }
        System.out.print("The Array is :\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(rma[i][j] + "  ");
            }
            System.out.println();
        }*/
        //double[][] rmm = smo.multiply(matrixA,matrixB);
       /* System.out.print("The Array is :\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(rmm[i][j] + "  ");
            }
            System.out.println();
        }*/

    }
}
