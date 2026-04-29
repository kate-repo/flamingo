package com.assignment.flamingo.service;

import com.assignment.flamingo.model.BoardCell;
import com.assignment.flamingo.model.BoardPosition;
import com.assignment.flamingo.model.Game;
import com.assignment.flamingo.model.GameStatus;
import com.assignment.flamingo.model.MoveDTO;
import com.assignment.flamingo.model.PlayerSymbol;
import com.assignment.flamingo.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import static com.assignment.flamingo.model.PlayerSymbol.O;
import static com.assignment.flamingo.model.PlayerSymbol.X;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class GameServiceImplTest {

    public static final MoveDTO MOVE_DTO = MoveDTO.builder()
            .boardPosition(B3)
            .playerSymbol(X)
            .build();
    private final static UUID GAME_ID = UUID.fromString("2414925f-13be-4b44-b287-9758234f405d");
    public static final Map<BoardPosition, PlayerSymbol> BOARD_MAP_DRAW = Map.of(
            A2, X,
            B3, O,
            C1, X,
            C2, O,
            B1, X,
            A3, O,
            C3, O,
            A1, O,
            B2, X
    );
    public static final Map<BoardPosition, PlayerSymbol> BOARD_MAP_WIN_X = Map.of(
            B2, X,
            C2, O,
            A2, X,
            B3, O,
            A1, X,
            C1, O,
            A3, X
    );
    public static final Map<BoardPosition, PlayerSymbol> BOARD_MAP_WIN_O = Map.of(
            A1, X,
            B3, O,
            C2, X,
            A3, O,
            B1, X,
            C3, O
    );
    public static final Map<BoardPosition, PlayerSymbol> BOARD_MAP_IN_PROGRESS = Map.of(
            A2, X,
            C1, O,
            B3, X
    );

    @Autowired
    @InjectMocks
    private GameServiceImpl gameService;

    @MockitoBean
    private GameRepository gameRepository;

    @Test
    void handleMoveRequest() throws IllegalMoveException {
        Game game = new Game();
        game.setId(GAME_ID);
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        Game result = gameService.handleMoveRequest(GAME_ID, MOVE_DTO);

        assertEquals(MOVE_DTO.getBoardPosition(), result.getBoardCells().iterator().next().getBoardPosition());
        assertEquals(MOVE_DTO.getPlayerSymbol(), result.getBoardCells().iterator().next().getPlayerSymbol());
        verify(gameRepository, times(1)).findById(GAME_ID);
    }

    @Test
    void handleMoveRequest_GameNotFound() throws IllegalMoveException {
        Game game = new Game();
        game.setId(GAME_ID);
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game result = gameService.handleMoveRequest(GAME_ID, MOVE_DTO);

        assertEquals(MOVE_DTO.getBoardPosition(), result.getBoardCells().iterator().next().getBoardPosition());
        assertEquals(MOVE_DTO.getPlayerSymbol(), result.getBoardCells().iterator().next().getPlayerSymbol());
        verify(gameRepository, times(1)).findById(GAME_ID);
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void handleMoveRequest_IllegalMove() {
        Game game = new Game();
        BoardCell boardCell = new BoardCell();
        boardCell.setBoardPosition(MOVE_DTO.getBoardPosition());
        boardCell.setPlayerSymbol(MOVE_DTO.getPlayerSymbol());
        game.setId(GAME_ID);
        game.setBoardCells(Set.of(boardCell));
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        assertThrows(IllegalMoveException.class, () -> gameService.handleMoveRequest(GAME_ID, MOVE_DTO));
    }

    @Test
    void getCurrentGameStatus_GameInProgress() {
        Game game = new Game();
        game.setBoardCells(convertBoardMapToBoardCellSet(BOARD_MAP_IN_PROGRESS));

        GameStatus result = gameService.getCurrentGameStatus(game, B3, X);

        assertEquals(GameStatus.IN_PROGRESS, result);
    }

    @Test
    void getCurrentGameStatus_Win() {
        Game game = new Game();
        game.setBoardCells(convertBoardMapToBoardCellSet(BOARD_MAP_WIN_O));

        GameStatus result = gameService.getCurrentGameStatus(game, C3, O);

        assertEquals(GameStatus.WIN, result);
    }

    @Test
    void getCurrentGameStatus_Draw() {
        Game game = new Game();
        game.setBoardCells(convertBoardMapToBoardCellSet(BOARD_MAP_DRAW));

        GameStatus result = gameService.getCurrentGameStatus(game, B3, X);

        assertEquals(GameStatus.DRAW, result);
    }

    @Test
    void hasWinningState_True_PlayerXWins() {
        boolean result = gameService.hasWinningState(BOARD_MAP_WIN_X, A3, X);

        assertTrue(result);
    }

    @Test
    void hasWinningState_True_PlayerOWins() {
        boolean result = gameService.hasWinningState(BOARD_MAP_WIN_O, C3, O);

        assertTrue(result);
    }

    @Test
    void hasWinningState_False_Draw() {
        boolean result = gameService.hasWinningState(BOARD_MAP_DRAW, B2, X);

        assertFalse(result);
    }

    @Test
    void hasWinningState_False_GameInProgress() {
        boolean result = gameService.hasWinningState(BOARD_MAP_IN_PROGRESS, B2, X);

        assertFalse(result);
    }

    @Test
    void isBoardFull_True() {
        boolean result = gameService.isBoardFull(BOARD_MAP_DRAW);

        assertTrue(result);
    }

    @Test
    void isBoardFull_False() {
        boolean result = gameService.isBoardFull(Map.of());

        assertFalse(result);

        result = gameService.isBoardFull(BOARD_MAP_IN_PROGRESS);

        assertFalse(result);
    }

    @Test()
    void isBoardFull_Invalid() {
        assertThrows(NullPointerException.class, () -> gameService.isBoardFull(null));
    }

    private static Set<BoardCell> convertBoardMapToBoardCellSet(Map<BoardPosition, PlayerSymbol> boardMap) {
        return boardMap.entrySet().stream()
                .map(entry -> {
                    BoardCell boardCell = new BoardCell();
                    boardCell.setBoardPosition(entry.getKey());
                    boardCell.setPlayerSymbol(entry.getValue());
                    return boardCell;
                })
                .collect(Collectors.toSet());
    }
}
