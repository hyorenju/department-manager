package vn.edu.vnua.department.exam.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.subject.request.CreateSubjectRequest;
import vn.edu.vnua.department.user.request.CreateUserRequest;

@Data
public class UpdateExamRequest {
    @NotBlank(message = "Phòng thi không được để trống")
    private String testRoom;

    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;

    private CreateMasterDataRequest form;

    @NotNull(message = "Mã đề thi không được để trống")
    private Integer examCode;

    private CreateUserRequest lecturerTeach;

    private CreateUserRequest proctor1;

    private CreateUserRequest proctor2;

    private CreateUserRequest marker1;

    private CreateUserRequest marker2;

    private CreateUserRequest picker;

    private CreateUserRequest printer;

    private CreateUserRequest questionTaker;

    private CreateUserRequest examTaker;

    private CreateUserRequest examGiver;

    private CreateUserRequest pointGiver;

    private String note;
}
