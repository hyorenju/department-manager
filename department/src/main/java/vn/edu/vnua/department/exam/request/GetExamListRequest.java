package vn.edu.vnua.department.exam.request;

import lombok.Data;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetExamListRequest extends GetPageBaseRequest {
    private String facultyId;
    private String departmentId;
    private String subjectName;
    private Long schoolYear;
    private Byte term;
    private Long formId;
    private String testDay;
    private String proctor1Id;
    private String proctor2Id;
    private String classId;
}
