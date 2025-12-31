package org.omer.connectfour.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.omer.connectfour.api.model.GameResponse;
import org.omer.connectfour.api.model.MoveRequest;
import org.omer.connectfour.api.model.MoveResponse;
import org.omer.connectfour.bot.Bot;
import org.omer.connectfour.model.Board;
import org.omer.connectfour.model.Game;
import org.omer.connectfour.repository.GameRepository;
import org.omer.connectfour.utils.Constants;

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
        when(gameRepository.createGame()).thenReturn(gameId);

        UUID result = gameService.createGame();

        assertAll("Create Game",
                () -> assertThat(result).as("Created game ID should match").isEqualTo(gameId),
                () -> verify(gameRepository).createGame());
    }

    @Test
    @DisplayName("Should get game response")
    void shouldGetGameResponse() {
        when(gameRepository.getGame(gameId)).thenReturn(game);
        when(gameMapper.toGameResponse(game)).thenReturn(new GameResponse());

        GameResponse result = gameService.getGameResponse(gameId);

        assertAll("Get Game Response",
                () -> assertThat(result).as("Game response should not be null").isNotNull(),
                () -> verify(gameMapper).toGameResponse(game));
    }

    @Test
    @DisplayName("Should make move successfully")
    void shouldMakeMove() {
        MoveRequest request = new MoveRequest().column(0);
        when(gameRepository.getGame(gameId)).thenReturn(game);
        when(gameMapper.createMoveResponse(any(), anyBoolean(), any(), any(), any(), any()))
                .thenReturn(new MoveResponse());

        MoveResponse response = gameService.makeMove(gameId, request);

        assertAll("Make Move Verification",
                () -> assertThat(response).as("Move response should not be null").isNotNull(),
                // Verify Board state changed (Player move set)
                () -> assertThat(game.getBoard().getBoard()[Constants.ROWS - 1][0])
                        .as("Board cell 0,0 should be X after move").isEqualTo(Constants.X_ON_BOARD),
                // Verify Logic Called Mapper
                () -> verify(gameMapper).createMoveResponse(any(), eq(true), any(), any(), any(), any()));
    }

    @Test
    @DisplayName("Should throw exception on invalid move")
    void shouldThrowOnInvalidMove() {
        MoveRequest request = new MoveRequest().column(99); // Invalid column
        when(gameRepository.getGame(gameId)).thenReturn(game);

        assertThrows(IllegalArgumentException.class, () -> gameService.makeMove(gameId, request));
    }
}
