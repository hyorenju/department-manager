package vn.edu.vnua.department.task.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTaskParticipantRequest {
    private List<String> userIds;
}
