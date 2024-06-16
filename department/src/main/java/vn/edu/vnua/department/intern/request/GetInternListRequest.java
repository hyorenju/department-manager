package vn.edu.vnua.department.intern.request;

import lombok.Data;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.request.GetPageBaseRequest;
import vn.edu.vnua.department.user.entity.User;

@Data
public class GetInternListRequest extends GetPageBaseRequest {
    private Boolean isAll;
    private String name;
    private Long schoolYear;
    private Byte term;
    private String instructorId;
    private Long typeId;
    private String status;
    private String facultyId;
    private String departmentId;
}
