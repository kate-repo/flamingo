package com.assignment.flamingo.service;


import com.assignment.flamingo.model.Session;

import java.util.Optional;
import java.util.UUID;

/**
 * Manages the game session.
 *
 * @author Kate
 * @since 23-Apr-2026
 */
public interface SessionService {

    /**
     * Simulate moves for both players (e.g., using a simple random or rule-based algorithm) and forward these moves to the Game Engine Service.
     *
     * @param sessionId the session id
     * @return the session and game state after simulation was finished
     */
    Optional<Session> simulateGame(UUID sessionId);

    /**
     * Creates a new session instance.
     *
     * @param session a session instance
     * @return the created session
     */
    Session save(Session session);

    /**
     * Retrieves session details, including the current game state and move history.
     *
     * @param sessionId the session id
     * @return the session details
     */
    Optional<Session> fetchSessionDetails(UUID sessionId);
}
