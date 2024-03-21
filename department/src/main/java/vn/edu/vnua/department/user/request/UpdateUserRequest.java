package vn.edu.vnua.department.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vn.edu.vnua.department.department.request.CreateDepartmentRequest;
import vn.edu.vnua.department.domain.validation.EmailAnnotation;
import vn.edu.vnua.department.domain.validation.PhoneNumber;
import vn.edu.vnua.department.role.request.CreateRoleRequest;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "Họ đệm giảng viên không được để trống")
    private String firstName;

    @NotBlank(message = "Tên giảng viên không được để trống")
    private String lastName;

    private String degree;

    @NotBlank(message = "Email của giảng viên không được để trống")
    @EmailAnnotation
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @PhoneNumber
    private String phoneNumber;

    private String password;

    private CreateRoleRequest role;

    private String note;
}
