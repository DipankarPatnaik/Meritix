package com.srm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Marks entity — links a Student to a Teacher with a marks value.
 *
 * A Teacher can assign marks to any Student, but only for their own subject.
 * Composite unique constraint: one mark per (student, teacher) pair.
 */
@Entity
@Table(
    name = "marks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "teacher_id"})
)
public class Marks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many marks can belong to one student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Many marks can belong to one teacher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @NotNull(message = "Marks value is required")
    @Min(value = 0,   message = "Marks cannot be less than 0")
    @Max(value = 100, message = "Marks cannot exceed 100")
    @Column(nullable = false)
    private Integer marks;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ── Lifecycle ───────────────────────────────────────────
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ── Constructors ────────────────────────────────────────
    public Marks() {}

    public Marks(Student student, Teacher teacher, Integer marks) {
        this.student = student;
        this.teacher = teacher;
        this.marks   = marks;
    }

    // ── Getters & Setters ───────────────────────────────────
    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }

    public Student getStudent()              { return student; }
    public void setStudent(Student student)  { this.student = student; }

    public Teacher getTeacher()              { return teacher; }
    public void setTeacher(Teacher teacher)  { this.teacher = teacher; }

    public Integer getMarks()                { return marks; }
    public void setMarks(Integer marks)      { this.marks = marks; }

    public LocalDateTime getUpdatedAt()      { return updatedAt; }

    @Override
    public String toString() {
        return "Marks{id=" + id + ", student=" + student.getName()
             + ", teacher=" + teacher.getName() + ", marks=" + marks + "}";
    }
}
