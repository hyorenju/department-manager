package vn.edu.vnua.department.user.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotNull(message = "Vui lòng nhập mật khẩu hiện tại")
    private String currentPassword;

    @NotNull(message = "Vui lòng nhập mật khẩu mới")
    private String newPassword;

    @NotNull(message = "Vui lòng xác nhận mật khẩu mới")
    private String confirmPassword;
}