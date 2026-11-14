package com.srm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Teacher entity — represents a teacher account.
 * Each teacher teaches exactly ONE subject.
 * Their loginId + password are used to authenticate.
 */
@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Subject is required")
    @Column(nullable = false)
    private String subject;

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
    public Teacher() {}

    public Teacher(String name, String subject, String loginId, String password) {
        this.name     = name;
        this.subject  = subject;
        this.loginId  = loginId;
        this.password = password;
    }

    // ── Getters & Setters ───────────────────────────────────
    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public String getName()                    { return name; }
    public void setName(String name)           { this.name = name; }

    public String getSubject()                 { return subject; }
    public void setSubject(String subject)     { this.subject = subject; }

    public String getLoginId()                 { return loginId; }
    public void setLoginId(String loginId)     { this.loginId = loginId; }

    public String getPassword()                { return password; }
    public void setPassword(String password)   { this.password = password; }

    public LocalDateTime getCreatedAt()        { return createdAt; }

    @Override
    public String toString() {
        return "Teacher{id=" + id + ", name='" + name + "', subject='" + subject + "', loginId='" + loginId + "'}";
    }
}
