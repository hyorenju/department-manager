package vn.edu.vnua.department.teaching.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.subject.request.CreateSubjectRequest;
import vn.edu.vnua.department.user.request.CreateUserRequest;

@Data
public class UpdateTeachingRequest {
    @NotNull(message = "Nhóm không được để trống")
    @Pattern(regexp = "^\\d+$", message = "Nhóm môn học phải là một chuỗi ký tự số")
    private String teachingGroup;

    @NotNull(message = "Học kỳ không được để trống")
    private Byte term;

    private CreateMasterDataRequest schoolYear;

    @NotBlank(message = "Vui lòng điền các mã lớp")
    private String classId;

    private CreateUserRequest teacher;

    private String componentFile;

    private String summaryFile;

    private String note;
}
