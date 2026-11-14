package com.srm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Student entity — represents a student account.
 * Each student has a unique loginId and rollNumber.
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Roll number is required")
    @Column(nullable = false, unique = true)
    private String rollNumber;

    @NotBlank(message = "Login ID is required")
    @Column(nullable = false, unique = true)
    private String loginId;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Lifecycle ───────────────────────────────────────────
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ── Constructors ────────────────────────────────────────
    public Student() {}

    public Student(String name, String rollNumber, String loginId, String password) {
        this.name       = name;
        this.rollNumber = rollNumber;
        this.loginId    = loginId;
        this.password   = password;
    }

    // ── Getters & Setters ───────────────────────────────────
    public Long getId()                           { return id; }
    public void setId(Long id)                    { this.id = id; }

    public String getName()                       { return name; }
    public void setName(String name)              { this.name = name; }

    public String getRollNumber()                 { return rollNumber; }
    public void setRollNumber(String rollNumber)  { this.rollNumber = rollNumber; }

    public String getLoginId()                    { return loginId; }
    public void setLoginId(String loginId)        { this.loginId = loginId; }

    public String getPassword()                   { return password; }
    public void setPassword(String password)      { this.password = password; }

    public LocalDateTime getCreatedAt()           { return createdAt; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', rollNumber='" + rollNumber + "', loginId='" + loginId + "'}";
    }
}
