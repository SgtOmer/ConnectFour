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
import org.omer.connectfour.exception.GameNotFoundException;
import org.omer.connectfour.exception.IllegalMoveException;
import org.omer.connectfour.model.Board;
import org.omer.connectfour.model.Game;
import org.omer.connectfour.repository.GameRepository;
import org.omer.connectfour.utils.Constants;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GameService.
 * Tests service layer business logic with mocked repository.
 */
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

        GameResponse result = gameService.getGameResponse(gameId);

        assertAll("Get Game Response",
                () -> assertThat(result).as("Game response should not be null").isNotNull(),
                () -> assertThat(result.getStatus()).as("Status should be ONGOING")
                        .isEqualTo(GameStatus.ONGOING));
    }

    @Test
    @DisplayName("Should throw GameNotFoundException when game not found")
    void shouldThrowWhenGameNotFound() {
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.getGameResponse(gameId))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessageContaining(gameId.toString());
    }

    @Test
    @DisplayName("Should make move successfully")
    void shouldMakeMove() {
        MoveRequest request = new MoveRequest().column(0);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameMapper.determineGameStatus(game)).thenReturn(GameStatus.ONGOING);

        MoveResponse response = gameService.makeMove(gameId, request);

        assertAll("Make Move Verification",
                () -> assertThat(response).as("Move response should not be null").isNotNull(),
                () -> assertThat(game.getBoard().getBoard()[Constants.ROWS - 1][0])
                        .as("Board cell 0,0 should be X after move").isEqualTo(Constants.X_ON_BOARD),
                () -> assertThat(response.getStatus()).as("Status should be ONGOING")
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
    @DisplayName("Should throw GameNotFoundException when making move on non-existent game")
    void shouldThrowOnMoveForNonExistentGame() {
        MoveRequest request = new MoveRequest().column(0);
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.makeMove(gameId, request))
                .isInstanceOf(GameNotFoundException.class);
    }

    @Test
    @DisplayName("Should remove game successfully")
    void shouldRemoveGame() {
        when(gameRepository.existsById(gameId)).thenReturn(true);

        gameService.removeGame(gameId);

        verify(gameRepository).deleteById(gameId);
    }

    @Test
    @DisplayName("Should throw GameNotFoundException when removing non-existent game")
    void shouldThrowWhenRemovingNonExistentGame() {
        when(gameRepository.existsById(gameId)).thenReturn(false);

        assertThatThrownBy(() -> gameService.removeGame(gameId))
                .isInstanceOf(GameNotFoundException.class);
    }
}
