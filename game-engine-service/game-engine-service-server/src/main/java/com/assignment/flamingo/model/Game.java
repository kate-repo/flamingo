package com.assignment.flamingo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Kate
 * @since 23-Apr-2026
 */
@Entity
@Table(name = "tbl_games")
@Getter
@Setter
public class Game {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Version
    @JsonIgnore
    private int version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus gameStatus = GameStatus.IN_PROGRESS;

    @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST)
    private Set<BoardCell> boardCells = new HashSet<>();

    @Transient
    private Map<BoardPosition, PlayerSymbol> boardMap = new HashMap<>();

    @PostLoad
    public void populateBoardMap() {
        this.boardMap.putAll(this.getBoardCells().stream()
                .collect(Collectors.toMap(BoardCell::getBoardPosition, BoardCell::getPlayerSymbol)));
    }
}
