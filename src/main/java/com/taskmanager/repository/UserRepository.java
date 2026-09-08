package com.taskmanager.repository;

import com.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Spring Data JPA repository ≈ a thin DAO / SQLAlchemy query helper.
 *
 * Extending JpaRepository gives you save(), findById(), findAll(), delete() for free.
 * Method names like findByUsername are turned into SQL automatically
 * (query derivation — no @Query needed for simple lookups).
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
