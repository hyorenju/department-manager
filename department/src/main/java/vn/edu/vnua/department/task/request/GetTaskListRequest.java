package vn.edu.vnua.department.task.request;

import lombok.Data;
import vn.edu.vnua.department.request.GetPageBaseRequest;

@Data
public class GetTaskListRequest {
    private String keyword;
    private String dateBetween;
    private Long projectId;
}
