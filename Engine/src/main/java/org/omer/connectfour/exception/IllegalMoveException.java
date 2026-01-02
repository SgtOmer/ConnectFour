package org.omer.connectfour.exception;

/**
 * Exception thrown when an illegal move is attempted in the game.
 * This is a checked exception to enforce explicit handling by callers.
 */
public class IllegalMoveException extends Exception {

    public enum Reason {
        COLUMN_NOT_FOUND,
        COLUMN_FULL
    }

    private final Reason reason;

    /**
     * Constructs a new IllegalMoveException with the specified message and reason.
     *
     * @param message The detail message.
     * @param reason  The reason for the illegal move.
     */
    public IllegalMoveException(String message, Reason reason) {
        super(message);
        this.reason = reason;
    }

    /**
     * Gets the reason for this illegal move.
     *
     * @return The reason enum value.
     */
    public Reason getReason() {
        return reason;
    }
}
