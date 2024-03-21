package vn.edu.vnua.department.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.department.request.CreateDepartmentRequest;
import vn.edu.vnua.department.domain.validation.EmailAnnotation;
import vn.edu.vnua.department.domain.validation.PhoneNumber;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Mã giảng viên không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*$", message = "Mã người dùng không được chứa ký tự đặc biệt hoặc khoảng trắng")
    private String id;

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

    private CreateDepartmentRequest department;

    private String password;

//    @NotBlank(message = "Vui lòng chọn vai trò")
//    private String roleId;

    private String note;
}
