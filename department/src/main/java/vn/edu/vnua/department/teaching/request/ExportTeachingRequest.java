package vn.edu.vnua.department.teaching.request;

import lombok.Data;

@Data
public class ExportTeachingRequest {
    private Boolean isAll;
    private Long schoolYear;
    private Byte term;
    private String facultyId;
    private String departmentId;
    private String subjectId;
    private String subjectName;
    private String teacherId;
    private String classId;
    private String status;
    private String note;
}
