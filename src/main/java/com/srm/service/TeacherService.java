package com.srm.service;

import com.srm.model.Teacher;
import com.srm.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for Teacher operations.
 */
@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    // ── Sign Up ─────────────────────────────────────────────

    /**
     * Register a new teacher account.
     * @throws IllegalArgumentException if loginId is already taken.
     */
    public Teacher register(String name, String subject, String loginId, String password) {
        if (teacherRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("Login ID '" + loginId + "' is already taken.");
        }
        Teacher teacher = new Teacher(name, subject, loginId, password);
        return teacherRepository.save(teacher);
    }

    // ── Login ───────────────────────────────────────────────

    /**
     * Authenticate a teacher by loginId and password.
     * @return Teacher object if credentials match, empty otherwise.
     */
    public Optional<Teacher> login(String loginId, String password) {
        return teacherRepository.findByLoginIdAndPassword(loginId, password);
    }

    // ── Queries ─────────────────────────────────────────────

    /** Get all registered teachers. */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /** Find a teacher by their database ID. */
    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }
}
