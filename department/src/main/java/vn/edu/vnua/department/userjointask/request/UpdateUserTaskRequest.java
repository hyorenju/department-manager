package vn.edu.vnua.department.userjointask.request;

import lombok.Data;
import vn.edu.vnua.department.userjointask.entity.UserTask;

import java.util.List;

@Data
public class UpdateUserTaskRequest {
    private Long taskId;
    private List<String> userIds;
}
