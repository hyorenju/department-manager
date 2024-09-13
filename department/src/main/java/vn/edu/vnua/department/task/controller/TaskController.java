package vn.edu.vnua.department.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.task.entity.TaskDTO;
import vn.edu.vnua.department.task.request.CreateTaskRequest;
import vn.edu.vnua.department.task.request.GetTaskListRequest;
import vn.edu.vnua.department.task.request.UpdateTaskRequest;
import vn.edu.vnua.department.task.service.TaskService;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("task")
public class TaskController extends BaseController {
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    private ResponseEntity<?> getTaskList(@RequestBody @Valid GetTaskListRequest request){
        List<TaskDTO> response = taskService.getTaskList(request).stream().map(
                task -> modelMapper.map(task, TaskDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("create")
    private ResponseEntity<?> createTask(@RequestBody @Valid CreateTaskRequest request) throws ParseException {
        TaskDTO response = modelMapper.map(taskService.createdTask(request), TaskDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    private ResponseEntity<?> updateTask(@RequestBody @Valid UpdateTaskRequest request, @PathVariable Long id) throws ParseException {
        TaskDTO response = modelMapper.map(taskService.updateTask(request,id), TaskDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("up/{id}")
    private ResponseEntity<?> upOrdinalNumber(@PathVariable Long id){
        List<TaskDTO> response = taskService.upOrdinalNumber(id).stream().map(
                task -> modelMapper.map(task, TaskDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("delete/{id}")
    private ResponseEntity<?> deleteTask(@PathVariable Long id) {
        TaskDTO response = modelMapper.map(taskService.deleteTask(id), TaskDTO.class);
        return buildItemResponse(response);
    }
}
