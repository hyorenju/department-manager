package vn.edu.vnua.department.exam.request;

import lombok.Data;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetExamListRequest extends GetPageBaseRequest {
    private Boolean isAll;
    private String facultyId;
    private String departmentId;
    private String subjectId;
    private String subjectName;
    private Long schoolYear;
    private Byte term;
    private Long formId;
    private String testDay;
    private String proctorId;
    private String classId;
    private Integer examGroup;
    private Integer cluster;
    private Integer quantity;
    private String testRoom;
    private Integer lessonStart;
    private Integer lessonsTest;
    private String examCode;
    private String lecturerTeachId;
    private String markerId;
    private String pickerId;
    private String printerId;
    private String questionTakerId;
    private String examTakerId;
    private String examGiverId;
    private String pointGiverId;
}
