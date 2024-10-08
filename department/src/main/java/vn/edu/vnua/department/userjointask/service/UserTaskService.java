package vn.edu.vnua.department.userjointask.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.project.request.FilterProjectPage;
import vn.edu.vnua.department.userjointask.entity.UserTask;
import vn.edu.vnua.department.userjointask.request.*;

import java.util.List;

public interface UserTaskService {
    List<UserTask> getUserTaskList(GetUserTaskListRequest request);
    List<UserTask> getUserTaskCalendar(GetUserTaskListRequest request);
    List<UserTask> createUserTaskList(Long taskId, CreateUserTaskRequest request);
    Page<UserTask> getUserTaskPage(GetUserTaskPageRequest request);
    List<UserTask> updateParticipant(UpdateUserTaskRequest request);
    UserTask updatePersonalStatus(UpdatePersonalStatusRequest request, Long id);
    UserTask finishedMyTask(Long id);
    UserTask deleteUserTask(Long id);

}
