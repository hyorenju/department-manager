package vn.edu.vnua.department.exam.request;

import lombok.Data;

@Data
public class ExportExamRequest {
    private String facultyId;
    private String departmentId;
    private String subjectName;
    private Long schoolYear;
    private Byte term;
    private Long formId;
    private String testDay;
    private String proctor1Id;
    private String proctor2Id;
    private String classId;
}
