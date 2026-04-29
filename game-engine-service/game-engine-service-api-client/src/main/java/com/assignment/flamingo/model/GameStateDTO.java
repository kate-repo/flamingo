package com.assignment.flamingo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * @author Kate
 * @since 23-Apr-2026
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameStateDTO {

    private UUID id;

    private GameStatus gameStatus;

    private Map<BoardPosition, PlayerSymbol> boardMap;
}
