package org.omer.connectfour.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested game cannot be found.
 * This is a runtime exception as game-not-found is an expected client error.
 */
public class GameNotFoundException extends RuntimeException {
    private final UUID gameId;

    /**
     * Constructs a new GameNotFoundException for the specified game ID.
     *
     * @param gameId The UUID of the game that was not found.
     */
    public GameNotFoundException(UUID gameId) {
        super("Game not found: " + gameId);
        this.gameId = gameId;
    }

    /**
     * Gets the ID of the game that was not found.
     *
     * @return The game UUID.
     */
    public UUID getGameId() {
        return gameId;
    }
}
