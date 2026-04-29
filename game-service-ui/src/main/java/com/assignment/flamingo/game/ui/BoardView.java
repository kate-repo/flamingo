package com.assignment.flamingo.game.ui;

import com.assignment.flamingo.model.BoardPosition;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.model.SessionDetailsDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Represents game board view, 3x3 board and current status.
 *
 * @author Kate
 * @since 28-Apr-2026
 */
public class BoardView extends VerticalLayout {

    Button statusButton = new Button();
    Button a1 = new Button();
    Button b1 = new Button();
    Button c1 = new Button();
    Button a2 = new Button();
    Button b2 = new Button();
    Button c2 = new Button();
    Button a3 = new Button();
    Button b3 = new Button();
    Button c3 = new Button();
    SessionDetailsDTO gameDetails;

    /**
     * Board view constructor. Initializes game board layout.
     *
     * @param gameDetails
     */
    public BoardView(SessionDetailsDTO gameDetails) {
        this.gameDetails = gameDetails;
        this.statusButton.addClassName("status-button");

        HorizontalLayout game = new HorizontalLayout();

        VerticalLayout board = createBoard();
        populateBoard();

        VerticalLayout status = new VerticalLayout();
        populateStatus();
        status.add(statusButton);

        game.add(board, status);
        add(game);
    }

    /**
     * Updates staus text on UI.
     */
    public void populateStatus() {
        statusButton.setText(getGameStatus());
    }

    /**
     * Creates UI game board layout.
     *
     * @return the created board layout
     */
    public VerticalLayout createBoard() {

        HorizontalLayout row1 = new HorizontalLayout(a1, b1, c1);
        HorizontalLayout row2 = new HorizontalLayout(a2, b2, c2);
        HorizontalLayout row3 = new HorizontalLayout(a3, b3, c3);

        return new VerticalLayout(
                row1,
                row2,
                row3);
    }

    /**
     * Populates UI game board layout with current state of the game.
     */
    public void populateBoard() {

        if (gameDetails != null && gameDetails.getBoardMap() != null) {

            if (gameDetails.getBoardMap().containsKey(BoardPosition.A1)) {
                a1.setText(gameDetails.getBoardMap().get(BoardPosition.A1).name());
            }
            if (gameDetails.getBoardMap().containsKey(BoardPosition.B1)) {
                b1.setText(gameDetails.getBoardMap().get(BoardPosition.B1).name());
            }
            if (gameDetails.getBoardMap().containsKey(BoardPosition.C1)) {
                c1.setText(gameDetails.getBoardMap().get(BoardPosition.C1).name());
            }
        }

        if (gameDetails != null && gameDetails.getBoardMap() != null) {

            if (gameDetails.getBoardMap().containsKey(BoardPosition.A2)) {
                a2.setText(gameDetails.getBoardMap().get(BoardPosition.A2).name());
            }
            if (gameDetails.getBoardMap().containsKey(BoardPosition.B2)) {
                b2.setText(gameDetails.getBoardMap().get(BoardPosition.B2).name());
            }
            if (gameDetails.getBoardMap().containsKey(BoardPosition.C2)) {
                c2.setText(gameDetails.getBoardMap().get(BoardPosition.C2).name());
            }
        }

        if (gameDetails != null && gameDetails.getBoardMap() != null) {

            if (gameDetails.getBoardMap().containsKey(BoardPosition.A3)) {
                a3.setText(gameDetails.getBoardMap().get(BoardPosition.A3).name());
            }
            if (gameDetails.getBoardMap().containsKey(BoardPosition.B3)) {
                b3.setText(gameDetails.getBoardMap().get(BoardPosition.B3).name());
            }
            if (gameDetails.getBoardMap().containsKey(BoardPosition.C3)) {
                c3.setText(gameDetails.getBoardMap().get(BoardPosition.C3).name());
            }
        }
    }

    /**
     * Clear UI game board layout for the new game.
     */
    public void clearBoard() {

        a1.setText("");
        b1.setText("");
        c1.setText("");
        a2.setText("");
        b2.setText("");
        c2.setText("");
        a3.setText("");
        b3.setText("");
        c3.setText("");
    }

    /**
     * Updates the current state of the game.
     *
     * @param gameDetails
     */
    public void setGameDetails(SessionDetailsDTO gameDetails) {
        this.gameDetails = gameDetails;
    }

    /**
     * Generates UI text for the current game state.
     *
     * @return string describing the current game state
     */
    @NotNull
    private String getGameStatus() {
        if (gameDetails == null) {
            return "Click \"Start Simulation\" button to start simulation";

        } else if (GameStatus.IN_PROGRESS.equals(gameDetails.getGameStatus())) {
            return "The game is in progress.";

        } else if (GameStatus.WIN.equals(gameDetails.getGameStatus())) {
            return String.format("Player %s wins!", gameDetails.getMoves().stream()
                    .sorted(Comparator.comparing(MoveDTO::getCreationTimestamp).reversed())
                    .findFirst()
                    .get()
                    .getPlayerSymbol());
        }
        return "It's a draw!";
    }
}
