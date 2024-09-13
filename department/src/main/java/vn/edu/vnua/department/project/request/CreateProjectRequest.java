package vn.edu.vnua.department.project.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProjectRequest {
    @NotBlank(message = "Tên dự án không được để trống")
    private String name;

    private String description;

    @NotBlank(message = "Ngày bắt đầu không được để trống")
    private String start;

    @NotBlank(message = "Hạn chót không được để trống")
    private String deadline;
}
