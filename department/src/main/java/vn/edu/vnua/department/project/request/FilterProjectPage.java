package vn.edu.vnua.department.project.request;

import lombok.Data;
import vn.edu.vnua.department.request.GetPageBaseRequest;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.userjointask.entity.UserTask;

import java.util.List;

@Data
public class FilterProjectPage extends GetPageBaseRequest {
    private String keyword;
    private String createdById;
    private String startDate;
    private String endDate;
    private String memberId;
    private String statusId;
}
