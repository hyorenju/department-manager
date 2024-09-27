package vn.edu.vnua.department.task.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.edu.vnua.department.user.entity.User;

import java.util.List;

@Data
public class CreateTaskRequest {
    @NotBlank(message = "Tên dự án không được để trống")
    private String name;

    private String description;

    @NotBlank(message = "Ngày bắt đầu không được để trống")
    private String start;

    @NotBlank(message = "Ngày kết thúc không được để trống")
    private String deadline;

    @NotNull(message = "Mã dự án không được để trống")
    private Long projectId;

    private List<String> userIds;
}
