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
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Manages the game session.
 *
 * @author Kate
 * @since 23-Apr-2026
 */
@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private GameEngineServiceApiClient gameEngineServiceApiClient;

    private static final Random PRNG = new Random();

    /**
     * Simulate moves for both players (e.g., using a simple random or rule-based algorithm) and forward these moves to the Game Engine Service.
     *
     * @param sessionId the session id
     * @return the session and game state after simulation was finished
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<Session> simulateGame(UUID sessionId) {

        Optional<Session> optionalSession = sessionRepository.findById(sessionId);

        if (optionalSession.isEmpty()) {
            return Optional.empty();
        }

        Session session = optionalSession.get();

        for (int i = 0; session.getGameStatus() == GameStatus.IN_PROGRESS; i++) {

            PlayerSymbol playerSymbol = i % 2 == 0 ? PlayerSymbol.X : PlayerSymbol.O;
            BoardPosition boardPosition = generateRandomMove();

            try {
                ResponseEntity<GameDTO> responseEntity = gameEngineServiceApiClient.move(sessionId, MoveDTO.builder()
                        .boardPosition(boardPosition)
                        .playerSymbol(playerSymbol)
                        .build());

                Move move = createMove(boardPosition, playerSymbol, session);
                session.getMoves().add(move);
                session.setGameStatus(responseEntity.getBody().getGameStatus());

                Thread.sleep(1000);

            } catch (FeignException e) {
                //get error details from the response, log and/or save to db as a reason the move is illegal
                //repeat move for the same player
                i--;
            } catch (InterruptedException e) {
                //log/do what is needed
                throw new RuntimeException(e);
            }
        }
        return Optional.of(session);
    }

    /**
     * Creates a new session instance.
     *
     * @param session a session instance
     * @return the created session
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    /**
     * Retrieves session details, including the current game state and move history.
     *
     * @param sessionId the session id
     * @return the session details
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Session> fetchSessionDetails(UUID sessionId) {

        Optional<Session> optionalSession = sessionRepository.findById(sessionId);

        if (optionalSession.isEmpty()) {
            return Optional.empty();
        }

        Session session = optionalSession.get();

        try {
            ResponseEntity<GameStateDTO> responseEntity = gameEngineServiceApiClient.fetch(sessionId);

            session.setGameStatus(responseEntity.getBody().getGameStatus());
            session.setBoardMap(responseEntity.getBody().getBoardMap());

        } catch (FeignException e) {
            //get error details from the response, log/show on UI
        }
        return Optional.of(session);
    }

    /**
     * Creates a new move instance.
     *
     * @param boardPosition the move position
     * @param playerSymbol  the move symbol
     * @param session       the session that move is made in
     * @return a created move instance
     */
    protected Move createMove(BoardPosition boardPosition, PlayerSymbol playerSymbol, Session session) {
        Move move = new Move();
        move.setCreationTimestamp(ZonedDateTime.now());
        move.setBoardPosition(boardPosition);
        move.setPlayerSymbol(playerSymbol);
        move.setSession(session);
        return move;
    }

    /**
     * Selects a random position on the board.
     *
     * @return randomly selected board position
     */
    protected BoardPosition generateRandomMove() {
        return BoardPosition.values()[PRNG.nextInt(BoardPosition.values().length)];
    }
}
