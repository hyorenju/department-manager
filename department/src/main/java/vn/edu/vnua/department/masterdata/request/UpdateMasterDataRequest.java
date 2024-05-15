package vn.edu.vnua.department.masterdata.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateMasterDataRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;
}
