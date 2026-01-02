package org.omer.connectfour.repository;

import lombok.extern.log4j.Log4j2;
import org.omer.connectfour.model.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Repository for managing Game entity persistence.
 * Provides pure data access operations (CRUD) without business logic.
 */
@Repository
@Log4j2
public class GameRepository {
    private final Map<UUID, Game> games = new HashMap<>();

    /**
     * Retrieves all game UUIDs currently stored.
     *
     * @return A set of all game UUIDs.
     */
    public Set<UUID> findAllIds() {
        return games.keySet();
    }

    /**
     * Saves a game entity to the repository.
     *
     * @param game The game to save.
     * @return The saved game.
     */
    public Game save(Game game) {
        log.debug("Saving game {}", game.getUuid());
        games.put(game.getUuid(), game);
        return game;
    }

    /**
     * Finds a game by its UUID.
     *
     * @param uuid The game UUID.
     * @return An Optional containing the game if found, empty otherwise.
     */
    public Optional<Game> findById(UUID uuid) {
        return Optional.ofNullable(games.get(uuid));
    }

    /**
     * Checks if a game exists by its UUID.
     *
     * @param uuid The game UUID.
     * @return True if the game exists, false otherwise.
     */
    public boolean existsById(UUID uuid) {
        return games.containsKey(uuid);
    }

    /**
     * Deletes a game by its UUID.
     *
     * @param uuid The game UUID.
     */
    public void deleteById(UUID uuid) {
        log.debug("Deleting game {}", uuid);
        games.remove(uuid);
    }
}
