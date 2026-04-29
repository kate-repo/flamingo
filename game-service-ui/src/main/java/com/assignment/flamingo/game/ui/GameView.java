package com.assignment.flamingo.game.ui;

import com.assignment.flamingo.base.ui.ViewTitle;
import com.assignment.flamingo.game.GameService;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.model.SessionDTO;
import com.assignment.flamingo.model.SessionDetailsDTO;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Represents game page view, including game board, current status and move history.
 *
 * @author Kate
 * @since 28-Apr-2026
 */
@Route(value = "")
@PageTitle("Play Game")
@Menu(order = 0, icon = "icons/plugin.svg", title = "Play Game")
class GameView extends VerticalLayout {

    private final GameService gameService;

    Button button;
    Grid<MoveDTO> moveGrid;
    BoardView board;
    SessionDetailsDTO gameDetails;

    /**
     * Game view constructor. Initializes game board layout.
     *
     * @param gameService the {@link  GameService} instance
     */
    GameView(GameService gameService) {
        this.gameService = gameService;

        button = createStartSimulationButton();
        board = new BoardView(gameDetails);
        moveGrid = createMoveHistoryGrid();

        var toolbar = new HorizontalLayout();
        toolbar.add(new ViewTitle(""), button, board);
        toolbar.setFlexGrow(1, board);
        toolbar.setWrap(true);
        toolbar.setWidthFull();

        setSizeFull();
        add(toolbar, moveGrid);
    }

    /**
     * Manages the progress of the game and its real time UI representation as it is played automatically by the microservices.
     *
     * @param clickEvent the click event of "Start Simulation" button
     */
    private void simulateGame(ClickEvent<Button> clickEvent) {
        UUID sessionId = gameService.createSession();
        gameDetails = gameService.fetchGameDetails(sessionId);
        updateUiForNewGame();

        UI ui = clickEvent.getSource().getUI().get();
        GameView view = (GameView) ui.getCurrentView();

        CompletableFuture.supplyAsync(() -> {

            while (gameDetails.getGameStatus() == GameStatus.IN_PROGRESS) {
                try {
                    Thread.sleep(1000);
                    gameDetails = gameService.fetchGameDetails(sessionId);

                    ui.access(() -> {
                        Notification.show("Move");
                        view.board.setGameDetails(gameDetails);
                        view.board.populateBoard();
                    });

                } catch (InterruptedException e) {
                    //log error

                } catch (Exception e) {
                    //log error
                }
            }
            return "The game has finished";

        }).thenAccept(s -> {
            ui.access(() -> {
                Notification.show(s);
            });
        });

        if (gameDetails.getGameStatus() == null) {
            Notification.show("Simulation Not Started. Internal Server Error.", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.ERROR);
            return;
        }

        Notification.show("Simulation Started.", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.SUCCESS);

        CompletableFuture.supplyAsync(() -> {
            SessionDTO session = gameService.simulateGame(sessionId);
            return session;

        }).thenAccept(session -> {
            ui.access(() -> {
                Notification.show("Simulation finished.", 3000, Notification.Position.BOTTOM_END)
                        .addThemeVariants(NotificationVariant.SUCCESS);
                // re-enable the button, disableOnClick is on
                clickEvent.getSource().setEnabled(true);

                gameDetails = gameService.fetchGameDetails(sessionId);
                gameDetails.setBoardMap(gameDetails.getMoves().stream()
                        .collect(Collectors.toMap(MoveDTO::getBoardPosition, MoveDTO::getPlayerSymbol)));

                ui.access(() -> {
                    view.board.setGameDetails(gameDetails);
                    view.board.populateBoard();
                    view.board.populateStatus();
                    view.moveGrid.setItems(gameDetails.getMoves().stream()
                            .sorted(Comparator.comparing(MoveDTO::getCreationTimestamp))
                            .collect(Collectors.toList()));
                });
            });

        });
    }

    /**
     * Creates "Start Simulation" button.
     *
     * @return button with styles and click listener
     */
    @NotNull
    private Button createStartSimulationButton() {
        Button button = new Button("Start Simulation");
        button.addClickListener(this::simulateGame);
        button.setDisableOnClick(true);
        button.addThemeVariants(ButtonVariant.PRIMARY);
        return button;
    }

    /**
     * Creates game moves history view.
     *
     * @return grid instance representing move history
     */
    @NotNull
    private static Grid<MoveDTO> createMoveHistoryGrid() {
        Grid<MoveDTO> moveGrid = new Grid<>();
        moveGrid.addColumn(MoveDTO::getCreationTimestamp).setHeader("Timestamp");
        moveGrid.addColumn(MoveDTO::getPlayerSymbol).setHeader("Player");
        moveGrid.addColumn(MoveDTO::getBoardPosition).setHeader("Move");
        moveGrid.setEmptyStateText("There are no moves yet.");
        moveGrid.setSizeFull();
        return moveGrid;
    }

    /**
     * Clears the game board and move history grid.
     */
    private void updateUiForNewGame() {
        board.setGameDetails(gameDetails);
        board.clearBoard();
        board.populateStatus();
        moveGrid.setItems(List.of());
    }

}
