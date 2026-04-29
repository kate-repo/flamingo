package com.assignment.flamingo.api;

import com.assignment.flamingo.model.GameDTO;
import com.assignment.flamingo.model.GameStateDTO;
import com.assignment.flamingo.model.MoveDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Declarative API Client for Game Engine Service
 * <p>
 * NOTE: please ensure you define in `properties` or `yml` file the correct value
 * for `application.api.game-engine-service-api-client.baseurl` property.
 *
 * @author Kate
 * @since 23-Apr-2026
 */
@FeignClient(
        value = "GameEngineServiceApiClient",
        url = "${application.api.game-engine-service-api-client.baseurl:http://localhost:8081}")
public interface GameEngineServiceApiClient {

    /**
     * Receives a move request containing the player symbol and board position.
     *
     * @param gameId  the game id
     * @param moveDTO the move details
     * @return the game with current status if move was valid, error otherwise
     */
    @PostMapping("/games/{gameId}/move")
    ResponseEntity<GameDTO> move(@PathVariable UUID gameId, @Valid @RequestBody MoveDTO moveDTO);

    /**
     * Creates a new game instance with provided id and status.
     *
     * @param gameDTO the game details
     * @return the created game if the game details were valid, error otherwise
     */
    @PostMapping("/games")
    ResponseEntity<GameDTO> create(@Valid @RequestBody GameDTO gameDTO);

    /**
     * Retrieves the current state of the game (board and status).
     *
     * @param gameId the game id
     * @return the current state of the game
     */
    @GetMapping("/games/{gameId}")
    ResponseEntity<GameStateDTO> fetch(@PathVariable UUID gameId);

}
