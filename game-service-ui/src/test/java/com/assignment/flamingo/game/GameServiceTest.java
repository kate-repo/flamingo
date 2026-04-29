package com.assignment.flamingo.game;

import com.assignment.flamingo.api.GameSessionServiceApiClient;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.SessionDTO;
import com.assignment.flamingo.model.SessionDetailsDTO;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class GameServiceTest {

    private final static UUID SESSION_ID = UUID.fromString("2414925f-13be-4b44-b287-9758234f405d");

    @Autowired
    private GameService gameService;

    @MockitoBean
    private GameSessionServiceApiClient gameSessionServiceApiClient;

    @Test
    public void create_game_session() {
        SessionDTO session = SessionDTO.builder()
                .id(SESSION_ID)
                .gameStatus(GameStatus.IN_PROGRESS)
                .build();
        when(gameSessionServiceApiClient.create(any(SessionDTO.class))).thenReturn(ResponseEntity.ok(session));

        UUID sessionId = gameService.createSession();

        assertThat(sessionId).isNotNull();
    }

    @Test
    public void fetch_game_session() {
        SessionDetailsDTO session = SessionDetailsDTO.builder()
                .id(SESSION_ID)
                .gameStatus(GameStatus.IN_PROGRESS)
                .build();
        when(gameSessionServiceApiClient.fetch(SESSION_ID)).thenReturn(ResponseEntity.ok(session));

        SessionDetailsDTO sessionDetailsDTO = gameService.fetchGameDetails(SESSION_ID);

        assertThat(sessionDetailsDTO).isNotNull();
        assertThat(sessionDetailsDTO.getId()).isEqualTo(SESSION_ID);
    }

    @Test
    public void simulate_game() {
        SessionDTO session = SessionDTO.builder()
                .id(SESSION_ID)
                .gameStatus(GameStatus.IN_PROGRESS)
                .build();

        when(gameSessionServiceApiClient.simulate(SESSION_ID)).thenReturn(ResponseEntity.ok(session));

        @Nullable SessionDTO sessionDTO = gameService.simulateGame(SESSION_ID);

        assertThat(sessionDTO).isNotNull();
        assertThat(sessionDTO.getId()).isEqualTo(SESSION_ID);
    }

}
