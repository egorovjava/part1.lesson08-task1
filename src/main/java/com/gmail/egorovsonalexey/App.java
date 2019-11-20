package com.gmail.egorovsonalexey;

import com.gmail.egorovsonalexey.exeptions.IncorrectFieldException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class App {
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: App <input file name> <output file name>");
            return;
        }

        LifeGame game;

        try {
            game = new LifeGame(2000);
            //game = new LifeGame(args[0]);
        } catch (IncorrectFieldException e) {
            e.printStackTrace();
            return;
        }

        int stepCount = 100;

        long startTime = System.currentTimeMillis();
        try {
            game.processMt( stepCount, 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        game.reset();

        startTime = System.currentTimeMillis();
        game.process(stepCount);
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
//        String outputFile = args[1];
//        try {
//            Files.deleteIfExists(Paths.get(outputFile));
//            game.gameFieldSave(outputFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
