package vn.edu.vnua.department.internship.request;

import lombok.Data;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetInternshipListRequest extends GetPageBaseRequest {
    private Boolean isAll;
    private String studentId;
    private String studentName;
    private String classId;
    private String company;
    private Long schoolYear;
    private Byte term;
    private String name;
    private String instructorId;
    private Long typeId;
    private String status;
    private String facultyId;
    private String departmentId;
    private String note;
}
