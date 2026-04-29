package com.assignment.flamingo.api;

import com.assignment.flamingo.config.FeignSupportConfig;
import com.assignment.flamingo.model.SessionDTO;
import com.assignment.flamingo.model.SessionDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Declarative API Client for Game Session Service
 * <p>
 * NOTE: please ensure you define in `properties` or `yml` file the correct value
 * for `application.api.game-session-service-api-client.baseurl` property.
 *
 * @author Kate
 * @since 26-Apr-2026
 */
@FeignClient(
        value = "GameSessionServiceApiClient",
        url = "${application.api.game-session-service-api-client.baseurl:http://localhost:8082}",
        configuration = FeignSupportConfig.class)
public interface GameSessionServiceApiClient {

    /**
     * Triggers the automated simulation of a game.
     *
     * @param sessionId the session id
     * @return the session with current status
     */
    @PostMapping("/sessions/{sessionId}/simulate")
    ResponseEntity<SessionDTO> simulate(@PathVariable UUID sessionId);

    /**
     * Creates a new session instance.
     *
     * @return the created session
     */
    @PostMapping("/sessions")
    ResponseEntity<SessionDTO> create(@RequestBody SessionDTO sessionDTO);

    /**
     * Retrieves session details, including the current game state and move history.
     *
     * @param sessionId the session id
     * @return the session details
     */
    @GetMapping("/sessions/{sessionId}")
    ResponseEntity<SessionDetailsDTO> fetch(@PathVariable UUID sessionId);

}
