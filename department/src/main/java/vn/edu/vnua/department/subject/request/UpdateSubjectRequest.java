package vn.edu.vnua.department.subject.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSubjectRequest {
    @NotBlank(message = "Tên môn học không được để trống")
    private String name;

    @NotNull(message = "Số tín chỉ không được để trống")
    private Integer credits;

    private String outline;

    private String lecture;

    private String note;
}
