package vn.edu.vnua.department.userjointask.request;

import lombok.Data;

@Data
public class GetUserTaskListRequest {
    private Long taskId;
    private String userId;
    private Integer monthCalendar;
    private Integer yearCalendar;

    //For schedule
    private Boolean isSchedule;
}
