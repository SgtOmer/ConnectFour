package org.omer.connectfour.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.omer.connectfour.api.model.GameResponse;
import org.omer.connectfour.api.model.GameStatus;
import org.omer.connectfour.api.model.MoveRequest;
import org.omer.connectfour.api.model.MoveResponse;
import org.omer.connectfour.api.model.Position;
import org.omer.connectfour.exception.IllegalMoveException;
import org.omer.connectfour.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private GameService gameService;

        @Test
        @DisplayName("POST /games should create a game and return 201 with ID")
        void shouldCreateGame() throws Exception {
                UUID uuid = UUID.randomUUID();
                when(gameService.createGame()).thenReturn(uuid);

                mockMvc.perform(post("/games"))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(uuid.toString()));
        }

        @Test
        @DisplayName("GET /games/{id} should return game when exists")
        void shouldGetGameWhenExists() throws Exception {
                UUID uuid = UUID.randomUUID();
                GameResponse response = new GameResponse().id(uuid).status(GameStatus.ONGOING);

                when(gameService.getGameResponse(uuid)).thenReturn(Optional.of(response));

                mockMvc.perform(get("/games/" + uuid))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(uuid.toString()))
                                .andExpect(jsonPath("$.status").value("ONGOING"));
        }

        @Test
        @DisplayName("GET /games/{id} should return 404 when not found")
        void shouldReturn404WhenGameNotFound() throws Exception {
                UUID uuid = UUID.randomUUID();
                when(gameService.getGameResponse(uuid)).thenReturn(Optional.empty());

                mockMvc.perform(get("/games/" + uuid))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("DELETE /games/{id} should return 204 when game exists")
        void shouldDeleteGameWhenExists() throws Exception {
                UUID uuid = UUID.randomUUID();
                when(gameService.gameExists(uuid)).thenReturn(true);

                mockMvc.perform(delete("/games/" + uuid))
                                .andExpect(status().isNoContent());

                verify(gameService).removeGame(uuid);
        }

        @Test
        @DisplayName("DELETE /games/{id} should return 404 when not found")
        void shouldReturn404WhenDeletingNonExistentGame() throws Exception {
                UUID uuid = UUID.randomUUID();
                when(gameService.gameExists(uuid)).thenReturn(false);

                mockMvc.perform(delete("/games/" + uuid))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("POST /games/{id}/moves should return 200 on success")
        void shouldMakeMoveSuccessfully() throws Exception {
                UUID uuid = UUID.randomUUID();
                Position botPos = new Position().row(5).column(3);
                MoveResponse response = new MoveResponse().botMove(botPos).status(GameStatus.ONGOING);

                when(gameService.makeMove(eq(uuid), any(MoveRequest.class))).thenReturn(Optional.of(response));

                mockMvc.perform(post("/games/" + uuid + "/moves")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"column\": 0}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("ONGOING"))
                                .andExpect(jsonPath("$.botMove.column").value(3));
        }

        @Test
        @DisplayName("POST /games/{id}/moves should return 400 on invalid column")
        void shouldReturnBadRequestOnInvalidColumn() throws Exception {
                UUID uuid = UUID.randomUUID();

                when(gameService.makeMove(eq(uuid), any(MoveRequest.class)))
                                .thenThrow(new IllegalMoveException("Column 99 does not exist",
                                                IllegalMoveException.Reason.COLUMN_NOT_FOUND));

                mockMvc.perform(post("/games/" + uuid + "/moves")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"column\": 99}"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /games/{id}/moves should return 400 when column is full")
        void shouldReturnBadRequestOnFullColumn() throws Exception {
                UUID uuid = UUID.randomUUID();

                when(gameService.makeMove(eq(uuid), any(MoveRequest.class)))
                                .thenThrow(new IllegalMoveException("Column 0 is full",
                                                IllegalMoveException.Reason.COLUMN_FULL));

                mockMvc.perform(post("/games/" + uuid + "/moves")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"column\": 0}"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /games/{id}/moves should return 404 when game not found")
        void shouldReturnNotFoundWhenGameMissing() throws Exception {
                UUID uuid = UUID.randomUUID();

                when(gameService.makeMove(eq(uuid), any(MoveRequest.class))).thenReturn(Optional.empty());

                mockMvc.perform(post("/games/" + uuid + "/moves")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"column\": 0}"))
                                .andExpect(status().isNotFound());
        }
}
