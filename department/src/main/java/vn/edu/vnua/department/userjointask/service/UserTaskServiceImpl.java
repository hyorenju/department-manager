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
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.project.repository.ProjectRepository;
import vn.edu.vnua.department.project.request.FilterProjectPage;
import vn.edu.vnua.department.service.mail.MailService;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.task.repository.TaskRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.userjointask.entity.UserTask;
import vn.edu.vnua.department.userjointask.repository.CustomUserTaskRepository;
import vn.edu.vnua.department.userjointask.repository.UserTaskRepository;
import vn.edu.vnua.department.userjointask.request.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTaskServiceImpl implements UserTaskService {
    private final UserTaskRepository userTaskRepository;
    private final UserRepository userRepository;
    private final MasterDataRepository masterDataRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MailService mailService;

    @Override
    public List<UserTask> getUserTaskList(GetUserTaskListRequest request) {
        request.setIsSchedule(false);
        if (request.getTaskId() != null) {
            Specification<UserTask> specification = CustomUserTaskRepository.filterUserTaskList(request);
            return userTaskRepository.findAll(specification);
        }
        return null;
    }

    @Override
    public List<UserTask> getUserTaskCalendar(GetUserTaskListRequest request) {
        request.setIsSchedule(false);
        Specification<UserTask> specification = CustomUserTaskRepository.filterUserTaskList(request);
        return userTaskRepository.findAll(specification);
    }

    @Override
    public List<UserTask> createUserTaskList(Long taskId, CreateUserTaskRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow(()->new RuntimeException(Constants.TaskConstant.TASK_NOT_FOUND));
        MasterData doingStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING);

        List<UserTask> userTasks = new ArrayList<>();
        for (String userId :
                request.getUserIds()) {
            UserTask userTask = new UserTask();
            userTask.setTask(task);
            userTask.setUser(User.builder().id(userId).build());
            userTask.setPersonalStatus(doingStatus);
            userTasks.add(userTask);
        }
        return userTaskRepository.saveAllAndFlush(userTasks);
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

        for (String userId :
                request.getUserIds()) {
            User user = userRepository.getUserById(userId);
            userTasks.add(UserTask.builder()
                    .task(task)
                    .user(user)
                    .personalStatus(masterDataRepository.findAllByType(Constants.MasterDataTypeConstant.TASK_STATUS, Sort.by("id").ascending()).get(0))
                    .finishedAt(null)
                    .build());
            mailService.sendMemberJoinTaskMail(task, user);
        }

        List<UserTask> userTasksDelete = userTaskRepository.findAllByTask(task);
        userTaskRepository.deleteAll(userTasksDelete);

        return userTaskRepository.saveAllAndFlush(userTasks);
    }

    @Override
    public UserTask updatePersonalStatus(UpdatePersonalStatusRequest request, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifier = userRepository.getUserById(authentication.getPrincipal().toString());

        UserTask userTask = userTaskRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserTaskConstant.USER_TASK_NOT_FOUND));
        User createdBy = userTask.getTask().getProject().getCreatedBy();

        if (!modifier.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) &&
                !modifier.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY) &&
                modifier != createdBy) {
            throw new RuntimeException(Constants.TaskConstant.CANNOT_UPDATE);
        }

        MasterData taskStatus = masterDataRepository.findById(request.getTaskStatusId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.TASK_STATUS_NOT_FOUND));

        MasterData doingStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING);
        MasterData doingLateStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING_LATE);
        MasterData finishedLateStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.FINISHED_LATE);
        MasterData finishedSoonerStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.FINISHED_SOONER);
        MasterData finishedOnTimeStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.FINISHED_ON_TIME);

        Timestamp deadline = userTask.getTask().getProject().getDeadline();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        //start of today
        Timestamp today = Timestamp.valueOf(LocalDate.now().atStartOfDay());

        userTask.setPersonalStatus(taskStatus);
        userTask.setNote(request.getNote());
        userTaskRepository.saveAndFlush(userTask);

        Task task = userTask.getTask();
        Project project = task.getProject();
        if (taskStatus == doingStatus || taskStatus == doingLateStatus) {
            userTask.setFinishedAt(null);
            if (deadline.before(now)) {
                task.setTaskStatus(doingLateStatus);
            } else {
                task.setTaskStatus(doingStatus);
            }
            if (deadline.before(now)) {
                project.setProjectStatus(doingLateStatus);
            } else {
                project.setProjectStatus(doingStatus);
            }
            taskRepository.saveAndFlush(task);
            projectRepository.saveAndFlush(project);
            return userTaskRepository.saveAndFlush(userTask);
        }

        if (!userTaskRepository.existsByTaskAndPersonalStatus(task, doingStatus) &&
                !userTaskRepository.existsByTaskAndPersonalStatus(task, doingLateStatus)) {
            if (deadline.before(now)) {
                task.setTaskStatus(finishedLateStatus);
            } else if (deadline.after(now)) {
                task.setTaskStatus(finishedSoonerStatus);
            } else if (deadline.equals(today)) {
                task.setTaskStatus(finishedOnTimeStatus);
            }
        }
        taskRepository.saveAndFlush(task);

        if (!taskRepository.existsByProjectAndTaskStatus(project, doingStatus) &&
                !taskRepository.existsByProjectAndTaskStatus(project, doingLateStatus)) {
            if (deadline.before(now)) {
                project.setProjectStatus(finishedLateStatus);
            } else if (deadline.after(now)) {
                project.setProjectStatus(finishedSoonerStatus);
            } else if (deadline.equals(today)) {
                project.setProjectStatus(finishedOnTimeStatus);
            }
        }
        projectRepository.saveAndFlush(project);


        return userTaskRepository.saveAndFlush(userTask);
    }

    @Override
    public UserTask finishedMyTask(Long id) {
        UserTask userTask = userTaskRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserTaskConstant.USER_TASK_NOT_FOUND));

        Timestamp deadline = userTask.getTask().getDeadline();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Timestamp today = Timestamp.valueOf(LocalDate.now().atStartOfDay());

        MasterData doingStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING);
        MasterData doingLateStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING_LATE);
        MasterData finishedLateStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.FINISHED_LATE);
        MasterData finishedSoonerStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.FINISHED_SOONER);
        MasterData finishedOnTimeStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.FINISHED_ON_TIME);

        if (deadline.before(now)) {
            userTask.setPersonalStatus(finishedLateStatus);
        } else if (deadline.after(now)) {
            userTask.setPersonalStatus(finishedSoonerStatus);
        } else if (deadline.equals(today)) {
            userTask.setPersonalStatus(finishedOnTimeStatus);
        }
        userTask.setFinishedAt(now);
        userTaskRepository.saveAndFlush(userTask);

        Task task = userTask.getTask();
        Project project = task.getProject();
        Timestamp deadline1 = userTask.getTask().getProject().getDeadline();
        Timestamp now1 = Timestamp.valueOf(LocalDateTime.now());
        //start of today
        Timestamp today1 = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        if (!userTaskRepository.existsByTaskAndPersonalStatus(task, doingStatus) &&
                !userTaskRepository.existsByTaskAndPersonalStatus(task, doingLateStatus)) {

            if (deadline1.before(now1)) {
                task.setTaskStatus(finishedLateStatus);
            } else if (deadline1.after(now1)) {
                task.setTaskStatus(finishedSoonerStatus);
            } else if (deadline1.equals(today1)) {
                task.setTaskStatus(finishedOnTimeStatus);
            }
        }
        taskRepository.saveAndFlush(task);

        if (!taskRepository.existsByProjectAndTaskStatus(project, doingStatus) &&
                !taskRepository.existsByProjectAndTaskStatus(project, doingLateStatus)) {

            if (deadline1.before(now1)) {
                project.setProjectStatus(finishedLateStatus);
            } else if (deadline1.after(now1)) {
                project.setProjectStatus(finishedSoonerStatus);
            } else if (deadline1.equals(today1)) {
                project.setProjectStatus(finishedOnTimeStatus);
            }
        }
        projectRepository.saveAndFlush(project);

        return userTaskRepository.saveAndFlush(userTask);
    }

    @Override
    public UserTask deleteUserTask(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifier = userRepository.getUserById(authentication.getPrincipal().toString());

        UserTask userTask = userTaskRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserTaskConstant.USER_TASK_NOT_FOUND));
        User createdBy = userTask.getTask().getProject().getCreatedBy();

        if (!modifier.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) &&
                !modifier.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY) &&
                modifier != createdBy) {
            throw new RuntimeException(Constants.TaskConstant.CANNOT_UPDATE);
        }

        userTaskRepository.delete(userTask);

        return userTask;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkTaskStatus() {
        GetUserTaskListRequest request = new GetUserTaskListRequest();
        request.setIsSchedule(true);
        Specification<UserTask> specification = CustomUserTaskRepository.filterUserTaskList(request);

        List<UserTask> userTaskLate = new ArrayList<>();
        List<UserTask> userTaskList = userTaskRepository.findAll(specification);
        for (UserTask userTask :
                userTaskList) {
            if (userTask.getTask().getDeadline().before(Timestamp.valueOf(LocalDateTime.now()))) {
                userTask.setPersonalStatus(masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING_LATE));
                userTaskLate.add(userTask);
            }
        }

        userTaskRepository.saveAllAndFlush(userTaskLate);
    }
}
