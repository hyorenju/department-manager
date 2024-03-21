package vn.edu.vnua.department.faculty.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateFacultyRequest {
    @NotBlank(message = "Tên khoa không được để trống")
    private String name;
}
