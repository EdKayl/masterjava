package ru.javaops.masterjava.matrix;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(final int[][] matrixA, final int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        //
        CompletionService completionService = new ExecutorCompletionService(executor);

        //transpose matrix B
        int[][] transposedMatrixB = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                transposedMatrixB[j][i] = matrixB[i][j];
            }
        }
        IntStream.range(0, matrixSize)
                .forEach(row -> completionService.submit(() -> {
                            int[] columnFromB;
                            int rowFromA[] = matrixA[row];
                            for (int col = 0; col < matrixSize; col++) {
                                columnFromB = transposedMatrixB[col];
                                for (int j = 0; j < matrixSize; j++) {
                                    matrixC[row][col] += rowFromA[j] * columnFromB[j];
                                }
                            }
                            return null;
                        })
                );
        //get result
        for (int i = 0; i < matrixSize; i++) {
            try {
                completionService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        int[] columnFromB = new int[matrixSize];
        int[][] transposedMatrixB = new int[matrixSize][matrixSize];

        //transpose matrix B
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                transposedMatrixB[j][i] = matrixB[i][j];
            }
        }

        for (int row = 0; row < matrixSize; row++) {
            int rowFromA[] = matrixA[row];
            for (int col = 0; col < matrixSize; col++) {
                columnFromB = transposedMatrixB[col];
                for (int j = 0; j < matrixSize; j++) {
                    matrixC[row][col] += rowFromA[j] * columnFromB[j];
                }
            }
        }
        return matrixC;
    }

    public static int[][] singleThreadMultiplyOld(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
