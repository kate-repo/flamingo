package com.assignment.flamingo.controller;

import com.assignment.flamingo.model.Game;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.service.GameService;
import com.assignment.flamingo.service.IllegalMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Rest resource that manages the game state.
 *
 * @author Kate
 * @since 23-Apr-2026
 */
@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * Receives a move request containing the player symbol and board position.
     *
     * @param gameId  the game id
     * @param moveDTO the move details
     * @return the game with current status if move was valid, error otherwise
     */
    @PostMapping("/games/{gameId}/move")
    public ResponseEntity<? extends Object> move(@PathVariable UUID gameId, @RequestBody MoveDTO moveDTO) {
        Game game = null;
        try {
            game = gameService.handleMoveRequest(gameId, moveDTO);

        } catch (IllegalMoveException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
        return ResponseEntity.ok(game);
    }
}
