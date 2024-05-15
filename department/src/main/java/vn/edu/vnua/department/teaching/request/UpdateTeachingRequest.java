package vn.edu.vnua.department.teaching.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.subject.request.CreateSubjectRequest;
import vn.edu.vnua.department.user.request.CreateUserRequest;

@Data
public class UpdateTeachingRequest {
    private CreateUserRequest teacher;

    private String componentFile;

    private String summaryFile;

    private String note;
}
