package edu.arsw.matrix.services;

import edu.arsw.matrix.models.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private final int sizeX;
    private final int sizeY;
    private final Unit[][] grid;

    // ... El constructor y el método create() se mantienen igual que en la versión
    // anterior ...
    public Board(int sizeX, int sizeY, int copsNum, int phoneNum, int blocks) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.grid = create(sizeX, sizeY, copsNum, phoneNum, blocks);
    }

    private Unit[][] create(int sizeX, int sizeY, int copsNum, int phoneNum, int blocks) {
        int totalEntities = copsNum + phoneNum + blocks + 1;
        if (totalEntities > sizeX * sizeY) {
            throw new IllegalArgumentException("Too many entities for the given board size.");
        }
        Unit[][] newBoard = new Unit[sizeY][sizeX];
        List<Point> coords = new ArrayList<>();
        for (int y = 0; y < sizeY; y++)
            for (int x = 0; x < sizeX; x++)
                coords.add(new Point(x, y));
        Collections.shuffle(coords);
        int index = 0;
        Point pos;
        pos = coords.get(index++);
        newBoard[pos.y][pos.x] = new Thief(pos.x, pos.y);
        for (int i = 0; i < copsNum; i++) {
            pos = coords.get(index++);
            newBoard[pos.y][pos.x] = new Cop(pos.x, pos.y);
        }
        for (int i = 0; i < phoneNum; i++) {
            pos = coords.get(index++);
            newBoard[pos.y][pos.x] = new Phone(pos.x, pos.y);
        }
        for (int i = 0; i < blocks; i++) {
            pos = coords.get(index++);
            newBoard[pos.y][pos.x] = new Block(pos.x, pos.y);
        }
        return newBoard;
    }

    /**
     * Applies a list of planned moves and detects game-ending collisions.
     * This is the synchronized point where the game state is safely modified.
     * 
     * @param moves The list of moves planned for this turn.
     * @return The new GameState after applying the moves.
     */
    public synchronized GameState applyMoves(List<Move> moves) {
        for (Move move : moves) {
            Unit unit = move.unit();
            int newX = move.newX();
            int newY = move.newY();

            // Ignorar si la unidad ya fue eliminada (ej. el ladrón fue capturado)
            if (grid[unit.getY()][unit.getX()] != unit)
                continue;

            // Validar que el movimiento esté dentro de los límites
            if (newX < 0 || newX >= sizeX || newY < 0 || newY >= sizeY)
                continue;

            Unit targetUnit = grid[newY][newX];

            // --- LÓGICA DE INTERACCIÓN ---

            // Condición de Victoria del Ladrón
            if (unit instanceof Thief && targetUnit instanceof Phone) {
                // Mover el ladrón a la nueva casilla (opcional, el juego termina)
                grid[unit.getY()][unit.getX()] = null;
                grid[newY][newX] = unit;
                return GameState.THIEF_WINS;
            }

            // Condición de Victoria de la Policía
            if (unit instanceof Cop && targetUnit instanceof Thief) {
                // El policía se mueve y captura al ladrón
                grid[unit.getY()][unit.getX()] = null; // Vaciar la celda vieja del policía
                grid[newY][newX] = unit; // Mover al policía a la celda del ladrón
                return GameState.COPS_WIN;
            }

            // Movimiento normal a una casilla vacía
            if (targetUnit == null) {
                grid[unit.getY()][unit.getX()] = null;
                unit.setX(newX);
                unit.setY(newY);
                grid[newY][newX] = unit;
            }
        }
        return GameState.RUNNING; // Si no ocurrió ningún evento, el juego continúa.
    }

    // ... findUnit, getters y printBoard se mantienen igual ...
    public synchronized Unit findUnit(Class<? extends Unit> type) {
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (type.isInstance(grid[y][x])) {
                    return grid[y][x];
                }
            }
        }
        return null;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Unit[][] getGrid() {
        return grid;
    }

    public void printBoard() {
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (grid[y][x] != null)
                    System.out.print(grid[y][x].getClass().getSimpleName().charAt(0) + " ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
        System.out.println("--------------------");
    }
}