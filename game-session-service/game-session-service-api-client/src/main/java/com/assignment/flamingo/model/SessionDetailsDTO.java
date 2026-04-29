package com.assignment.flamingo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Kate
 * @since 26-Apr-2026
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDetailsDTO {

    private UUID id;

    private GameStatus gameStatus;

    private Map<BoardPosition, PlayerSymbol> boardMap;

    private Set<MoveDTO> moves;
}
