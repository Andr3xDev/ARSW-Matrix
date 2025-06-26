package edu.arsw.matrix.models;

import edu.arsw.matrix.services.Board;
import java.util.Random;

public class Thief extends Unit implements Movable {
    private final Random random = new Random();

    public Thief(int x, int y) {
        super(x, y);
    }

    @Override
    public Move calculateNextMove(Board board) {
        int moveX = random.nextInt(3) - 1;
        int moveY = random.nextInt(3) - 1;
        return new Move(this, this.x + moveX, this.y + moveY);
    }
}