package vn.edu.vnua.department.project.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.project.request.CreateProjectRequest;
import vn.edu.vnua.department.project.request.FilterProjectPage;
import vn.edu.vnua.department.project.request.GetProjectListRequest;
import vn.edu.vnua.department.project.request.UpdateProjectRequest;

import java.text.ParseException;

public interface ProjectService {
    Page<Project> getProjectList(GetProjectListRequest request);
    Page<Project> filterPage(FilterProjectPage request);
    Project createProject(CreateProjectRequest request) throws ParseException;
    Project updateProject(UpdateProjectRequest request, Long id) throws ParseException;
    Project deleteProject(Long id);
}
