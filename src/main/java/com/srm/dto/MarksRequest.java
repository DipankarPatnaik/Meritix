package com.srm.dto;

/**
 * DTO for assigning / updating marks.
 * Teacher sends { studentId, marks } to the marks endpoint.
 */
public class MarksRequest {

    private Long    studentId;
    private Integer marks;

    public MarksRequest() {}

    public MarksRequest(Long studentId, Integer marks) {
        this.studentId = studentId;
        this.marks     = marks;
    }

    public Long getStudentId()                { return studentId; }
    public void setStudentId(Long studentId)  { this.studentId = studentId; }

    public Integer getMarks()                 { return marks; }
    public void setMarks(Integer marks)       { this.marks = marks; }
}
