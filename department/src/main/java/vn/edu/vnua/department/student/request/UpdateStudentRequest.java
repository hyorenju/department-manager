package vn.edu.vnua.department.student.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import vn.edu.vnua.department.intern.request.CreateInternRequest;

@Data
public class UpdateStudentRequest {
    @NotBlank(message = "Mã sinh viên không được để trống")
    @Size(max = 100, message = "Mã sinh viên quá dài")
    @Pattern(regexp = "^[0-9]+", message = "Mã sinh viên phải là một chuỗi ký tự số")
    private String studentId;

    @NotBlank(message = "Tên sinh viên không được để trống")
    @Size(max = 200, message = "Tên sinh viên quá dài")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Tên sinh viên không được chứa ký tự đặc biệt")
    private String name;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotBlank(message = "Nơi thực tập không được để trống")
    private String company;

    private CreateInternRequest intern;

    private String note;
}
