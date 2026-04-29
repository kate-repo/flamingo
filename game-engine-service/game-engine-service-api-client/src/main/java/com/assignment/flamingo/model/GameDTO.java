package com.assignment.flamingo.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Kate
 * @since 23-Apr-2026
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {

    @NotEmpty
    private UUID id;

    @NotEmpty
    private GameStatus gameStatus;
}
