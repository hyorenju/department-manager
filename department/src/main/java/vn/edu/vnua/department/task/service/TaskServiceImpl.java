package vn.edu.vnua.department.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.project.repository.ProjectRepository;
import vn.edu.vnua.department.service.mail.MailService;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.task.repository.CustomTaskRepository;
import vn.edu.vnua.department.task.repository.TaskRepository;
import vn.edu.vnua.department.task.request.CreateTaskRequest;
import vn.edu.vnua.department.task.request.GetTaskListRequest;
import vn.edu.vnua.department.task.request.UpdateTaskRequest;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.userjointask.entity.UserTask;
import vn.edu.vnua.department.userjointask.repository.UserTaskRepository;
import vn.edu.vnua.department.util.MyUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final MasterDataRepository masterDataRepository;
    private final UserTaskRepository userTaskRepository;
    private final MailService mailService;

    @Override
    public List<Task> getTaskList(GetTaskListRequest request) {
        Specification<Task> specification = CustomTaskRepository.filterTaskList(request);
        return taskRepository.findAll(specification);
    }

    @Override
    public Task createdTask(CreateTaskRequest request) throws ParseException {
        //Lấy thông tin người tạo
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifier = userRepository.getUserById(authentication.getPrincipal().toString());

        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new RuntimeException(Constants.ProjectConstant.PROJECT_NOT_FOUND));
        //Kiểm tra nếu người tạo task khác người tạo project
        User createdBy = project.getCreatedBy();
        if (!modifier.getRole().getId().equals(Constants.RoleIdConstant.ADMIN) &&
                modifier != createdBy) {
            throw new RuntimeException(Constants.TaskConstant.CANNOT_UPDATE);
        }

        //Lỗi nếu ngày kết thúc < ngày bắt đầu
        Timestamp start = MyUtils.convertTimestampFromString(request.getStart());
        Timestamp deadline = MyUtils.convertTimestampFromString(request.getDeadline());
        if (start.after(deadline)) {
            throw new RuntimeException(Constants.TaskConstant.DATE_BETWEEN_PROBLEM);
        }

        String projectStart = MyUtils.convertTimestampToString(project.getStart());
        String projectDeadline = MyUtils.convertTimestampToString(project.getDeadline());
        //Lỗi nếu ngày bắt đầu của task < ngày bắt đầu của project
        if (start.before(project.getStart()) || start.after(project.getDeadline())) {
            throw new RuntimeException(String.format(Constants.TaskConstant.START_PROBLEM, projectStart, projectDeadline));
        }
        //Lỗi nếu ngày kết thúc của task > ngày kết thúc của project
        if (deadline.before(project.getStart()) || deadline.after(project.getDeadline())) {
            throw new RuntimeException(String.format(Constants.TaskConstant.DEADLINE_PROBLEM, projectStart, projectDeadline));
        }

        MasterData doingStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING);

        Task task = new Task();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        task.setStart(start);
        task.setDeadline(deadline);
        task.setOrdinalNumber(project.getTasks().size() + 1);
        task.setTaskStatus(doingStatus);
        task.setProject(project);
        taskRepository.saveAndFlush(task);

        List<UserTask> userTasks = new ArrayList<>();
        for (String userId :
                request.getUserIds()) {
            User user = userRepository.getUserById(userId);
            UserTask userTask = new UserTask();
            userTask.setTask(task);
            userTask.setUser(user);
            userTask.setPersonalStatus(doingStatus);
            userTasks.add(userTask);
            mailService.sendMemberJoinTaskMail(task, user);
        }
        userTaskRepository.saveAll(userTasks);

        task.setUserJoined(userTasks);

        return task;
    }

    @Override
    public Task updateTask(UpdateTaskRequest request, Long id) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifier = userRepository.getUserById(authentication.getPrincipal().toString());

        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.TaskConstant.TASK_NOT_FOUND));

        User createdBy = task.getProject().getCreatedBy();

        if (!modifier.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) &&
                !modifier.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY) &&
                modifier != createdBy) {
            throw new RuntimeException(Constants.TaskConstant.CANNOT_UPDATE);
        }

        Timestamp start;
        Timestamp deadline;
        try {
            start = MyUtils.convertTimestampFromString(request.getStart());
        } catch (Exception e) {
            throw new RuntimeException(Constants.ProjectConstant.START_INVALID);
        }
        try {
            deadline = MyUtils.convertTimestampFromString(request.getDeadline());
        } catch (Exception e) {
            throw new RuntimeException(Constants.ProjectConstant.DEADLINE_INVALID);
        }
        if (start.after(deadline)) {
            throw new RuntimeException(Constants.TaskConstant.DATE_BETWEEN_PROBLEM);
        }

        Timestamp projectStart = task.getProject().getStart();
        Timestamp projectDeadline = task.getProject().getDeadline();
        String projectStartStr = MyUtils.convertTimestampToString(projectStart);
        String projectDeadlineStr = MyUtils.convertTimestampToString(projectDeadline);
        if (start.before(projectStart) || start.after(projectDeadline)) {
            throw new RuntimeException(String.format(Constants.TaskConstant.START_PROBLEM, projectStartStr, projectDeadlineStr));
        }
        if (deadline.before(projectStart) || deadline.after(projectDeadline)) {
            throw new RuntimeException(String.format(Constants.TaskConstant.DEADLINE_PROBLEM, projectStartStr, projectDeadlineStr));
        }

//        MasterData doingStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING);

//        List<UserTask> userTasks = new ArrayList<>();
//        for (String userId :
//                request.getUserIds()) {
//            UserTask userTask = new UserTask();
//            userTask.setTask(task);
//            userTask.setUser(User.builder().id(userId).build());
//            userTask.setPersonalStatus(doingStatus);
//            userTasks.add(userTask);
//        }
//        userTaskRepository.saveAll(userTasks);

        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setStart(start);
        task.setDeadline(deadline);
//        task.setUserJoined(userTasks);

        return taskRepository.saveAndFlush(task);
    }

    @Override
    public List<Task> upOrdinalNumber(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifier = userRepository.getUserById(authentication.getPrincipal().toString());

        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.TaskConstant.TASK_NOT_FOUND));

        User createdBy = task.getProject().getCreatedBy();

        if (!modifier.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) &&
                !modifier.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY) &&
                modifier != createdBy) {
            throw new RuntimeException(Constants.TaskConstant.CANNOT_UPDATE);
        }

        Task aboveTask = null;
        if (task.getOrdinalNumber() > 1) {
            aboveTask = taskRepository.findByProjectAndOrdinalNumber(task.getProject(), task.getOrdinalNumber() - 1);
            Integer intermediate = task.getOrdinalNumber();
            task.setOrdinalNumber(aboveTask.getOrdinalNumber());
            aboveTask.setOrdinalNumber(intermediate);
        }

        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        taskList.add(aboveTask);
        return taskRepository.saveAllAndFlush(taskList);
    }

    @Override
    public Task deleteTask(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifier = userRepository.getUserById(authentication.getPrincipal().toString());

        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.TaskConstant.TASK_NOT_FOUND));

        User createdBy = task.getProject().getCreatedBy();

        if (!modifier.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) &&
                !modifier.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY) &&
                modifier != createdBy) {
            throw new RuntimeException(Constants.TaskConstant.CANNOT_UPDATE);
        }

        taskRepository.delete(task);

        return task;
    }
}
