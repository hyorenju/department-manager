package vn.edu.vnua.department.aclass.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.domain.validation.EmailAnnotation;

import vn.edu.vnua.department.faculty.request.CreateFacultyRequest;

@Data
public class UpdateClassRequest {
    @NotBlank(message = "Tên lớp không được để trống")
    private String name;

    private CreateFacultyRequest faculty;

    @NotBlank(message = "Giáo viên chủ nhiệm không được để trống")
    private String hrTeacher;

    @NotBlank(message = "Lớp trưởng không được để trống")
    private String monitor;

    @NotBlank(message = "Số điện thoại lớp trưởng không được để trống")
    @Pattern(regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$", message = "Số điện thoại không hợp lệ")
    private String monitorPhone;

    private String monitorEmail;

    private String note;
}
