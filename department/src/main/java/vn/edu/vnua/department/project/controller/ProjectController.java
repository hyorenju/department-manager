package vn.edu.vnua.department.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.entity.ClassDTO;
import vn.edu.vnua.department.aclass.request.GetClassListRequest;
import vn.edu.vnua.department.aclass.service.ClassService;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.project.entity.ProjectDTO;
import vn.edu.vnua.department.project.request.CreateProjectRequest;
import vn.edu.vnua.department.project.request.GetProjectListRequest;
import vn.edu.vnua.department.project.request.UpdateProjectRequest;
import vn.edu.vnua.department.project.service.ProjectService;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("project")
public class ProjectController extends BaseController {
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    private ResponseEntity<?> getProjectList(@RequestBody @Valid GetProjectListRequest request){
        Page<Project> page = projectService.getProjectList(request);
        List<ProjectDTO> response = page.getContent().stream().map(
                project -> modelMapper.map(project, ProjectDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("create")
    private ResponseEntity<?> createProject(@RequestBody @Valid CreateProjectRequest request) throws ParseException {
        ProjectDTO response = modelMapper.map(projectService.createProject(request), ProjectDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    private ResponseEntity<?> updateProject(@RequestBody @Valid UpdateProjectRequest request, @PathVariable Long id) throws ParseException {
        ProjectDTO response = modelMapper.map(projectService.updateProject(request,id), ProjectDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    private ResponseEntity<?> deleteProject(@PathVariable Long id) {
        ProjectDTO response = modelMapper.map(projectService.deleteProject(id), ProjectDTO.class);
        return buildItemResponse(response);
    }
}
