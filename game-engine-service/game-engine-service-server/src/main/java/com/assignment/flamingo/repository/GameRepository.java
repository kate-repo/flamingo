package com.assignment.flamingo.repository;

import com.assignment.flamingo.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;
import java.util.UUID;

/**
 * Rest resource and CRUD repository that manages the game.
 *
 * @author Kate
 * @since 23-Apr-2026
 */
@RepositoryRestResource
public interface GameRepository extends CrudRepository<Game, UUID> {

    /**
     * Creates or updates a game instance.
     *
     * @param game the game details
     * @return the created or updated game if the game details were valid, error otherwise
     */
    @Override
    @RestResource
    Game save(Game game);

    /**
     * Retrieves the current state of the game (board and status).
     *
     * @param id the game id
     * @return the current state of the game
     */
    @Override
    @RestResource
    Optional<Game> findById(UUID id);
}
