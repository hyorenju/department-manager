package vn.edu.vnua.department.subject.request;

import lombok.Data;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.request.GetPageBaseRequest;
@Data
public class GetSubjectListRequest extends GetPageBaseRequest {
    private String keyword;
    private String id;
    private String name;
    private String facultyId;
    private String departmentId;
}
