package vn.edu.vnua.department.aclass.request;

import lombok.Data;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetClassListRequest extends GetPageBaseRequest {
    private String keyword;
    private String id;
    private String name;
    private String hrTeacher;
    private String monitor;
    private String facultyId;
}
