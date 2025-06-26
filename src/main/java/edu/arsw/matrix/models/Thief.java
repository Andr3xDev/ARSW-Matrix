package edu.arsw.matrix.models;

import edu.arsw.matrix.Game;
import edu.arsw.matrix.services.Board;
import java.util.List;

public class Thief extends Unit implements Movable {

    public Thief(int x, int y, Game game) {
        super(x, y, game);
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

    @Override
    public Move calculateNextMove(Board board) {
        List<Unit> phones = board.findAllUnits(Phone.class);
        if (phones.isEmpty()) {
            return new Move(this, this.x, this.y);
        }

        Unit nearestPhone = null;
        int minDistance = Integer.MAX_VALUE;

        for (Unit phone : phones) {
            int distance = Math.abs(this.x - phone.getX()) + Math.abs(this.y - phone.getY());
            if (distance < minDistance) {
                minDistance = distance;
                nearestPhone = phone;
            }
        }

        int targetX = nearestPhone.getX();
        int targetY = nearestPhone.getY();
        int moveX = Integer.compare(targetX, this.x);
        int moveY = Integer.compare(targetY, this.y);
        return new Move(this, this.x + moveX, this.y + moveY);
    }
}
