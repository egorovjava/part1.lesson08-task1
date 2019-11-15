package com.gmail.egorovsonalexey;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Random;

public class PerformanceTests {

    @Test
    public void test1000() throws IncorrectFieldException {
        Random rnd = new Random();
        int size = 1000;
        LifeGame game = new LifeGame(size);

        long startTime = System.currentTimeMillis();
        game.process(100);
        long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime < 4000);
    }

    @Test
    public void test1000Mt() throws IncorrectFieldException {
        Random rnd = new Random();
        int size = 1000;
        LifeGame game = new LifeGame(size);

        long startTime = System.currentTimeMillis();
        try {
            game.processMt(100, 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime < 2000);
    }
}
