package vn.edu.vnua.department.internship.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.user.request.CreateUserRequest;

@Data
public class CreateInternshipRequest {
    private CreateMasterDataRequest schoolYear;

    @NotNull(message = "Học kỳ không được để trống")
    private Byte term;

    @NotBlank(message = "Mã sv không được để trống")
    private String studentId;

    @NotBlank(message = "Tên sv không được để trống")
    private String studentName;

    @NotBlank(message = "Lớp không được để trống")
    private String classId;

    private String phone;

    private String company;

    @NotBlank(message = "Tên đề tài không được để trống")
    private String name;

    private CreateMasterDataRequest type;

    private CreateUserRequest instructor;

    private String outlineFile;
    private String progressFile;
    private String finalFile;

    private String note;
}
