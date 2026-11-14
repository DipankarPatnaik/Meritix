package com.srm.service;

import com.srm.dto.StudentResultDTO;
import com.srm.model.Marks;
import com.srm.model.Student;
import com.srm.model.Teacher;
import com.srm.repository.MarksRepository;
import com.srm.repository.StudentRepository;
import com.srm.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business logic for Marks operations.
 */
@Service
public class MarksService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    // ── Assign / Update marks ───────────────────────────────

    /**
     * Assign or update marks for a student by a specific teacher.
     * Uses "upsert" logic — if a marks record already exists for
     * this student+teacher pair, it updates; otherwise inserts.
     *
     * @param teacher   the teacher assigning marks (their subject only)
     * @param student   the student receiving marks
     * @param marksVal  the marks value (0–100)
     */
    public Marks assignMarks(Teacher teacher, Student student, int marksVal) {
        // Check if marks already exist for this student-teacher pair
        Optional<Marks> existing = marksRepository.findByStudentAndTeacher(student, teacher);

        Marks marks;
        if (existing.isPresent()) {
            // Update existing record
            marks = existing.get();
            marks.setMarks(marksVal);
        } else {
            // Create new record
            marks = new Marks(student, teacher, marksVal);
        }
        return marksRepository.save(marks);
    }

    // ── Get marks for student dashboard ─────────────────────

    /**
     * Returns a list of all subjects (one per teacher) with the marks
     * this student received. If no marks assigned yet, marks = null.
     *
     * This is what the Student Dashboard displays.
     */
    public List<StudentResultDTO> getStudentResults(Student student) {
        // Get all teacher-mark pairs this student has
        List<Marks> studentMarks = marksRepository.findByStudent(student);

        // Get all teachers in the system
        List<Teacher> allTeachers = teacherRepository.findAll();

        // For each teacher, check if marks exist for this student
        return allTeachers.stream().map(teacher -> {
            Integer marksVal = studentMarks.stream()
                .filter(m -> m.getTeacher().getId().equals(teacher.getId()))
                .map(Marks::getMarks)
                .findFirst()
                .orElse(null);   // null = "Not yet assigned"

            return new StudentResultDTO(
                teacher.getId(),
                teacher.getName(),
                teacher.getSubject(),
                marksVal
            );
        }).collect(Collectors.toList());
    }

    // ── Get all marks by a teacher ──────────────────────────

    /**
     * Returns all marks assigned by a specific teacher.
     * Used in the Teacher Dashboard to show which students have been graded.
     */
    public List<Marks> getMarksByTeacher(Teacher teacher) {
        return marksRepository.findByTeacher(teacher);
    }

    // ── Get a single mark ───────────────────────────────────

    /**
     * Get the mark a specific teacher gave to a specific student.
     */
    public Optional<Marks> getMark(Student student, Teacher teacher) {
        return marksRepository.findByStudentAndTeacher(student, teacher);
    }

    // ── Delete a mark ───────────────────────────────────────

    /**
     * Delete or reset the mark a teacher gave to a student.
     */
    public boolean deleteMark(Long teacherId, Long studentId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        Optional<Student> student = studentRepository.findById(studentId);
        if (teacher.isPresent() && student.isPresent()) {
            Optional<Marks> mark = marksRepository.findByStudentAndTeacher(student.get(), teacher.get());
            if (mark.isPresent()) {
                marksRepository.delete(mark.get());
                return true;
            }
        }
        return false;
    }
}
