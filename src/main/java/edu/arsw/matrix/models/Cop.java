package edu.arsw.matrix.models;

import edu.arsw.matrix.services.Board;
import java.util.Random;

public class Cop extends Unit implements Movable {
    private final Random random = new Random();

    public Cop(int x, int y) {
        super(x, y);
    }

    @Override
    public Move calculateNextMove(Board board) {
        Unit thief = board.findUnit(Thief.class);
        if (thief == null)
            return new Move(this, this.x, this.y);

        int targetX = thief.getX();
        int targetY = thief.getY();

        int moveX = Integer.compare(targetX, this.x);
        int moveY = Integer.compare(targetY, this.y);

        if (moveX != 0 && moveY != 0) {
            if (random.nextBoolean()) {
                moveY = 0;
            } else {
                moveX = 0;
            }
        }

        return new Move(this, this.x + moveX, this.y + moveY);
    }
}