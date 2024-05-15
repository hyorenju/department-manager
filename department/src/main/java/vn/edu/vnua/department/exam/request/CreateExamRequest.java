package vn.edu.vnua.department.exam.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.subject.request.CreateSubjectRequest;
import vn.edu.vnua.department.user.entity.UserDTO;
import vn.edu.vnua.department.user.request.CreateUserRequest;

@Data
public class CreateExamRequest {
    private Long id;

    private CreateSubjectRequest subject;

    private CreateMasterDataRequest schoolYear;

    @NotNull(message = "Học kỳ không được để trống")
    private Byte term;

    @NotBlank(message = "Phòng thi không được để trống")
    private String testRoom;

    @NotBlank(message = "Ngày thi không được để trống")
    private String testDay;

    @NotNull(message = "Tiết bắt đầu không được để trống")
    private Integer lessonStart;

    @NotNull(message = "Số tiết không được để trống")
    private Integer lessonsTest;

    @NotBlank(message = "Mã lớp không được để trống")
    private String classId;

    @NotNull(message = "Nhóm không được để trống")
    private Integer examGroup;

    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;

    @NotNull(message = "Tổ thi không được để trống")
    private Integer cluster;

    private String note;
}
