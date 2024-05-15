package vn.edu.vnua.department.intern.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.user.request.CreateUserRequest;

@Data
public class CreateInternRequest {
    private Long id;

    @NotBlank(message = "Tên đề tài không được để trống")
    private String name;

    private CreateMasterDataRequest type;

    private CreateMasterDataRequest schoolYear;

    @NotNull(message = "Học kỳ không được để trống")
    private Byte term;

    private String outlineFile;
    private String progressFile;
    private String finalFile;

    private String note;
}
