package vn.edu.vnua.department.subject.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.sql.Timestamp;

@Data
public class CreateSubjectRequest {
    @NotBlank(message = "Mã môn học không được để trống")
    private String id;

    @NotBlank(message = "Tên môn học không được để trống")
    private String name;

    @NotNull(message = "Số tín chỉ không được để trống")
    private Integer credits;

    private String outline;
    private String lecture;

    private String note;
}
