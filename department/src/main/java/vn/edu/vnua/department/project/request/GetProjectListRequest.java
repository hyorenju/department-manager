package vn.edu.vnua.department.project.request;

import lombok.Data;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetProjectListRequest extends GetPageBaseRequest {
    private String keyword;
    private String dateBetween;
}
