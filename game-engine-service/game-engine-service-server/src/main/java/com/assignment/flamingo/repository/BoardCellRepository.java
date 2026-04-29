package com.assignment.flamingo.repository;

import com.assignment.flamingo.model.BoardCell;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * CRUD repository.
 *
 * @author Kate
 * @since 23-Apr-2026
 */
@Repository
public interface BoardCellRepository extends CrudRepository<BoardCell, UUID> {
}
