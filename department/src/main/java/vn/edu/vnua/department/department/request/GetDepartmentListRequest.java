package vn.edu.vnua.department.department.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
@RequiredArgsConstructor
public class GetDepartmentListRequest extends GetPageBaseRequest {
    private String keyword;
    private String facultyId;
}
