package com.srm.controller;

import com.srm.dto.ApiResponse;
import com.srm.dto.LoginRequest;
import com.srm.model.Teacher;
import com.srm.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Teacher operations.
 *
 * Base URL: /api/teachers
 *
 * Endpoints:
 *   POST /api/teachers/signup      — Register a new teacher
 *   POST /api/teachers/login       — Authenticate a teacher
 *   GET  /api/teachers             — Get all teachers (for student dashboard subject list)
 *   GET  /api/teachers/{id}        — Get one teacher by ID
 */
@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")   // Allow frontend (any origin) to call this API
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    // ── POST /api/teachers/signup ───────────────────────────
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Teacher>> signup(@RequestBody Teacher teacher) {
        try {
            // Basic validation
            if (teacher.getName()     == null || teacher.getName().isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Name is required."));
            if (teacher.getSubject()  == null || teacher.getSubject().isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Subject is required."));
            if (teacher.getLoginId()  == null || teacher.getLoginId().isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Login ID is required."));
            if (teacher.getPassword() == null || teacher.getPassword().length() < 4)
                return ResponseEntity.badRequest().body(ApiResponse.error("Password must be at least 4 characters."));

            Teacher saved = teacherService.register(
                teacher.getName(),
                teacher.getSubject(),
                teacher.getLoginId(),
                teacher.getPassword()
            );
            // Don't send password back to frontend
            saved.setPassword(null);
            return ResponseEntity.ok(ApiResponse.ok("Account created successfully!", saved));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    // ── POST /api/teachers/login ────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Teacher>> login(@RequestBody LoginRequest req) {
        if (req.getLoginId() == null || req.getPassword() == null)
            return ResponseEntity.badRequest().body(ApiResponse.error("Login ID and password are required."));

        Optional<Teacher> result = teacherService.login(req.getLoginId(), req.getPassword());

        if (result.isEmpty())
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid Login ID or Password."));

        Teacher teacher = result.get();
        teacher.setPassword(null);   // Never send password to frontend
        return ResponseEntity.ok(ApiResponse.ok("Login successful!", teacher));
    }

    // ── GET /api/teachers ───────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<Teacher>>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        // Strip passwords before sending
        teachers.forEach(t -> t.setPassword(null));
        return ResponseEntity.ok(ApiResponse.ok(teachers));
    }

    // ── GET /api/teachers/{id} ──────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Teacher>> getTeacher(@PathVariable Long id) {
        return teacherService.findById(id)
            .map(t -> { t.setPassword(null); return ResponseEntity.ok(ApiResponse.ok(t)); })
            .orElse(ResponseEntity.notFound().build());
    }
}
