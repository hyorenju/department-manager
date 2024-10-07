package vn.edu.vnua.department.subject.request;

import lombok.Data;

@Data
public class ExportSubjectListRequest {
    private String keyword;
    private String id;
    private String name;
    private String facultyId;
    private String departmentId;
}
