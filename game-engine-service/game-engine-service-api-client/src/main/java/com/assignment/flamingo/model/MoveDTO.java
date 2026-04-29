package com.assignment.flamingo.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * @author Kate
 * @since 23-Apr-2026
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveDTO {

    @NotEmpty
    private BoardPosition boardPosition;

    @NotEmpty
    private PlayerSymbol playerSymbol;

    private ZonedDateTime creationTimestamp;
}
