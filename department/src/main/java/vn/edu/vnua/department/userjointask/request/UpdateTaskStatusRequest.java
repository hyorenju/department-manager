package vn.edu.vnua.department.userjointask.request;

import lombok.Data;

@Data
public class UpdateTaskStatusRequest {
    private Long taskStatusId;
    private String note;
}
