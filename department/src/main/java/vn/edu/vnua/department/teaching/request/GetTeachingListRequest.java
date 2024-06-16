package vn.edu.vnua.department.teaching.request;

import lombok.Data;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetTeachingListRequest extends GetPageBaseRequest {
    private Boolean isAll;
    private Long schoolYear;
    private Byte term;
    private String facultyId;
    private String departmentId;
    private String subjectId;
    private String subjectName;
    private String teacherId;
    private String classId;
    private String status;
}
