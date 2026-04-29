package com.assignment.flamingo.integration;

import com.assignment.flamingo.GameSessionServiceApplication;
import com.assignment.flamingo.api.GameEngineServiceApiClient;
import com.assignment.flamingo.model.GameDTO;
import com.assignment.flamingo.model.GameStateDTO;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.model.Session;
import com.assignment.flamingo.repository.SessionRepository;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.UUID;

import static com.assignment.flamingo.model.BoardPosition.B3;
import static com.assignment.flamingo.model.PlayerSymbol.X;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = GameSessionServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integration-test.yaml")
public class SessionControllerIT {

    public static final MoveDTO MOVE_DTO = MoveDTO.builder()
            .boardPosition(B3)
            .playerSymbol(X)
            .build();
    private final static UUID SESSION_ID_1 = UUID.fromString("2414925f-13be-4b44-b287-9758234f405d");
    private final static UUID SESSION_ID_2 = UUID.fromString("3dae6233-5b25-484c-b2c8-3aca1ad399d7");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @MockitoBean
    private GameEngineServiceApiClient gameEngineServiceApiClient;

    @Test
    public void simulateSession_200() throws Exception {
        Session session = new Session();
        session = sessionRepository.save(session);

        Optional<Session> optionalSession = sessionRepository.findById(session.getId());
        assertThat(optionalSession).isPresent();

        when(gameEngineServiceApiClient.move(any(), any())).thenReturn(ResponseEntity.ok(GameDTO.builder()
                .id(session.getId())
                .gameStatus(GameStatus.WIN)
                .build()));

        mvc.perform(MockMvcRequestBuilders.post("/sessions/{sessionId}/simulate", session.getId().toString())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(session.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value(GameStatus.WIN.toString()));

        optionalSession = sessionRepository.findById(session.getId());
        assertThat(optionalSession).isPresent();

        //if simulation was already done, API returns the result of previous simulation
        mvc.perform(MockMvcRequestBuilders.post("/sessions/{sessionId}/simulate", session.getId().toString())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(session.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value(GameStatus.WIN.toString()));
    }

    @Test
    public void simulateSession_404() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/sessions/{sessionId}/simulate", SESSION_ID_1.toString())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void create_200() throws Exception {
        Session session = new Session();
        String requestBody = objectMapper.writeValueAsString(session);

        mvc.perform(MockMvcRequestBuilders.post("/sessions")
                        .content(requestBody)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value(GameStatus.IN_PROGRESS.toString()));
    }

    @Test
    public void fetch_200() throws Exception {
        Session session = new Session();
        session = sessionRepository.save(session);

        when(gameEngineServiceApiClient.fetch(session.getId())).thenReturn(ResponseEntity.ok(GameStateDTO.builder()
                .id(session.getId())
                .gameStatus(GameStatus.WIN)
                .build()));

        mvc.perform(MockMvcRequestBuilders.get("/sessions/{sessionId}", session.getId())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value(GameStatus.WIN.toString()));

        Optional<Session> optionalSession = sessionRepository.findById(session.getId());
        assertThat(optionalSession).isPresent();
    }

    @Test
    public void fetch_404() throws Exception {
        Optional<Session> optionalSession = sessionRepository.findById(SESSION_ID_2);
        assertThat(optionalSession).isEmpty();

        mvc.perform(MockMvcRequestBuilders.get("/sessions/{sessionId}", SESSION_ID_2.toString())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
