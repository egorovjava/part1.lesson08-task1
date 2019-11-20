package com.gmail.egorovsonalexey;

import java.util.concurrent.Callable;

class GameThread implements Callable<int[][]> {

    int threadNumber;
    int threadsCount;
    GameField field;

    GameThread(GameField f, int n, int count) {
        field = f;
        threadNumber = n;
        threadsCount = count;
    }

    @Override
    public int[][] call() {
        int[][] result = new int[field.getSize()][field.getSize()];
        field.step(threadNumber, threadsCount, result);
        return result;
    }
}
