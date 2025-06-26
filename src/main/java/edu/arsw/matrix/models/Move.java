package edu.arsw.matrix.models;

/**
 * An immutable record to represent the intention of a move.
 * It contains the unit that intends to move and its desired new position.
 */
public record Move(Unit unit, int newX, int newY) {
}