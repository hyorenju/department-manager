package vn.edu.vnua.department.aclass.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vn.edu.vnua.department.faculty.request.CreateFacultyRequest;

@Data
public class CreateClassRequest {
    @NotBlank(message = "Mã lớp không được để trống")
    private String id;

    @NotBlank(message = "Tên lớp không được để trống")
    private String name;

    private CreateFacultyRequest faculty;

    private String hrTeacher;
    private String monitor;
    private String monitorPhone;
    private String monitorEmail;
    private String note;
}
