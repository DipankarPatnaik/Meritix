package com.srm.repository;

import com.srm.model.Marks;
import com.srm.model.Student;
import com.srm.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Marks entity.
 */
@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {

    // Get all marks for a specific student (for student dashboard)
    List<Marks> findByStudent(Student student);

    // Get all marks assigned by a specific teacher (for teacher dashboard)
    List<Marks> findByTeacher(Teacher teacher);

    // Get a specific mark entry for a student-teacher pair (upsert logic)
    Optional<Marks> findByStudentAndTeacher(Student student, Teacher teacher);
}
