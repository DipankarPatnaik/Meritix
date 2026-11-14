package com.srm.repository;

import com.srm.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for Teacher entity.
 * Spring Data JPA auto-generates all CRUD operations.
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Find a teacher by their loginId (for login authentication)
    Optional<Teacher> findByLoginId(String loginId);

    // Find a teacher by both loginId AND password (login check)
    Optional<Teacher> findByLoginIdAndPassword(String loginId, String password);

    // Check if a loginId already exists (for sign-up validation)
    boolean existsByLoginId(String loginId);
}
