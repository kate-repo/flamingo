package com.assignment.flamingo.service;

import com.assignment.flamingo.model.BoardCell;
import com.assignment.flamingo.model.BoardPosition;
import com.assignment.flamingo.model.Game;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.model.PlayerSymbol;
import com.assignment.flamingo.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.assignment.flamingo.model.BoardPosition.A1;
import static com.assignment.flamingo.model.BoardPosition.A2;
import static com.assignment.flamingo.model.BoardPosition.A3;
import static com.assignment.flamingo.model.BoardPosition.B1;
import static com.assignment.flamingo.model.BoardPosition.B2;
import static com.assignment.flamingo.model.BoardPosition.B3;
import static com.assignment.flamingo.model.BoardPosition.C1;
import static com.assignment.flamingo.model.BoardPosition.C2;
import static com.assignment.flamingo.model.BoardPosition.C3;

/**
 * Manages the game state.
 *
 * @author Kate
 * @since 23-Apr-2026
 */
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    /**
     * The list of all game board lines.
     */
    private final List<List<BoardPosition>> lines = List.of(
            List.of(A1, A2, A3),
            List.of(B1, B2, B3),
            List.of(C1, C2, C3),
            List.of(A1, B1, C1),
            List.of(A2, B2, C2),
            List.of(A3, B3, C3),
            List.of(A1, B2, C3),
            List.of(A3, B2, C1)
    );

    /**
     * Handles move request, ensures that each move is legal, checks for winning conditions and saves game state after the move.
     *
     * @param gameId  the game id
     * @param moveDTO the move details
     * @return the game with current status if move was valid, error otherwise
     * @throws IllegalMoveException if the move is illegal (e.g., the target cell is unoccupied)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Game handleMoveRequest(UUID gameId, MoveDTO moveDTO) throws IllegalMoveException {

        Game game = gameRepository.findById(gameId)
                .orElseGet(() -> {
                    Game newGame = new Game();
                    newGame.setId(gameId);
                    return gameRepository.save(newGame);
                });

        Optional<BoardCell> targetBoardCell = game.getBoardCells().stream()
                .filter(boardCell -> boardCell.getBoardPosition().equals(moveDTO.getBoardPosition()))
                .findAny();

        if (targetBoardCell.isPresent()) {
            throw new IllegalMoveException(String.format("Board position %s is occupied.", moveDTO.getBoardPosition()));
        }

        BoardCell boardCell = new BoardCell();
        boardCell.setBoardPosition(moveDTO.getBoardPosition());
        boardCell.setPlayerSymbol(moveDTO.getPlayerSymbol());
        boardCell.setGame(game);

        game.getBoardCells().add(boardCell);
        game.setGameStatus(getCurrentGameStatus(game, boardCell.getBoardPosition(), boardCell.getPlayerSymbol()));
        return game;
    }

    /**
     * Checks for winning and draw conditions of the given move and returns updated status.
     *
     * @param game          the game the move was made for
     * @param boardPosition the move position
     * @param playerSymbol  the move symbol
     * @return the current status of the game after the given move
     */
    protected GameStatus getCurrentGameStatus(Game game, BoardPosition boardPosition, PlayerSymbol playerSymbol) {
        Map<BoardPosition, PlayerSymbol> boardMap = game.getBoardCells().stream()
                .collect(Collectors.toMap(BoardCell::getBoardPosition, BoardCell::getPlayerSymbol));

        if (hasWinningState(boardMap, boardPosition, playerSymbol)) {
            return GameStatus.WIN;

        } else if (isBoardFull(boardMap)) {
            return GameStatus.DRAW;
        }
        return GameStatus.IN_PROGRESS;
    }

    /**
     * Checks if winning conditions of the move were met for given game board.
     *
     * @param boardMap      the game board
     * @param boardPosition the move position
     * @param playerSymbol  the move symbol
     * @return true if winning conditions were met, false otherwise
     */
    protected boolean hasWinningState(Map<BoardPosition, PlayerSymbol> boardMap, BoardPosition boardPosition, PlayerSymbol playerSymbol) {
        return lines.stream()
                .filter(line -> line.contains(boardPosition))
                .anyMatch(line -> line.stream()
                        .allMatch(c -> boardMap.containsKey(c) && playerSymbol.equals(boardMap.get(c))));

    }

    /**
     * Checks if the game board if full.
     *
     * @param boardMap the game board
     * @return true if the game board is full, false otherwise
     */
    protected boolean isBoardFull(Map<BoardPosition, PlayerSymbol> boardMap) {
        return boardMap.keySet().containsAll(Arrays.asList(BoardPosition.values()));
    }
}
