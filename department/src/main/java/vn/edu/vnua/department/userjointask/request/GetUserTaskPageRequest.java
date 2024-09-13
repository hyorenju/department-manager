package vn.edu.vnua.department.userjointask.request;

import lombok.Data;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetUserTaskPageRequest extends GetPageBaseRequest {
    private String keyword;
    private String userId;
    private Boolean isPrivate;
}
