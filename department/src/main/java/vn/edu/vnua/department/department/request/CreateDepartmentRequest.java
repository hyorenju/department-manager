package vn.edu.vnua.department.department.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vn.edu.vnua.department.faculty.request.CreateFacultyRequest;

@Data
public class CreateDepartmentRequest {
    @NotBlank(message = "Mã bộ môn không được để trống")
    private String id;

    @NotBlank(message = "Tên bộ môn không được để trống")
    private String name;

    private CreateFacultyRequest faculty;
}
