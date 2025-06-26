package edu.arsw.matrix.models;

import edu.arsw.matrix.Game;
import edu.arsw.matrix.services.Board;
import java.util.Random;

public class Cop extends Unit implements Movable {

    private final Random random = new Random();

    public Cop(int x, int y, Game game) {
        super(x, y, game);
    }

    @Override
    public Move calculateNextMove(Board board) {
        Unit thief = board.findUnit(Thief.class);
        if (thief == null) {
            return new Move(this, this.x, this.y);
        }

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

    @Override
    public void run() {
        while (game.isRunning()) {
            try {
                Thread.sleep(1500);
                if (!game.isRunning())
                    break;

                Move nextMove = calculateNextMove(game.getBoard());
                game.getBoard().requestMove(nextMove);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}