package com.assignment.flamingo.repository.eventhandlers;

import com.assignment.flamingo.api.GameEngineServiceApiClient;
import com.assignment.flamingo.model.GameDTO;
import com.assignment.flamingo.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * Rest resource repository event handler.
 */
@Component
@RepositoryEventHandler
public class SessionRepositoryEventHandler {

    @Autowired
    private GameEngineServiceApiClient gameEngineServiceApiClient;

    /**
     * Aspect that needs to be executed after a new game session was created.
     *
     * @param session a newly created session
     */
    @HandleAfterCreate
    public void handleGameInitialization(Session session) {
        //create Game object in GES
        gameEngineServiceApiClient.create(GameDTO.builder()
                .id(session.getId())
                .gameStatus(session.getGameStatus())
                .build());
    }
}
