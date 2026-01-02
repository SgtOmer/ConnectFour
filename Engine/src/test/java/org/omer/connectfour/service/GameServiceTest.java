package org.omer.connectfour.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.omer.connectfour.api.model.GameResponse;
import org.omer.connectfour.api.model.GameStatus;
import org.omer.connectfour.api.model.MoveRequest;
import org.omer.connectfour.api.model.MoveResponse;
import org.omer.connectfour.bot.Bot;
import org.omer.connectfour.model.Board;
import org.omer.connectfour.model.Game;
import org.omer.connectfour.repository.GameRepository;
import org.omer.connectfour.exception.IllegalMoveException;
import org.omer.connectfour.utils.Constants;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameService gameService;

    private Game game;
    private final UUID gameId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Board board = new Board(Constants.ROWS, Constants.COLS);
        Bot bot = new Bot(board, Constants.O_ON_BOARD);
        game = new Game(board, bot);
    }

    @Test
    @DisplayName("Should create game")
    void shouldCreateGame() {
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID result = gameService.createGame();

        assertAll("Create Game",
                () -> assertThat(result).as("Created game ID should not be null").isNotNull(),
                () -> verify(gameRepository).save(any(Game.class)));
    }

    @Test
    @DisplayName("Should get game response")
    void shouldGetGameResponse() {
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameMapper.mapBoard(any())).thenReturn(new ArrayList<>());
        when(gameMapper.determineGameStatus(game)).thenReturn(GameStatus.ONGOING);

        Optional<GameResponse> result = gameService.getGameResponse(gameId);

        assertAll("Get Game Response",
                () -> assertThat(result).as("Game response should be present").isPresent(),
                () -> assertThat(result.get().getStatus()).as("Status should be ONGOING")
                        .isEqualTo(GameStatus.ONGOING));
    }

    @Test
    @DisplayName("Should return empty Optional when game not found")
    void shouldReturnEmptyWhenGameNotFound() {
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        Optional<GameResponse> result = gameService.getGameResponse(gameId);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should make move successfully")
    void shouldMakeMove() throws IllegalMoveException {
        MoveRequest request = new MoveRequest().column(0);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameMapper.determineGameStatus(game)).thenReturn(GameStatus.ONGOING);

        Optional<MoveResponse> response = gameService.makeMove(gameId, request);

        assertAll("Make Move Verification",
                () -> assertThat(response).as("Move response should be present").isPresent(),
                () -> assertThat(game.getBoard().getBoard()[Constants.ROWS - 1][0])
                        .as("Board cell 0,0 should be X after move").isEqualTo(Constants.X_ON_BOARD),
                () -> assertThat(response.get().getStatus()).as("Status should be ONGOING")
                        .isEqualTo(GameStatus.ONGOING));
    }

    @Test
    @DisplayName("Should throw exception on invalid column")
    void shouldThrowOnInvalidMove() {
        MoveRequest request = new MoveRequest().column(99);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        IllegalMoveException exception = assertThrows(IllegalMoveException.class,
                () -> gameService.makeMove(gameId, request));
        assertThat(exception.getReason()).isEqualTo(IllegalMoveException.Reason.COLUMN_NOT_FOUND);
    }

    @Test
    @DisplayName("Should return empty Optional when making move on non-existent game")
    void shouldReturnEmptyOnMoveForNonExistentGame() throws IllegalMoveException {
        MoveRequest request = new MoveRequest().column(0);
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        Optional<MoveResponse> response = gameService.makeMove(gameId, request);

        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("Should return true for existing game")
    void shouldCheckGameExists() {
        when(gameRepository.existsById(gameId)).thenReturn(true);

        boolean exists = gameService.gameExists(gameId);

        assertThat(exists).isTrue();
    }
}
