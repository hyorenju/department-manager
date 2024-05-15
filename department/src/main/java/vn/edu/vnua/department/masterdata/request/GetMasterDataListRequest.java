package vn.edu.vnua.department.masterdata.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetMasterDataListRequest {
    @NotBlank(message = "Loại master data không được để trống")
    private String type;
}
