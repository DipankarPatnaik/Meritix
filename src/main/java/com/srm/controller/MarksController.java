package com.srm.controller;

import com.srm.dto.ApiResponse;
import com.srm.dto.MarksRequest;
import com.srm.model.Marks;
import com.srm.model.Student;
import com.srm.model.Teacher;
import com.srm.service.MarksService;
import com.srm.service.StudentService;
import com.srm.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for Marks operations.
 *
 * Base URL: /api/marks
 *
 * Endpoints:
 *   POST /api/marks/assign/{teacherId}         — Teacher assigns/updates marks for a student
 *   GET  /api/marks/teacher/{teacherId}        — Get all marks assigned by a teacher (with student info)
 *   GET  /api/marks/student/{studentId}/teacher/{teacherId}  — Get one specific mark entry
 */
@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = "*")
public class MarksController {

    @Autowired
    private MarksService marksService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    // ── POST /api/marks/assign/{teacherId} ──────────────────
    /**
     * A teacher assigns marks to a student.
     * teacherId comes from the URL (the logged-in teacher's ID).
     * Body: { "studentId": 1, "marks": 85 }
     */
    @PostMapping("/assign/{teacherId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> assignMarks(
            @PathVariable Long teacherId,
            @RequestBody MarksRequest req) {

        // Validate marks value
        if (req.getMarks() == null || req.getMarks() < 0 || req.getMarks() > 100)
            return ResponseEntity.badRequest().body(ApiResponse.error("Marks must be between 0 and 100."));

        // Find teacher
        Optional<Teacher> teacher = teacherService.findById(teacherId);
        if (teacher.isEmpty())
            return ResponseEntity.badRequest().body(ApiResponse.error("Teacher not found."));

        // Find student
        Optional<Student> student = studentService.findById(req.getStudentId());
        if (student.isEmpty())
            return ResponseEntity.badRequest().body(ApiResponse.error("Student not found."));

        // Assign marks (upsert)
        Marks saved = marksService.assignMarks(teacher.get(), student.get(), req.getMarks());

        // Build response map
        Map<String, Object> response = new HashMap<>();
        response.put("marksId",     saved.getId());
        response.put("studentId",   saved.getStudent().getId());
        response.put("teacherId",   saved.getTeacher().getId());
        response.put("marks",       saved.getMarks());
        response.put("subject",     saved.getTeacher().getSubject());
        response.put("updatedAt",   saved.getUpdatedAt().toString());

        return ResponseEntity.ok(ApiResponse.ok("Marks saved successfully!", response));
    }

    // ── GET /api/marks/teacher/{teacherId} ──────────────────
    /**
     * Returns all marks entries for a teacher, with student details.
     * Used in Teacher Dashboard to show which students have been graded.
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMarksByTeacher(
            @PathVariable Long teacherId) {

        Optional<Teacher> teacher = teacherService.findById(teacherId);
        if (teacher.isEmpty())
            return ResponseEntity.notFound().build();

        List<Marks> marksList = marksService.getMarksByTeacher(teacher.get());

        List<Map<String, Object>> result = marksList.stream().map(m -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("marksId",    m.getId());
            entry.put("studentId",  m.getStudent().getId());
            entry.put("studentName",m.getStudent().getName());
            entry.put("rollNumber", m.getStudent().getRollNumber());
            entry.put("marks",      m.getMarks());
            entry.put("updatedAt",  m.getUpdatedAt().toString());
            return entry;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    // ── GET /api/marks/student/{studentId}/teacher/{teacherId} ──
    /**
     * Get the specific mark a teacher gave to a student.
     */
    @GetMapping("/student/{studentId}/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOneMark(
            @PathVariable Long studentId,
            @PathVariable Long teacherId) {

        Optional<Student> student = studentService.findById(studentId);
        Optional<Teacher> teacher = teacherService.findById(teacherId);

        if (student.isEmpty() || teacher.isEmpty())
            return ResponseEntity.notFound().build();

        return marksService.getMark(student.get(), teacher.get())
            .map(m -> {
                Map<String, Object> entry = new HashMap<>();
                entry.put("marks",     m.getMarks());
                entry.put("subject",   m.getTeacher().getSubject());
                entry.put("updatedAt", m.getUpdatedAt().toString());
                return ResponseEntity.ok(ApiResponse.ok(entry));
            })
            .orElse(ResponseEntity.ok(ApiResponse.ok("No marks assigned yet", null)));
    }

    // ── DELETE /api/marks/teacher/{teacherId}/student/{studentId} ──
    @DeleteMapping("/teacher/{teacherId}/student/{studentId}")
    public ResponseEntity<ApiResponse<Void>> deleteMark(
            @PathVariable Long teacherId,
            @PathVariable Long studentId) {

        boolean deleted = marksService.deleteMark(teacherId, studentId);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.ok("Mark entry deleted successfully", null));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Mark entry not found to delete."));
    }
}
