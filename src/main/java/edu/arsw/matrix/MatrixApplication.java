package edu.arsw.matrix;

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

            Game game = new Game(sizeX, sizeY, copsNum, phonesNum, blocks);
            game.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}