package vn.edu.vnua.department.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.department.request.CreateDepartmentRequest;
import vn.edu.vnua.department.domain.validation.EmailAnnotation;

import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.role.request.CreateRoleRequest;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Mã giảng viên không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*$", message = "Mã người dùng không được chứa ký tự đặc biệt hoặc khoảng trắng")
    private String id;

    @NotBlank(message = "Họ đệm giảng viên không được để trống")
    @Pattern(regexp = "^[\\p{L}]*$")
    private String firstName;

    @NotBlank(message = "Tên giảng viên không được để trống")
    @Pattern(regexp = "^[\\p{L}]*$")
    private String lastName;

    private CreateMasterDataRequest degree;

    @NotBlank(message = "Email của giảng viên không được để trống")
    @EmailAnnotation
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    private CreateDepartmentRequest department;

    private String password;

    private CreateRoleRequest role;

    private String note;
}
