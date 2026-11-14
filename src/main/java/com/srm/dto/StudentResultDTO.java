package com.srm.dto;

/**
 * DTO returned when a student views their results.
 * Contains teacher/subject info plus the marks given.
 */
public class StudentResultDTO {

    private Long   teacherId;
    private String teacherName;
    private String subject;
    private Integer marks;        // null if not yet assigned

    public StudentResultDTO() {}

    public StudentResultDTO(Long teacherId, String teacherName, String subject, Integer marks) {
        this.teacherId   = teacherId;
        this.teacherName = teacherName;
        this.subject     = subject;
        this.marks       = marks;
    }

    public Long getTeacherId()                   { return teacherId; }
    public void setTeacherId(Long teacherId)     { this.teacherId = teacherId; }

    public String getTeacherName()               { return teacherName; }
    public void setTeacherName(String n)         { this.teacherName = n; }

    public String getSubject()                   { return subject; }
    public void setSubject(String subject)       { this.subject = subject; }

    public Integer getMarks()                    { return marks; }
    public void setMarks(Integer marks)          { this.marks = marks; }
}
