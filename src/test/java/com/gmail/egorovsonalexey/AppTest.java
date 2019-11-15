package com.gmail.egorovsonalexey;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void gliderTest() {

        final String inputFile = "testInput1.txt";
        final String outputFile = "testOutput1.txt";

        try {
            Files.deleteIfExists(Paths.get(outputFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        LifeGame game;

        try {
            game = new LifeGame(inputFile);
        } catch (IOException | IncorrectFieldException e) {
            e.printStackTrace();
            return;
        }

        game.process(36);

        try {
            game.gameFieldSave(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] input = null;
        byte[] output = null;
        try {
            input = Files.readAllBytes(Paths.get(inputFile));
            output = Files.readAllBytes(Paths.get(outputFile));
        } catch (IOException e) {
           e.printStackTrace();
        }

        assertNotNull(input);
        assertNotNull(output);
        assertArrayEquals(input, output);
    }

    @Test
    public void gliderTestMt() {

        final String inputFile = "testInput1.txt";
        final String outputFile = "testOutput1.txt";

        try {
            Files.deleteIfExists(Paths.get(outputFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        LifeGame game;

        try {
            game = new LifeGame(inputFile);
        } catch (IOException | IncorrectFieldException e) {
            e.printStackTrace();
            return;
        }

        try {
            game.processMt(36, 9);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            game.gameFieldSave(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] input = null;
        byte[] output = null;
        try {
            input = Files.readAllBytes(Paths.get(inputFile));
            output = Files.readAllBytes(Paths.get(outputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(input);
        assertNotNull(output);
        assertArrayEquals(input, output);
    }
}
