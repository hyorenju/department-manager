package vn.edu.vnua.department.user.request;

import lombok.Data;

@Data
public class ExportUserListRequest {
    private String id;
    private String firstName;
    private String lastName;
    private String degreeId;
    private String facultyId;
    private String departmentId;
    private String roleId;
}
