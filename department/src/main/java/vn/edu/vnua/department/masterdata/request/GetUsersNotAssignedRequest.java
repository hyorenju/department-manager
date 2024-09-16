package vn.edu.vnua.department.masterdata.request;

import lombok.Data;

@Data
public class GetUsersNotAssignedRequest {
    private String testDay;
    private Integer lessonStart;
    private Integer lessonsTest;
}
