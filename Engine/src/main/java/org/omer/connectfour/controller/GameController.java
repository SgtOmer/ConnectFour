package org.omer.connectfour.controller;

import org.omer.connectfour.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping
    public Set<UUID> getAllGames() {
        return gameRepository.getAllGames();
    }

    @GetMapping("/create")
    public ResponseEntity<UUID> createGame() {
        return ResponseEntity.ok(gameRepository.createGame());
    }

    @GetMapping("/{uuid}/{move}")
    public ResponseEntity<Integer> setMove(@PathVariable String uuid, @PathVariable int move) {
        return ResponseEntity.ok(gameRepository.setMove(UUID.fromString(uuid), move));
    }

    @GetMapping("/boards/{uuid}")
    public ResponseEntity<char[][]> getBoard(@PathVariable String uuid) {
        return ResponseEntity.ok(gameRepository.getBoard(UUID.fromString(uuid)));
    }
}
