package vn.edu.vnua.department.aclass.request;

import lombok.Data;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetClassListRequest extends GetPageBaseRequest {
    private String keyword;
    private String facultyId;
}
