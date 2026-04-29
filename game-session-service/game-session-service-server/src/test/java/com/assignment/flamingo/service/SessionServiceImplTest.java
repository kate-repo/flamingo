package com.assignment.flamingo.service;

import com.assignment.flamingo.api.GameEngineServiceApiClient;
import com.assignment.flamingo.model.BoardPosition;
import com.assignment.flamingo.model.GameDTO;
import com.assignment.flamingo.model.GameStateDTO;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.Move;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.model.PlayerSymbol;
import com.assignment.flamingo.model.Session;
import com.assignment.flamingo.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SessionServiceImplTest {

    private final static UUID SESSION_ID = UUID.fromString("2414925f-13be-4b44-b287-9758234f405d");
    public static final MoveDTO MOVE_DTO = MoveDTO.builder()
            .boardPosition(BoardPosition.A1)
            .playerSymbol(PlayerSymbol.X)
            .build();

    @Autowired
    @InjectMocks
    private SessionServiceImpl sessionService;

    @MockitoBean
    private SessionRepository sessionRepository;

    @MockitoBean
    private GameEngineServiceApiClient gameEngineServiceApiClient;

    @Test
    void simulateGame() {
        Session session = new Session();
        session.setId(SESSION_ID);
        when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));
        when(gameEngineServiceApiClient.move(any(UUID.class), any(MoveDTO.class))).thenReturn(ResponseEntity.ok(GameDTO.builder().build()));

        Optional<Session> result = sessionService.simulateGame(SESSION_ID);

        assertEquals(SESSION_ID, result.get().getId());
        assertNotEquals(GameStatus.IN_PROGRESS, result.get().getGameStatus());
        verify(sessionRepository, times(1)).findById(SESSION_ID);
        verify(gameEngineServiceApiClient).move(any(UUID.class), any(MoveDTO.class));
    }

    @Test
    void simulateGame_NotFound() {
        when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.empty());

        Optional<Session> optionalSession = sessionService.simulateGame(SESSION_ID);

        assertThat(optionalSession).isEmpty();
        verify(sessionRepository, times(1)).findById(SESSION_ID);
    }

    @Test
    void save() {
        Session session = new Session();
        session.setId(SESSION_ID);
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.save(session);

        assertEquals(SESSION_ID, result.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void fetchSessionDetails() {
        Session session = new Session();
        session.setId(SESSION_ID);
        when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));
        when(gameEngineServiceApiClient.fetch(SESSION_ID)).thenReturn(ResponseEntity.ok(GameStateDTO.builder().build()));

        Optional<Session> result = sessionService.fetchSessionDetails(SESSION_ID);

        assertThat(result).isPresent();
        assertEquals(SESSION_ID, result.get().getId());
        verify(sessionRepository, times(1)).findById(SESSION_ID);
        verify(gameEngineServiceApiClient, times(1)).fetch(SESSION_ID);
    }

    @Test
    void fetchSessionDetails_NotFound() {
        when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.empty());

        Optional<Session> optionalSession = sessionService.simulateGame(SESSION_ID);

        assertThat(optionalSession).isEmpty();
        verify(sessionRepository, times(1)).findById(SESSION_ID);
    }

    @Test
    void createMove() {
        Session session = new Session();
        session.setId(SESSION_ID);

        Move result = sessionService.createMove(MOVE_DTO.getBoardPosition(), MOVE_DTO.getPlayerSymbol(), session);

        assertEquals(session.getId(), result.getSession().getId());
        assertEquals(MOVE_DTO.getBoardPosition(), result.getBoardPosition());
        assertEquals(MOVE_DTO.getPlayerSymbol(), result.getPlayerSymbol());
    }

    @Test
    void generateRandomMove() {
        BoardPosition result = sessionService.generateRandomMove();

        assertTrue(Arrays.asList(BoardPosition.values()).contains(result));
    }
}
