package vn.edu.vnua.department.user.request;

import lombok.Data;

@Data
public class GetUserListPickRequest {
    private Long taskId;
    private Boolean isUpdateParticipant;
}
