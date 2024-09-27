package vn.edu.vnua.department.intern.request;

import lombok.Data;

@Data
public class ExportInternListRequest {
    private Boolean isAll;
    private String studentId;
    private String studentName;
    private String classId;
    private String company;
    private Long schoolYear;
    private Byte term;
    private String name;
    private String instructorId;
    private Long typeId;
    private String status;
    private String facultyId;
    private String departmentId;
}
