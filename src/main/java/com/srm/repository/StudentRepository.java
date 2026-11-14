package com.srm.repository;

import com.srm.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for Student entity.
 * Spring Data JPA auto-generates all CRUD operations.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find student by loginId (for login authentication)
    Optional<Student> findByLoginId(String loginId);

    // Find student by loginId AND password (login check)
    Optional<Student> findByLoginIdAndPassword(String loginId, String password);

    // Check if loginId already exists (sign-up validation)
    boolean existsByLoginId(String loginId);

    // Check if rollNumber already exists (sign-up validation)
    boolean existsByRollNumber(String rollNumber);
}
