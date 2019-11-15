package com.gmail.egorovsonalexey;

import java.io.*;
import java.util.Random;

class LifeGame {

    private int size;
    private GameField field;
    private int[][] startPosition;

    LifeGame(int s) throws IncorrectFieldException {
        size = s;
        startPosition = getRandomPosition();
        field = new GameField(startPosition);
    }

    LifeGame(String fileName) throws IncorrectFieldException, IOException {
        startPosition = gameFieldLoad(fileName);
        field = new GameField(startPosition);
        size = field.getSize();
    }

    void reset() {
        try {
            field = new GameField(startPosition);
        } catch (IncorrectFieldException e) {
            e.printStackTrace();
        }
    }

    void process(int stepCount) {
        field.process(stepCount);
    }

    void processMt(int stepCount, int threadCount) throws InterruptedException {
        if(threadCount > size) {
            throw new IllegalArgumentException("Thread count mast be less than or equals game field size.");
        }
        field.processMt(stepCount, threadCount);
    }

    private int[][] gameFieldLoad(String fileName) throws IOException {

        int[][] data = null;
        int chZero = '0';

        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
            String str;
            int strCount = 0;
            while ((str = bf.readLine()) != null) {
                int len = str.length();
                if (data == null) {
                    data = new int[len][len];
                }

                for (int i = 0; i < len; i++) {
                    int val = str.charAt(i) - chZero;
                    data[strCount][i] = val;
                }
                strCount++;
            }
        }
        return data;
    }

    void gameFieldSave(String fileName) throws IOException {
        try (FileOutputStream out = new FileOutputStream(fileName, true)) {
            PrintWriter pw = new PrintWriter(out);
            int size = field.getSize();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    pw.print(field.getValue(i, j));
                }
                pw.println();
            }
            pw.close();
        }
    }

    private int[][] getRandomPosition() {
        Random rnd = new Random();
        int[][] gameFieldData = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boolean rBool = rnd.nextBoolean();
                gameFieldData[i][j] = rBool ? 1 : 0;
            }
        }
        return gameFieldData;
    }
}
