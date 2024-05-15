package vn.edu.vnua.department.intern.request;

import lombok.Data;

@Data
public class ExportInternListRequest {
    private String name;
    private Long schoolYear;
    private Byte term;
    private String instructorId;
    private Long typeId;
    private String status;
    private String facultyId;
    private String departmentId;
}
