package edu.arsw.matrix.models;

/**
 * Abstract base class for any entity on the game board.
 * It stores the unit's position.
 */
public abstract class Unit {
    protected int x;
    protected int y;

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
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