package edu.arsw.matrix.models;

import edu.arsw.matrix.services.Board;

/**
 * An interface for all entities that have the ability to calculate a move.
 */
public interface Movable {
    Move calculateNextMove(Board board);
}