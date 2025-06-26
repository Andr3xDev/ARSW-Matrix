package edu.arsw.matrix.models;

import edu.arsw.matrix.Game;

/**
 * A static entity representing an obstacle. It does not move.
 */
public class Block extends Unit {
    public Block(int x, int y, Game game) {
        super(x, y, game);
    }

    @Override
    public void run() {
    }
}