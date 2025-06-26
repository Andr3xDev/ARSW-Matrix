package edu.arsw.matrix.models;

import edu.arsw.matrix.Game;

/**
 * A static entity representing a phone. It does not move.
 */
public class Phone extends Unit {
    public Phone(int x, int y, Game game) {
        super(x, y, game);
    }

    @Override
    public void run() {
    }
}