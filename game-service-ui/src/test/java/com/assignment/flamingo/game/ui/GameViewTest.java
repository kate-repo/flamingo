package com.assignment.flamingo.game.ui;

import com.assignment.flamingo.game.GameService;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.model.SessionDTO;
import com.assignment.flamingo.model.SessionDetailsDTO;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.notification.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.assignment.flamingo.model.BoardPosition.A1;
import static com.assignment.flamingo.model.BoardPosition.A2;
import static com.assignment.flamingo.model.BoardPosition.A3;
import static com.assignment.flamingo.model.BoardPosition.B1;
import static com.assignment.flamingo.model.BoardPosition.B2;
import static com.assignment.flamingo.model.BoardPosition.B3;
import static com.assignment.flamingo.model.BoardPosition.C1;
import static com.assignment.flamingo.model.BoardPosition.C2;
import static com.assignment.flamingo.model.BoardPosition.C3;
import static com.assignment.flamingo.model.PlayerSymbol.O;
import static com.assignment.flamingo.model.PlayerSymbol.X;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class GameViewTest extends SpringBrowserlessTest {

    private final static UUID SESSION_ID = UUID.fromString("2414925f-13be-4b44-b287-9758234f405d");
    private final static ZonedDateTime SIMULATION_ZONED_DATE_TIME = ZonedDateTime.now();

    @MockitoBean
    private GameService gameService;

    @Test
    void empty_grid_shows_no_moves() {
        var view = navigate(GameView.class);

        assertThat(test(view.moveGrid).size()).isZero();
        assertThat(view.moveGrid.getEmptyStateText()).isEqualTo("There are no moves yet.");
        assertThat($(Notification.class).exists()).isFalse();
    }

    @Test
    void click_start_simulation_changes_staus() {
        var view = navigate(GameView.class);
        SessionDetailsDTO session = SessionDetailsDTO.builder()
                .id(SESSION_ID)
                .gameStatus(GameStatus.IN_PROGRESS)
                .moves(Set.of())
                .build();
        when(gameService.createSession()).thenReturn(SESSION_ID);
        when(gameService.fetchGameDetails(SESSION_ID)).thenReturn(session);

        assertThat(view.board.statusButton.getText()).isEqualTo("Click \"Start Simulation\" button to start simulation");

        test(view.button).click();

        assertThat(view.board.statusButton.getText()).isEqualTo("The game is in progress.");
        assertThat($(Notification.class).exists()).isTrue();
    }

    @Test
    void simulation_ends_with_player_O_win() {
        var view = navigate(GameView.class);
        SessionDTO session = SessionDTO.builder()
                .id(SESSION_ID)
                .gameStatus(GameStatus.WIN)
                .build();
        SessionDetailsDTO sessionDetails = SessionDetailsDTO.builder()
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
        when(gameService.createSession()).thenReturn(SESSION_ID);
        when(gameService.fetchGameDetails(SESSION_ID)).thenReturn(sessionDetails);
        when(gameService.simulateGame(SESSION_ID)).thenReturn(session);

        test(view.button).click();

        assertThat(view.board.statusButton.getText()).isEqualTo("Player O wins!");
        assertThat($(Notification.class).exists()).isTrue();
        verify(gameService, times(1)).createSession();
        verify(gameService, times(1)).simulateGame(SESSION_ID);
    }

    @Test
    void simulation_ends_with_player_X_win() {
        var view = navigate(GameView.class);
        SessionDTO session = SessionDTO.builder()
                .id(SESSION_ID)
                .gameStatus(GameStatus.WIN)
                .build();
        SessionDetailsDTO sessionDetails = SessionDetailsDTO.builder()
                .id(SESSION_ID)
                .gameStatus(GameStatus.WIN)
                .boardMap(Map.of(
                        B2, X,
                        C2, O,
                        A2, X,
                        B3, O,
                        A1, X,
                        C1, O,
                        A3, X
                ))
                .moves(Set.of(
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME).boardPosition(B2).playerSymbol(X).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(1)).boardPosition(C2).playerSymbol(O).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(2)).boardPosition(A2).playerSymbol(X).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(3)).boardPosition(B3).playerSymbol(O).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(4)).boardPosition(A1).playerSymbol(X).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(5)).boardPosition(C1).playerSymbol(O).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(6)).boardPosition(A3).playerSymbol(X).build()
                ))
                .build();
        when(gameService.createSession()).thenReturn(SESSION_ID);
        when(gameService.fetchGameDetails(SESSION_ID)).thenReturn(sessionDetails);
        when(gameService.simulateGame(SESSION_ID)).thenReturn(session);

        test(view.button).click();

        assertThat(view.board.statusButton.getText()).isEqualTo("Player X wins!");
        assertThat($(Notification.class).exists()).isTrue();
        verify(gameService, times(1)).createSession();
        verify(gameService, times(1)).simulateGame(SESSION_ID);
    }

    @Test
    void simulation_ends_with_draw() {
        var view = navigate(GameView.class);
        SessionDTO session = SessionDTO.builder()
                .id(SESSION_ID)
                .gameStatus(GameStatus.DRAW)
                .build();
        SessionDetailsDTO sessionDetails = SessionDetailsDTO.builder()
                .id(SESSION_ID)
                .gameStatus(GameStatus.DRAW)
                .boardMap(Map.of(
                        A2, X,
                        B3, O,
                        C1, X,
                        C2, O,
                        B1, X,
                        A3, O,
                        C3, O,
                        A1, O,
                        B2, X
                ))
                .moves(Set.of(
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME).boardPosition(A2).playerSymbol(X).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(1)).boardPosition(B3).playerSymbol(O).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(2)).boardPosition(C1).playerSymbol(X).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(3)).boardPosition(C2).playerSymbol(O).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(4)).boardPosition(B1).playerSymbol(X).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(5)).boardPosition(A3).playerSymbol(O).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(6)).boardPosition(C3).playerSymbol(X).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(7)).boardPosition(A1).playerSymbol(O).build(),
                        MoveDTO.builder().creationTimestamp(SIMULATION_ZONED_DATE_TIME.plusSeconds(8)).boardPosition(B2).playerSymbol(X).build()
                ))
                .build();
        when(gameService.createSession()).thenReturn(SESSION_ID);
        when(gameService.fetchGameDetails(SESSION_ID)).thenReturn(sessionDetails);
        when(gameService.simulateGame(SESSION_ID)).thenReturn(session);

        test(view.button).click();

        assertThat(view.board.statusButton.getText()).isEqualTo("It's a draw!");
        assertThat($(Notification.class).exists()).isTrue();
        verify(gameService, times(1)).createSession();
        verify(gameService, times(1)).simulateGame(SESSION_ID);
    }
}
