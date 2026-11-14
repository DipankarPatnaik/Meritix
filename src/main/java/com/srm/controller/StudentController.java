package com.srm.controller;

import com.srm.dto.ApiResponse;
import com.srm.dto.LoginRequest;
import com.srm.dto.StudentResultDTO;
import com.srm.model.Student;
import com.srm.service.MarksService;
import com.srm.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Student operations.
 *
 * Base URL: /api/students
 *
 * Endpoints:
 *   POST /api/students/signup          — Register a new student
 *   POST /api/students/login           — Authenticate a student
 *   GET  /api/students                 — Get all students (teacher dashboard)
 *   GET  /api/students/{id}            — Get one student by ID
 *   GET  /api/students/{id}/results    — Get this student's marks for all subjects
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private MarksService marksService;

    // ── POST /api/students/signup ───────────────────────────
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Student>> signup(@RequestBody Student student) {
        try {
            if (student.getName()       == null || student.getName().isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Name is required."));
            if (student.getRollNumber() == null || student.getRollNumber().isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Roll number is required."));
            if (student.getLoginId()    == null || student.getLoginId().isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Login ID is required."));
            if (student.getPassword()   == null || student.getPassword().length() < 4)
                return ResponseEntity.badRequest().body(ApiResponse.error("Password must be at least 4 characters."));

            Student saved = studentService.register(
                student.getName(),
                student.getRollNumber(),
                student.getLoginId(),
                student.getPassword()
            );
            saved.setPassword(null);
            return ResponseEntity.ok(ApiResponse.ok("Account created successfully!", saved));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    // ── POST /api/students/login ────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Student>> login(@RequestBody LoginRequest req) {
        if (req.getLoginId() == null || req.getPassword() == null)
            return ResponseEntity.badRequest().body(ApiResponse.error("Login ID and password are required."));

        Optional<Student> result = studentService.login(req.getLoginId(), req.getPassword());

        if (result.isEmpty())
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid Login ID or Password."));

        Student student = result.get();
        student.setPassword(null);
        return ResponseEntity.ok(ApiResponse.ok("Login successful!", student));
    }

    // ── GET /api/students ───────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        students.forEach(s -> s.setPassword(null));
        return ResponseEntity.ok(ApiResponse.ok(students));
    }

    // ── GET /api/students/{id} ──────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudent(@PathVariable Long id) {
        return studentService.findById(id)
            .map(s -> { s.setPassword(null); return ResponseEntity.ok(ApiResponse.ok(s)); })
            .orElse(ResponseEntity.notFound().build());
    }

    // ── GET /api/students/{id}/results ─────────────────────
    /**
     * Returns all subjects (one per teacher) with the marks this student received.
     * Marks is null for subjects where teacher hasn't assigned yet.
     */
    @GetMapping("/{id}/results")
    public ResponseEntity<ApiResponse<List<StudentResultDTO>>> getResults(@PathVariable Long id) {
        Optional<Student> student = studentService.findById(id);
        if (student.isEmpty())
            return ResponseEntity.notFound().build();

        List<StudentResultDTO> results = marksService.getStudentResults(student.get());
        return ResponseEntity.ok(ApiResponse.ok(results));
    }
}
