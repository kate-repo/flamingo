package com.assignment.flamingo.game;

import com.assignment.flamingo.api.GameSessionServiceApiClient;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.SessionDTO;
import com.assignment.flamingo.model.SessionDetailsDTO;
import feign.FeignException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Manages data retrieval for game page view.
 *
 * @author Kate
 * @since 28-Apr-2026
 */
@Service
public class GameService {

    private final GameSessionServiceApiClient gameSessionServiceApiClient;

    GameService(GameSessionServiceApiClient gameSessionServiceApiClient) {
        this.gameSessionServiceApiClient = gameSessionServiceApiClient;
    }

    /**
     * Calls Game Session Service API that starts simulation.
     *
     * @param sessionId the current session id
     * @return the session details after simulation is finished
     */
    public @Nullable SessionDTO simulateGame(UUID sessionId) {
        try {
            ResponseEntity<SessionDTO> responseEntity = gameSessionServiceApiClient.simulate(sessionId);
            return responseEntity.getBody();

        } catch (FeignException e) {
            //get error details from the response, log/show on UI
        }
        return null;
    }

    /**
     * Calls Game Session Service API that creates a new session.
     *
     * @return the created session details
     */
    public UUID createSession() {
        try {
            ResponseEntity<SessionDTO> responseEntity = gameSessionServiceApiClient.create(SessionDTO.builder().gameStatus(GameStatus.IN_PROGRESS).build());
            return responseEntity.getBody().getId();

        } catch (FeignException e) {
            //get error details from the response, log/show on UI
        }
        return null;
    }

    /**
     * Calls Game Session Service API that retrieves session details.
     *
     * @param sessionId the session id
     * @return the session details
     */
    public SessionDetailsDTO fetchGameDetails(UUID sessionId) {
        try {
            ResponseEntity<SessionDetailsDTO> responseEntity = gameSessionServiceApiClient.fetch(sessionId);
            return responseEntity.getBody();

        } catch (FeignException e) {
            //get error details from the response, log/show on UI
        }
        return null;
    }
}
