package vn.edu.vnua.department.userjointask.request;

import lombok.Data;
import vn.edu.vnua.department.user.request.CreateUserRequest;

import java.util.List;

@Data
public class CreateUserTaskRequest {
    private List<String> userIds;
}
