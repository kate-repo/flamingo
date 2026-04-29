package com.assignment.flamingo.integration;

import com.assignment.flamingo.GameEngineServiceApplication;
import com.assignment.flamingo.model.Game;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.repository.GameRepository;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.UUID;

import static com.assignment.flamingo.model.BoardPosition.B3;
import static com.assignment.flamingo.model.PlayerSymbol.X;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = GameEngineServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integration-test.yaml")
public class GameControllerIT {

    public static final MoveDTO MOVE_DTO = MoveDTO.builder()
            .boardPosition(B3)
            .playerSymbol(X)
            .build();
    private final static UUID GAME_ID_1 = UUID.fromString("2414925f-13be-4b44-b287-9758234f405d");
    private final static UUID GAME_ID_2 = UUID.fromString("3dae6233-5b25-484c-b2c8-3aca1ad399d7");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void move_200() throws Exception {
        String requestBody = objectMapper.writeValueAsString(MOVE_DTO);

        mvc.perform(MockMvcRequestBuilders.post("/games/{gameId}/move", GAME_ID_1.toString())
                        .content(requestBody)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(GAME_ID_1.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value(GameStatus.IN_PROGRESS.toString()));

        Optional<Game> game = gameRepository.findById(GAME_ID_1);
        assertThat(game).isPresent();
        assertThat(game.get().getBoardCells().size()).isEqualTo(1);
    }

    @Test
    public void move_400() throws Exception {
        Game game = new Game();
        game.setId(GAME_ID_2);
        game = gameRepository.save(game);
        Optional<Game> optionalGame = gameRepository.findById(GAME_ID_2);
        assertThat(optionalGame).isPresent();
        assertThat(optionalGame.get().getBoardCells().size()).isEqualTo(0);
        String requestBody = objectMapper.writeValueAsString(MOVE_DTO);

        mvc.perform(MockMvcRequestBuilders.post("/games/{gameId}/move", GAME_ID_2.toString())
                        .content(requestBody)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(GAME_ID_2.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value(GameStatus.IN_PROGRESS.toString()));

        optionalGame = gameRepository.findById(GAME_ID_2);
        assertThat(optionalGame).isPresent();
        assertThat(optionalGame.get().getBoardCells().size()).isEqualTo(1);

        mvc.perform(MockMvcRequestBuilders.post("/games/{gameId}/move", GAME_ID_2.toString())
                        .content(requestBody)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(String.format("Board position %s is occupied.", MOVE_DTO.getBoardPosition())));
    }
}
