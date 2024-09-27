package vn.edu.vnua.department.userjointask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.task.repository.TaskRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.userjointask.entity.UserTask;
import vn.edu.vnua.department.userjointask.repository.CustomUserTaskRepository;
import vn.edu.vnua.department.userjointask.repository.UserTaskRepository;
import vn.edu.vnua.department.userjointask.request.GetUserTaskListRequest;
import vn.edu.vnua.department.userjointask.request.GetUserTaskPageRequest;
import vn.edu.vnua.department.userjointask.request.UpdatePersonalStatusRequest;
import vn.edu.vnua.department.userjointask.request.UpdateUserTaskRequest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTaskServiceImpl implements UserTaskService{
    private final UserTaskRepository userTaskRepository;
    private final UserRepository userRepository;
    private final MasterDataRepository masterDataRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<UserTask> getUserTaskList(GetUserTaskListRequest request) {
        request.setIsSchedule(false);
        if(request.getTaskId() != null) {
            Specification<UserTask> specification = CustomUserTaskRepository.filterUserTaskList(request);
            return userTaskRepository.findAll(specification);
        }
        return null;
    }

    @Override
    public Page<UserTask> getUserTaskPage(GetUserTaskPageRequest request) {
        Specification<UserTask> specification = CustomUserTaskRepository.filterUserTaskPage(request);
        return userTaskRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public List<UserTask> updateParticipant(UpdateUserTaskRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifier = userRepository.getUserById(authentication.getPrincipal().toString());

        Task task = taskRepository.findById(request.getTaskId()).orElseThrow(() -> new RuntimeException(Constants.TaskConstant.TASK_NOT_FOUND));

        User createdBy = task.getProject().getCreatedBy();

        if (!modifier.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) &&
                !modifier.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY) &&
                modifier != createdBy) {
            throw new RuntimeException(Constants.UserTaskConstant.CANNOT_UPDATE);
        }

        List<UserTask> userTasks = new ArrayList<>();

        for (String userId:
                request.getUserIds()) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
            userTasks.add(UserTask.builder()
                    .task(task)
                    .user(user)
                    .personalStatus(masterDataRepository.findAllByType(Constants.MasterDataTypeConstant.TASK_STATUS, Sort.by("id").ascending()).get(0))
                    .finishedAt(null)
                    .build());
        }

        List<UserTask> userTasksDelete = userTaskRepository.findAllByTask(task);
        userTaskRepository.deleteAll(userTasksDelete);

        return userTaskRepository.saveAllAndFlush(userTasks);
    }

    @Override
    public UserTask updatePersonalStatus(UpdatePersonalStatusRequest request, Long id) {
        MasterData taskStatus = masterDataRepository.findById(request.getTaskStatusId()).orElseThrow(()->new RuntimeException(Constants.MasterDataConstant.TASK_STATUS_NOT_FOUND));

        UserTask userTask = userTaskRepository.findById(id).orElseThrow(()-> new RuntimeException(Constants.UserTaskConstant.USER_TASK_NOT_FOUND));

        userTask.setPersonalStatus(taskStatus);
        userTask.setNote(request.getNote());

        return userTaskRepository.saveAndFlush(userTask);
    }

    @Override
    public UserTask finishedMyTask(Long id) {
        UserTask userTask = userTaskRepository.findById(id).orElseThrow(()-> new RuntimeException(Constants.UserTaskConstant.USER_TASK_NOT_FOUND));

        Timestamp deadline = userTask.getTask().getDeadline();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Timestamp startOfToday = Timestamp.valueOf(LocalDate.now().atStartOfDay());

        if(deadline.before(now)){
            userTask.setPersonalStatus(masterDataRepository.findByName(Constants.MasterDataNameConstant.FINISHED_LATE));
        } else if(deadline.after(now)) {
            userTask.setPersonalStatus(masterDataRepository.findByName(Constants.MasterDataNameConstant.FINISHED_SOONER));
        } else if(deadline.equals(startOfToday)){
            userTask.setPersonalStatus(masterDataRepository.findByName(Constants.MasterDataNameConstant.FINISHED_ON_TIME));
        }
        userTask.setFinishedAt(now);

        return userTaskRepository.saveAndFlush(userTask);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkTaskStatus() {
        GetUserTaskListRequest request = new GetUserTaskListRequest();
        request.setIsSchedule(true);
        Specification<UserTask> specification = CustomUserTaskRepository.filterUserTaskList(request);

        List<UserTask> userTaskLate = new ArrayList<>();
        List<UserTask> userTaskList = userTaskRepository.findAll(specification);
        for (UserTask userTask:
             userTaskList) {
            if(userTask.getTask().getDeadline().before(Timestamp.valueOf(LocalDateTime.now()))){
                userTask.setPersonalStatus(masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING_LATE));
                userTaskLate.add(userTask);
            }
        }

        userTaskRepository.saveAllAndFlush(userTaskLate);
    }
}
