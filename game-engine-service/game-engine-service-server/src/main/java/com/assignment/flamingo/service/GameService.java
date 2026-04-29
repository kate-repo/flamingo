package com.assignment.flamingo.service;


import com.assignment.flamingo.model.Game;
import com.assignment.flamingo.model.MoveDTO;

import java.util.UUID;

/**
 * Manages the game state.
 *
 * @author Kate
 * @since 23-Apr-2026
 */
public interface GameService {

    /**
     * Handles move request, ensures that each move is legal, checks for winning conditions and saves game state after the move.
     *
     * @param gameId  the game id
     * @param moveDTO the move details
     * @return the game with current status if move was valid, error otherwise
     * @throws IllegalMoveException if the move is illegal (e.g., the target cell is unoccupied)
     */
    Game handleMoveRequest(UUID gameId, MoveDTO moveDTO) throws IllegalMoveException;
}
