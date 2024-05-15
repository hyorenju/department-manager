package vn.edu.vnua.department.masterdata.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMasterDataRequest {
    private Long id;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Phân loại không được để trống")
    private String type;
}
