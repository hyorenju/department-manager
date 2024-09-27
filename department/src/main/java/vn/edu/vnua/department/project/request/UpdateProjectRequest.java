package vn.edu.vnua.department.project.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProjectRequest {
    @NotBlank(message = "Tên dự án không được để trống")
    private String name;

    private String description;

    @NotBlank(message = "Ngày bắt đầu không được để trống")
    private String start;

    @NotBlank(message = "Ngày kết thúc không được để trống")
    private String deadline;
}
