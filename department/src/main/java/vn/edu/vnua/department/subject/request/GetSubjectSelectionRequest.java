package vn.edu.vnua.department.subject.request;

import lombok.Data;

@Data
public class GetSubjectSelectionRequest {
    private String facultyId;
    private String departmentId;
}
