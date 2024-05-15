package vn.edu.vnua.department.exam.request;

import lombok.Data;
import vn.edu.vnua.department.user.request.CreateUserRequest;

@Data
public class UpdateProctorExamRequest {
    private CreateUserRequest proctor1;
    private CreateUserRequest proctor2;
}
