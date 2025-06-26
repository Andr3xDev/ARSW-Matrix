package edu.arsw.matrix;

import edu.arsw.matrix.services.Board;

public class MatrixApplication {
    public static void main(String[] args) {
        if (args.length < 5) {
            System.err.println("Usage: java ... MatrixApplication <sizeX> <sizeY> <copsNum> <phonesNum> <blocks>");
            return;
        }

        try {
            int sizeX = Integer.parseInt(args[0]);
            int sizeY = Integer.parseInt(args[1]);
            int copsNum = Integer.parseInt(args[2]);
            int phonesNum = Integer.parseInt(args[3]);
            int blocks = Integer.parseInt(args[4]);

            Board board = new Board(sizeX, sizeY, copsNum, phonesNum, blocks);
            Game game = new Game(board);
            game.start();

        } catch (NumberFormatException e) {
            System.err.println("Error: All arguments must be integer numbers.");
        } catch (IllegalArgumentException e) {
            System.err.println("Configuration Error: " + e.getMessage());
        }
    }
}