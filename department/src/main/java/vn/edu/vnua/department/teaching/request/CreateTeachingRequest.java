package vn.edu.vnua.department.teaching.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.subject.entity.SubjectDTO;
import vn.edu.vnua.department.subject.request.CreateSubjectRequest;
import vn.edu.vnua.department.user.entity.UserDTO;
import vn.edu.vnua.department.user.request.CreateUserRequest;

import java.sql.Timestamp;

@Data
public class CreateTeachingRequest {
    private Long id;

    private CreateSubjectRequest subject;

    private CreateUserRequest teacher;

    @NotBlank(message = "Vui lòng điền các mã lớp")
    private String classId;

    @NotNull(message = "Nhóm không được để trống")
    private Integer teachingGroup;

    private CreateMasterDataRequest schoolYear;

    @NotNull(message = "Học kỳ không được để trống")
    private Byte term;

    private String componentFile;

    private String summaryFile;

    private String note;
}
