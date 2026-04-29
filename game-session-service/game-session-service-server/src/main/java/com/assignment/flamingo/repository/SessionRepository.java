package com.assignment.flamingo.repository;

import com.assignment.flamingo.model.Session;
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
public interface SessionRepository extends CrudRepository<Session, UUID> {

}
