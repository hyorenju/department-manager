package vn.edu.vnua.department.user.request;

import lombok.Data;

@Data
public class GetUserSelectionRequest {
    private String facultyId;
    private String departmentId;
}
