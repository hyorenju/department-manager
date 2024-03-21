package vn.edu.vnua.department.user.request;

import lombok.Data;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetUserListRequest extends GetPageBaseRequest {
    private String keyword;
    private String degree;
    private String facultyId;
    private String departmentId;
}
