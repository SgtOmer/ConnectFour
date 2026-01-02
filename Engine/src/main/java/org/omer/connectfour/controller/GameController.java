package org.omer.connectfour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.omer.connectfour.api.GamesApi;
import org.omer.connectfour.api.model.CreateGameResponse;
import org.omer.connectfour.api.model.GameResponse;
import org.omer.connectfour.api.model.MoveRequest;
import org.omer.connectfour.api.model.MoveResponse;
import org.omer.connectfour.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller for the Connect Four Game API.
 * Implements the generated GamesApi interface.
 */
@RestController
@Log4j2
@RequiredArgsConstructor
public class GameController implements GamesApi {
    private final GameService gameService;

    /**
     * Creates a new game.
     *
     * @return The created game ID (201 Created).
     */
    @Override
    public ResponseEntity<CreateGameResponse> createGame() {
        log.info("Received request to create game");
        UUID uuid = gameService.createGame();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateGameResponse().id(uuid));
    }

    /**
     * Retrieves the state of a specific game.
     *
     * @param id The game UUID.
     * @return The game response.
     */
    @Override
    public ResponseEntity<GameResponse> getGame(UUID id) {
        log.debug("Received request to get game {}", id);

        return ResponseEntity.ok(gameService.getGameResponse(id));
    }

    /**
     * Deletes (abandons) a game.
     *
     * @param id The game UUID.
     * @return 204 No Content.
     */
    @Override
    public ResponseEntity<Void> deleteGame(UUID id) {
        gameService.removeGame(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Processes a move in the game.
     *
     * @param id          The game UUID.
     * @param moveRequest The move request (validated).
     * @return The move response.
     */
    @Override
    public ResponseEntity<MoveResponse> makeMove(UUID id, MoveRequest moveRequest) {
        log.debug("Received move request for game {}", id);

        return ResponseEntity.ok(gameService.makeMove(id, moveRequest));
    }
}
