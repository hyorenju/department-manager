package vn.edu.vnua.department.teaching.request;

import lombok.Data;

@Data
public class ExportTeachingRequest {
    private Long schoolYear;
    private Byte term;
    private String facultyId;
    private String departmentId;
    private String subjectName;
    private String teacherId;
    private String classId;
    private String status;
}
