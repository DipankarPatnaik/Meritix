package com.srm.service;

import com.srm.model.Student;
import com.srm.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for Student operations.
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // ── Sign Up ─────────────────────────────────────────────

    /**
     * Register a new student account.
     * @throws IllegalArgumentException if loginId or rollNumber is already taken.
     */
    public Student register(String name, String rollNumber, String loginId, String password) {
        if (studentRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("Login ID '" + loginId + "' is already taken.");
        }
        if (studentRepository.existsByRollNumber(rollNumber)) {
            throw new IllegalArgumentException("Roll number '" + rollNumber + "' is already registered.");
        }
        Student student = new Student(name, rollNumber, loginId, password);
        return studentRepository.save(student);
    }

    // ── Login ───────────────────────────────────────────────

    /**
     * Authenticate a student by loginId and password.
     * @return Student object if credentials match, empty otherwise.
     */
    public Optional<Student> login(String loginId, String password) {
        return studentRepository.findByLoginIdAndPassword(loginId, password);
    }

    // ── Queries ─────────────────────────────────────────────

    /** Get all registered students. */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /** Find a student by their database ID. */
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
}
