package com.assignment.flamingo.game.ui;

import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.model.SessionDetailsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.assignment.flamingo.model.BoardPosition.A1;
import static com.assignment.flamingo.model.BoardPosition.A3;
import static com.assignment.flamingo.model.BoardPosition.B1;
import static com.assignment.flamingo.model.BoardPosition.B3;
import static com.assignment.flamingo.model.BoardPosition.C2;
import static com.assignment.flamingo.model.BoardPosition.C3;
import static com.assignment.flamingo.model.PlayerSymbol.O;
import static com.assignment.flamingo.model.PlayerSymbol.X;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BoardViewTest {

    private final static UUID SESSION_ID = UUID.fromString("2414925f-13be-4b44-b287-9758234f405d");
    private final static ZonedDateTime SIMULATION_ZONED_DATE_TIME = ZonedDateTime.now();
    private final static SessionDetailsDTO WIN_SESSION_DETAILS = SessionDetailsDTO.builder()
            .id(SESSION_ID)
            .gameStatus(GameStatus.WIN)
            .boardMap(Map.of(
                    A1, X,
                    B3, O,
                    C2, X,
                    A3, O,
                    B1, X,
                    C3, O
            ))
            .moves(Set.of(
                    MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME).boardPosition(A1).playerSymbol(X).build(),
                    MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(1)).boardPosition(B3).playerSymbol(O).build(),
                    MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(2)).boardPosition(C2).playerSymbol(X).build(),
                    MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(3)).boardPosition(A3).playerSymbol(O).build(),
                    MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(4)).boardPosition(B1).playerSymbol(X).build(),
                    MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(5)).boardPosition(C3).playerSymbol(O).build()
            ))
            .build();
    private final static SessionDetailsDTO DRAW_SESSION_DETAILS = SessionDetailsDTO.builder()
            .id(SESSION_ID)
            .gameStatus(GameStatus.DRAW)
            .build();
    private final static SessionDetailsDTO EMPTY_SESSION_DETAILS = SessionDetailsDTO.builder()
            .id(SESSION_ID)
            .gameStatus(GameStatus.IN_PROGRESS)
            .build();
    private static BoardView board;

    @Test
    void populate_status_no_session() {
        board = new BoardView(null);

        assertThat(board.statusButton.getText()).isEqualTo("Click \"Start Simulation\" button to start simulation");
    }

    @Test
    void populate_status_new_session() {
        board = new BoardView(EMPTY_SESSION_DETAILS);

        assertThat(board.statusButton.getText()).isEqualTo("The game is in progress.");
    }

    @Test
    void populate_status_win_session() {
        board = new BoardView(WIN_SESSION_DETAILS);

        assertThat(board.statusButton.getText()).isEqualTo("Player O wins!");
    }

    @Test
    void populate_status_draw_session() {
        board = new BoardView(DRAW_SESSION_DETAILS);

        assertThat(board.statusButton.getText()).isEqualTo("It's a draw!");
    }

}
