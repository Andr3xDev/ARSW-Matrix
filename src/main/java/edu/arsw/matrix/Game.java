package edu.arsw.matrix;

import edu.arsw.matrix.models.Unit;
import edu.arsw.matrix.services.Board;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Board board;
    private volatile boolean isRunning = true; // Must be volatile
    private final List<Thread> unitThreads = new ArrayList<>();

    public Game(int sizeX, int sizeY, int copsNum, int phoneNum, int blocks) {
        // The Board needs a reference to this Game instance to stop it.
        this.board = new Board(sizeX, sizeY, copsNum, phoneNum, blocks, this);
    }

    public void start() {
        System.out.println("Starting game... All units will move concurrently.");

        // Find all units and create a thread for each one
        Unit[][] grid = board.getGrid(); // No need to sync, board is not yet shared
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] != null) {
                    Thread t = new Thread(grid[y][x]);
                    unitThreads.add(t);
                    t.start();
                }
            }
        }

        // A separate thread to print the board periodically
        Thread renderer = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                    board.printBoard();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        renderer.start();
    }

    public synchronized void endGame(String message) {
        if (isRunning) {
            this.isRunning = false;
            System.out.println("====================");
            System.out.println("    GAME OVER");
            System.out.println("====================");
            System.out.println("RESULT: " + message);

            // Interrupt all threads to make them stop cleanly
            unitThreads.forEach(Thread::interrupt);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Board getBoard() {
        return board;
    }
}