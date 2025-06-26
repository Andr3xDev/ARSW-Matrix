package edu.arsw.matrix.services;

import edu.arsw.matrix.Game;
import edu.arsw.matrix.models.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private final int sizeX;
    private final int sizeY;
    private final Unit[][] grid;
    private final Game game; // Needs a reference to the game to stop it.

    public Board(int sizeX, int sizeY, int copsNum, int phoneNum, int blocks, Game game) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.game = game;
        this.grid = new Unit[sizeY][sizeX];
        create(copsNum, phoneNum, blocks);
    }

    private void create(int copsNum, int phoneNum, int blocks) {
        int totalEntities = copsNum + phoneNum + blocks + 1;
        if (totalEntities > sizeX * sizeY)
            throw new IllegalArgumentException("Too many entities.");

        List<Point> coords = new ArrayList<>();
        for (int y = 0; y < sizeY; y++)
            for (int x = 0; x < sizeX; x++)
                coords.add(new Point(x, y));
        Collections.shuffle(coords);

        int index = 0;
        Point pos;

        pos = coords.get(index++);
        grid[pos.y][pos.x] = new Thief(pos.x, pos.y, game);
        for (int i = 0; i < copsNum; i++) {
            pos = coords.get(index++);
            grid[pos.y][pos.x] = new Cop(pos.x, pos.y, game);
        }
        for (int i = 0; i < phoneNum; i++) {
            pos = coords.get(index++);
            grid[pos.y][pos.x] = new Phone(pos.x, pos.y, game);
        }
        for (int i = 0; i < blocks; i++) {
            pos = coords.get(index++);
            grid[pos.y][pos.x] = new Block(pos.x, pos.y, game);
        }
    }

    /**
     * A synchronized method for units to request a move.
     * This is the critical section that controls all state changes.
     */
    public synchronized void requestMove(Move move) {
        if (!game.isRunning())
            return;

        Unit unit = move.unit();
        int newX = move.newX();
        int newY = move.newY();

        if (grid[unit.getY()][unit.getX()] != unit)
            return;
        if (newX < 0 || newX >= sizeX || newY < 0 || newY >= sizeY)
            return;

        Unit targetUnit = grid[newY][newX];

        if (unit instanceof Thief && targetUnit instanceof Phone) {
            game.endGame("The thief reached a phone and escaped!");
            return;
        }

        if (unit instanceof Cop && targetUnit instanceof Thief) {
            game.endGame("The cops caught the thief!");
            return;
        }

        if (targetUnit == null) {
            grid[unit.getY()][unit.getX()] = null;
            unit.setX(newX);
            unit.setY(newY);
            grid[newY][newX] = unit;
        }
    }

    // All public methods that access the grid must be synchronized
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

    public synchronized List<Unit> findAllUnits(Class<? extends Unit> type) {
        List<Unit> found = new ArrayList<>();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (type.isInstance(grid[y][x])) {
                    found.add(grid[y][x]);
                }
            }
        }
        return found;
    }

    public synchronized Unit[][] getGrid() {
        return grid;
    }

    public synchronized void printBoard() {
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