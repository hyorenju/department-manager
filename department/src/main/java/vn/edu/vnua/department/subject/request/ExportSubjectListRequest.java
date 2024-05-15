package vn.edu.vnua.department.subject.request;

import lombok.Data;

@Data
public class ExportSubjectListRequest {
    private String keyword;
    private String facultyId;
    private String departmentId;
}
