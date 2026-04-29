package com.assignment.flamingo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;


/**
 * @author Kate
 * @since 23-Apr-2026
 */
@Entity
@Table(name = "tbl_moves")
@Getter
@Setter
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(updatable = false, nullable = false)
    private ZonedDateTime creationTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false, nullable = false)
    private BoardPosition boardPosition;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false, nullable = false)
    private PlayerSymbol playerSymbol;

    @ManyToOne(optional = false)
    @JsonIgnore
    private Session session;
}
