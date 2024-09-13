package vn.edu.vnua.department.task.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.task.request.CreateTaskRequest;
import vn.edu.vnua.department.task.request.GetTaskListRequest;
import vn.edu.vnua.department.task.request.UpdateTaskParticipantRequest;
import vn.edu.vnua.department.task.request.UpdateTaskRequest;

import java.text.ParseException;
import java.util.List;

public interface TaskService {
    List<Task> getTaskList(GetTaskListRequest request);
    Task createdTask(CreateTaskRequest request) throws ParseException;
    Task updateTask(UpdateTaskRequest request, Long id) throws ParseException;
    List<Task> upOrdinalNumber(Long id);
    Task deleteTask(Long id);
}
