package edu.arsw.matrix.models;

import edu.arsw.matrix.Game;

/**
 * Abstract base class for any entity. Now implements Runnable to
 * contain its own life cycle in a separate thread.
 */
public abstract class Unit implements Runnable {
    protected int x;
    protected int y;
    protected final Game game;

    public Unit(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}