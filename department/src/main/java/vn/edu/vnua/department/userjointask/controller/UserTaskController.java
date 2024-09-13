package vn.edu.vnua.department.userjointask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.userjointask.entity.UserTask;
import vn.edu.vnua.department.userjointask.entity.UserTaskDTO;
import vn.edu.vnua.department.userjointask.request.GetUserTaskListRequest;
import vn.edu.vnua.department.userjointask.request.GetUserTaskPageRequest;
import vn.edu.vnua.department.userjointask.request.UpdateTaskStatusRequest;
import vn.edu.vnua.department.userjointask.request.UpdateUserTaskRequest;
import vn.edu.vnua.department.userjointask.service.UserTaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-task")
public class UserTaskController extends BaseController {
    private final UserTaskService userTaskService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    private ResponseEntity<?> getUserTaskList(@RequestBody @Valid GetUserTaskListRequest request){
        List<UserTaskDTO> response = userTaskService.getUserTaskList(request).stream().map(
                userTask -> modelMapper.map(userTask, UserTaskDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("page")
    public ResponseEntity<?> getUserTaskPage(@RequestBody @Valid GetUserTaskPageRequest request) {
        Page<UserTask> page = userTaskService.getUserTaskPage(request);
        List<UserTaskDTO> response = page.getContent().stream().map(
                userTask -> modelMapper.map(userTask, UserTaskDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("update")
    private ResponseEntity<?> updateParticipant(@RequestBody @Valid UpdateUserTaskRequest request){
        List<UserTaskDTO> response = userTaskService.updateParticipant(request).stream().map(
                userTask -> modelMapper.map(userTask, UserTaskDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("status/{id}")
    private ResponseEntity<?> updateTaskStatus(@PathVariable Long id, @RequestBody @Valid UpdateTaskStatusRequest request){
        UserTaskDTO response = modelMapper.map(userTaskService.updateTaskStatus(request, id), UserTaskDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("finished/{id}")
    private ResponseEntity<?> finishedMyTask(@PathVariable Long id){
        UserTaskDTO response = modelMapper.map(userTaskService.finishedMyTask(id), UserTaskDTO.class);
        return buildItemResponse(response);
    }
}
