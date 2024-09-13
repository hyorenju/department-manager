package vn.edu.vnua.department.task.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTaskRequest {
    @NotBlank(message = "Tên dự án không được để trống")
    private String name;

    private String description;

    @NotBlank(message = "Ngày bắt đầu không được để trống")
    private String start;

    @NotBlank(message = "Hạn chót không được để trống")
    private String deadline;

    private List<String> userIds;
}
