package vn.edu.vnua.department.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.project.repository.CustomProjectRepository;
import vn.edu.vnua.department.project.repository.ProjectRepository;
import vn.edu.vnua.department.project.request.CreateProjectRequest;
import vn.edu.vnua.department.project.request.FilterProjectPage;
import vn.edu.vnua.department.project.request.GetProjectListRequest;
import vn.edu.vnua.department.project.request.UpdateProjectRequest;
import vn.edu.vnua.department.task.service.TaskService;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.userjointask.service.UserTaskService;
import vn.edu.vnua.department.util.MyUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MasterDataRepository masterDataRepository;
    private final TaskService taskService;
    private final UserTaskService userTaskService;

    @Override
    public Page<Project> getProjectList(GetProjectListRequest request) {
        Specification<Project> specification = CustomProjectRepository.filterProjectList(request);
        return projectRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public Page<Project> filterPage(FilterProjectPage request) {
        if(StringUtils.hasText(request.getKeyword()) ||
                StringUtils.hasText(request.getCreatedById()) ||
                StringUtils.hasText(request.getStartDate()) ||
                StringUtils.hasText(request.getEndDate()) ||
                StringUtils.hasText(request.getStatusId()) ||
                StringUtils.hasText(request.getMemberId())) {
            Specification<Project> specification = CustomProjectRepository.filterPage(request);
            return projectRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
        } else {
            return projectRepository.findAll(PageRequest.of(request.getPage() - 1, request.getSize()));
        }
    }

    @Override
    public Project createProject(CreateProjectRequest request) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());

        Timestamp start = MyUtils.convertTimestampFromString(request.getStart());
        Timestamp deadline = MyUtils.convertTimestampFromString(request.getDeadline());
        if (start.after(deadline)) {
            throw new RuntimeException(Constants.ProjectConstant.DATE_BETWEEN_PROBLEM);
        }

        MasterData projectStatus = masterDataRepository.findByName(Constants.MasterDataNameConstant.DOING);

        return projectRepository.saveAndFlush(
                Project.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                        .createdBy(createdBy)
                        .start(start)
                        .projectStatus(projectStatus)
                        .deadline(deadline)
                        .build()
        );
    }

    @Override
    public Project updateProject(UpdateProjectRequest request, Long id) throws ParseException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.ProjectConstant.PROJECT_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifier = userRepository.getUserById(authentication.getPrincipal().toString());

        if (!modifier.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) && project.getCreatedBy() != modifier) {
            throw new RuntimeException(Constants.ProjectConstant.CANNOT_UPDATE);
        }

        Timestamp start;
        Timestamp deadline;
        try {
            start = MyUtils.convertTimestampFromString(request.getStart());
        } catch(Exception e) {
            throw new RuntimeException(Constants.ProjectConstant.START_INVALID);
        }
        try {
            deadline = MyUtils.convertTimestampFromString(request.getDeadline());
        } catch(Exception e) {
            throw new RuntimeException(Constants.ProjectConstant.DEADLINE_INVALID);
        }
        if (start.after(deadline)) {
            throw new RuntimeException(Constants.ProjectConstant.DATE_BETWEEN_PROBLEM);
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStart(start);
        project.setDeadline(deadline);

        return projectRepository.saveAndFlush(project);
    }

    @Override
    public Project deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.ProjectConstant.PROJECT_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifier = userRepository.getUserById(authentication.getPrincipal().toString());

        if (!modifier.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) &&
                !modifier.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY) &&
                project.getCreatedBy() != modifier) {
            throw new RuntimeException(Constants.ProjectConstant.CANNOT_UPDATE);
        }

        projectRepository.delete(project);

        return project;
    }
}
