package vn.edu.vnua.department.userjointask.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.userjointask.entity.UserTask;
import vn.edu.vnua.department.userjointask.request.GetUserTaskListRequest;
import vn.edu.vnua.department.userjointask.request.GetUserTaskPageRequest;
import vn.edu.vnua.department.userjointask.request.UpdateTaskStatusRequest;
import vn.edu.vnua.department.userjointask.request.UpdateUserTaskRequest;

import java.util.List;

public interface UserTaskService {
    List<UserTask> getUserTaskList(GetUserTaskListRequest request);
    Page<UserTask> getUserTaskPage(GetUserTaskPageRequest request);
    List<UserTask> updateParticipant(UpdateUserTaskRequest request);
    UserTask updateTaskStatus(UpdateTaskStatusRequest request, Long id);
    UserTask finishedMyTask(Long id);

}