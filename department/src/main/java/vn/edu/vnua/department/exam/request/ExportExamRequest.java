package vn.edu.vnua.department.exam.request;

import lombok.Data;

@Data
public class ExportExamRequest {
    private Boolean isAll;
    private String facultyId;
    private String departmentId;
    private String subjectName;
    private Long schoolYear;
    private Byte term;
    private Long formId;
    private String testDay;
    private String proctorId;
    private String classId;
}
