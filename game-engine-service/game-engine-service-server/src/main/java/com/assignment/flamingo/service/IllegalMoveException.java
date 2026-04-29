package com.assignment.flamingo.service;


/**
 * Custom checked exception thrown when the move made is illegal (e.g., the target cell is unoccupied).
 *
 * @author Kate
 * @since 26-Apr-2026
 */
public class IllegalMoveException extends Exception {

    public IllegalMoveException(String message) {
        super(message);
    }
}
