package vn.edu.vnua.department.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class GetPageBaseRequest {
    @NotNull(message = "Số trang không được để trống")
    private Integer page;

    @NotNull(message = "Số bản ghi mỗi trang không được để trống")
    private Integer size;
}
