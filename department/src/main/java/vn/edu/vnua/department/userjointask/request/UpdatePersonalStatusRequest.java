package vn.edu.vnua.department.userjointask.request;

import lombok.Data;

@Data
public class UpdatePersonalStatusRequest {
    private Long taskStatusId;
    private String note;
}
