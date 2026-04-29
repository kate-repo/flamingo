package com.assignment.flamingo.repository;

import com.assignment.flamingo.model.Move;
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
public interface MoveRepository extends CrudRepository<Move, UUID> {
}
