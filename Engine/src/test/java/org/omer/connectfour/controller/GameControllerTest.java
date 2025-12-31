package org.omer.connectfour.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.omer.connectfour.api.model.GameResponse;
import org.omer.connectfour.api.model.MoveRequest;
import org.omer.connectfour.api.model.MoveResponse;
import org.omer.connectfour.model.Game;
import org.omer.connectfour.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @MockBean
    private GameService gameService;

    @Test
    @DisplayName("POST /games should create a game and return 201")
    void shouldCreateGame() throws Exception {
        UUID uuid = UUID.randomUUID();
        GameResponse response = new GameResponse().id(uuid);

        when(gameService.createGame()).thenReturn(uuid);
        when(gameService.getGameResponse(uuid)).thenReturn(response);

        mockMvc.perform(post("/games"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(uuid.toString()));
    }

    @Test
    @DisplayName("GET /games/{id} should return game or 404")
    void shouldGetGame() throws Exception {
        UUID uuid = UUID.randomUUID();
        GameResponse response = new GameResponse().id(uuid);

        when(gameService.getGameResponse(uuid)).thenReturn(response);

        mockMvc.perform(get("/games/" + uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(uuid.toString()));

        // Test 404
        when(gameService.getGameResponse(any(UUID.class))).thenReturn(null);
        mockMvc.perform(get("/games/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /games/{id} should return 204 or 404")
    void shouldDeleteGame() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(gameService.getGame(uuid)).thenReturn(new Game(null, null)); // Mock non-null game

        mockMvc.perform(delete("/games/" + uuid))
                .andExpect(status().isNoContent());

        verify(gameService).removeGame(uuid);

        // Test 404
        when(gameService.getGame(any(UUID.class))).thenReturn(null);
        mockMvc.perform(delete("/games/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /games/{id}/moves should handle success and errors")
    void shouldMakeMove() throws Exception {
        UUID uuid = UUID.randomUUID();
        MoveRequest request = new MoveRequest().column(0);
        MoveResponse response = new MoveResponse().success(true);

        when(gameService.makeMove(eq(uuid), any(MoveRequest.class))).thenReturn(response);

        mockMvc.perform(post("/games/" + uuid + "/moves")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"column\": 0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Test 400 Bad Request (Invalid Move)
        when(gameService.makeMove(eq(uuid), any(MoveRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid move"));
        mockMvc.perform(post("/games/" + uuid + "/moves")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"column\": 0}"))
                .andExpect(status().isBadRequest());

        // Test 404 Not Found (Game missing)
        when(gameService.makeMove(eq(uuid), any(MoveRequest.class))).thenReturn(null);
        mockMvc.perform(post("/games/" + uuid + "/moves")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"column\": 0}"))
                .andExpect(status().isNotFound());
    }
}
