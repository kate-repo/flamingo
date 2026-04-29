package com.assignment.flamingo.controller;

import com.assignment.flamingo.model.Session;
import com.assignment.flamingo.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

/**
 * Rest resource that manages the game session.
 *
 * @author Kate
 * @since 26-Apr-2026
 */
@RestController
public class SessionController {

    @Autowired
    private SessionService sessionService;

    /**
     * Triggers the automated simulation of a game.
     *
     * @param sessionId the session id
     * @return the session with current status
     */
    @PostMapping("/sessions/{sessionId}/simulate")
    public ResponseEntity<Session> simulateSession(@PathVariable UUID sessionId) {
        Optional<Session> optionalSession = sessionService.simulateGame(sessionId);
        return optionalSession.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new session instance.
     *
     * @param sessionDTO the session details
     * @return the created session
     */
    @PostMapping("/sessions")
    ResponseEntity<Session> create(@RequestBody Session sessionDTO) {
        Session session = sessionService.save(sessionDTO);
        return ResponseEntity.ok(session);
    }

    /**
     * Retrieves session details, including the current game state and move history.
     *
     * @param sessionId the session id
     * @return the session details
     */
    @GetMapping("/sessions/{sessionId}")
    ResponseEntity<Session> fetch(@PathVariable UUID sessionId) {
        Optional<Session> optionalSession = sessionService.fetchSessionDetails(sessionId);
        return optionalSession.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
