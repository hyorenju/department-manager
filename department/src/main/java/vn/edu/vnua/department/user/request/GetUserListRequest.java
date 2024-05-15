package vn.edu.vnua.department.user.request;

import lombok.Data;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetUserListRequest extends GetPageBaseRequest {
    private String id;
    private String firstName;
    private String lastName;
    private String degreeId;
    private String facultyId;
    private String departmentId;
    private String roleId;
}
