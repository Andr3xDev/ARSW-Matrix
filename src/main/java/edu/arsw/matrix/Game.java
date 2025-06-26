package edu.arsw.matrix;

import edu.arsw.matrix.models.*;
import edu.arsw.matrix.services.Board;
import edu.arsw.matrix.services.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Game implements Runnable {

    private final Board board;
    private final ExecutorService executor;
    private volatile GameState currentState = GameState.RUNNING;

    public Game(Board board) {
        this.board = board;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (currentState == GameState.RUNNING) {
            try {
                List<Callable<Move>> tasks = new ArrayList<>();
                Unit[][] currentGrid = board.getGrid();

                for (int y = 0; y < board.getSizeY(); y++) {
                    for (int x = 0; x < board.getSizeX(); x++) {
                        Unit unitAtCurrentPosition = currentGrid[y][x];

                        if (unitAtCurrentPosition instanceof Movable) {
                            // --- LA SOLUCIÓN ESTÁ AQUÍ ---
                            // 1. Creamos una variable final para la unidad de ESTA iteración.
                            // Esta variable 'movableEntity' es "efectivamente final" porque
                            // solo se asigna una vez dentro del ámbito de este 'if'.
                            final Movable movableEntity = (Movable) unitAtCurrentPosition;

                            // 2. La lambda ahora captura 'movableEntity', no 'x' ni 'y'.
                            // 'movableEntity' no cambia, por lo que el compilador está feliz.
                            tasks.add(() -> movableEntity.calculateNextMove(board));
                        }
                    }
                }

                // El resto del código no cambia...
                List<Future<Move>> futureMoves = executor.invokeAll(tasks);
                List<Move> plannedMoves = new ArrayList<>();
                for (Future<Move> f : futureMoves) {
                    plannedMoves.add(f.get());
                }

                this.currentState = board.applyMoves(plannedMoves);

                System.out.println("Game state: " + this.currentState);
                board.printBoard();

                Thread.sleep(1000);

            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Game loop was interrupted.");
                this.currentState = GameState.COPS_WIN;
                Thread.currentThread().interrupt();
            }
        }

        printFinalMessage();
        executor.shutdownNow();
    }

    private void printFinalMessage() {
        System.out.println("====================");
        System.out.println("    GAME OVER");
        System.out.println("====================");
        if (currentState == GameState.THIEF_WINS) {
            System.out.println("RESULT: The thief reached a phone and escaped!");
        } else {
            System.out.println("RESULT: The cops caught the thief!");
        }
    }
}