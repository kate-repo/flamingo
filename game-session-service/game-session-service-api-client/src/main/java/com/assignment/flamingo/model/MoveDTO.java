package com.assignment.flamingo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * @author Kate
 * @since 26-Apr-2026
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveDTO {

    private BoardPosition boardPosition;

    private PlayerSymbol playerSymbol;

    private ZonedDateTime creationTimestamp;
}
