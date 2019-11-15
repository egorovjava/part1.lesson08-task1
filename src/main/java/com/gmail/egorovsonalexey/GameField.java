package com.gmail.egorovsonalexey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

class GameField {

    private int[][] position;
    private int[][] nextPosition;
    private int[][] neighbors;
    private int[][] nextNeighbors;

    private int[][][] city;

    private int size;

    GameField(int[][] startPosition) throws IncorrectFieldException {

        if(startPosition == null || startPosition.length == 0) {
            throw new IncorrectFieldException("The game field is empty.");
        }

        size = startPosition.length;
        position = new int[size][size];
        nextPosition = new int[size][size];
        neighbors = new int[size][size];
        nextNeighbors = new int[size][size];

        for(int i = 0; i < startPosition.length; i++) {
            if(startPosition[i] == null || startPosition[i].length != size) {
                throw new IncorrectFieldException("The game field isn't square.");
            } else {
                for(int j = 0; j < size; j++) {
                    if(startPosition[i][j] == 1) {
                        increaseNeighboursCount(i, j, neighbors);
                        increaseNeighboursCount(i, j, nextNeighbors);
                        position[i][j] = 1;
                    }
                }
            }
        }
    }

    private void increaseNeighboursCount(int x, int y, int[][] neighborhood) {
        int left = (x + size - 1) % size;
        int right = (x + size + 1) % size;
        int top = (y + size - 1) % size;
        int bottom = (y + size + 1) % size;

        //clock otherwise
        neighborhood[left][top]++;
        neighborhood[left][y]++;
        neighborhood[left][bottom]++;
        neighborhood[x][bottom]++;
        neighborhood[right][bottom]++;
        neighborhood[right][y]++;
        neighborhood[right][top]++;
        neighborhood[x][top]++;
    }

    private void decreaseNeighborsCount(int x, int y, int[][] neighborhood) {
        int left = (x + size - 1) % size;
        int right = (x + size + 1) % size;
        int top = (y + size - 1) % size;
        int bottom = (y + size + 1) % size;

        //clock otherwise
        neighborhood[left][top]--;
        neighborhood[left][y]--;
        neighborhood[left][bottom]--;
        neighborhood[x][bottom]--;
        neighborhood[right][bottom]--;
        neighborhood[right][y]--;
        neighborhood[right][top]--;
        neighborhood[x][top]--;
    }

    private void processCell(int x, int y, int[][] neighbourhood) {
        int neighborCount = neighbors[x][y];
        int value = position[x][y];

        if(value == 0 && neighborCount == 3) {
            nextPosition[x][y] = 1;
            increaseNeighboursCount(x, y, neighbourhood);
        } else if(value == 1 && (neighborCount < 2 || neighborCount > 3)) {
            nextPosition[x][y] = 0;
            decreaseNeighborsCount(x, y, neighbourhood);
        } else {
            nextPosition[x][y] = position[x][y];
        }
    }

    private void step(int threadNum, int threadsCount) {
        int upBound = threadNum * (size / threadsCount);
        int bottomBound = (threadNum + 1) * (size / threadsCount);
        if(threadNum == threadsCount - 1) {
            bottomBound = size;
        }

        for(int x = upBound; x < bottomBound; x++) {
            for(int y = 0; y < size; y++) {
               processCell(x, y, city[threadNum]);
            }
        }
    }

    void processMt(int stepCount, int threadsCount) throws InterruptedException {

        ArrayList<Callable<Object>> threads = new ArrayList<>();
        final ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        for(int i = 0; i < threadsCount; i++) {
            threads.add(Executors.callable(new ProcessThread(this, i, threadsCount)));
        }

        for(int i = 0; i < stepCount; i++) {
            city = new int[threadsCount][size][size];
            executor.invokeAll(threads);

            position = nextPosition;
            nextPosition = new int[size][size];
            for(int s = 0; s < threadsCount; s++) {
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < size; k++) {
                        neighbors[j][k] += city[s][j][k];
                    }
                }
            }
        }
        executor.shutdown();
    }

    void process(int stepCount) {
        for(int i = 0; i < stepCount; i++) {
            for(int x = 0; x < size; x++) {
                for(int y = 0; y < size; y++) {
                    processCell(x, y, nextNeighbors);
                }
            }
            position = nextPosition;
            nextPosition = new int[size][size];
            neighbors = nextNeighbors;
            nextNeighbors = new int[size][size];
            for(int k = 0; k < size; k++) {
                nextNeighbors[k] = Arrays.copyOf(neighbors[k], size);
            }
        }
    }

    int getValue(int x, int y) {
        return position[x][y];
    }

    int getSize() {
        return size;
    }

    class ProcessThread extends Thread {

        int threadNumber;
        int threadsCount;
        GameField field;

        ProcessThread(GameField f, int n, int count) {
            field = f;
            threadNumber = n;
            threadsCount = count;
        }

        @Override
        public void run() {
            field.step(threadNumber, threadsCount);
        }
    }
}


class IncorrectFieldException extends Exception {

    IncorrectFieldException(String message) {
        super(message);
    }
}
