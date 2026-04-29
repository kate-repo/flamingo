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
public class GameRestRepositoryIT {

    public static final MoveDTO MOVE_DTO = MoveDTO.builder()
            .boardPosition(B3)
            .playerSymbol(X)
            .build();
    private final static UUID GAME_ID_3 = UUID.fromString("8787925f-13be-4b44-b287-9758234f405a");
    private final static UUID GAME_ID_4 = UUID.fromString("5678925f-13be-4b44-b287-9758234f405b");
    private final static UUID GAME_ID_5 = UUID.fromString("1234925f-13be-4b44-b287-9758234f405c");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void save_200() throws Exception {
        Optional<Game> optionalGame = gameRepository.findById(GAME_ID_3);
        assertThat(optionalGame).isEmpty();

        Game game = new Game();
        game.setId(GAME_ID_3);
        String requestBody = objectMapper.writeValueAsString(game);

        mvc.perform(MockMvcRequestBuilders.post("/games")
                        .content(requestBody)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        optionalGame = gameRepository.findById(GAME_ID_3);
        assertThat(optionalGame).isPresent();
        assertThat(optionalGame.get().getBoardCells().size()).isEqualTo(0);
    }

    @Test
    public void findById_200() throws Exception {
        Optional<Game> optionalGame = gameRepository.findById(GAME_ID_5);
        assertThat(optionalGame).isEmpty();

        Game game = new Game();
        game.setId(GAME_ID_5);
        gameRepository.save(game);

        mvc.perform(MockMvcRequestBuilders.get("/games/{gameId}", GAME_ID_5.toString())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameStatus").value(GameStatus.IN_PROGRESS.toString()));

        optionalGame = gameRepository.findById(GAME_ID_5);
        assertThat(optionalGame).isPresent();
        assertThat(optionalGame.get().getBoardCells().size()).isEqualTo(0);
    }

    @Test
    public void findById_404() throws Exception {
        Optional<Game> optionalGame = gameRepository.findById(GAME_ID_4);
        assertThat(optionalGame).isEmpty();

        mvc.perform(MockMvcRequestBuilders.get("/games/{gameId}", GAME_ID_4.toString())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
