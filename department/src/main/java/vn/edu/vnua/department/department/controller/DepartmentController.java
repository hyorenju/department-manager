package vn.edu.vnua.department.department.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.entity.DepartmentDTO;
import vn.edu.vnua.department.department.request.CreateDepartmentRequest;
import vn.edu.vnua.department.department.request.GetDepartmentListRequest;
import vn.edu.vnua.department.department.request.GetDepartmentSelectionRequest;
import vn.edu.vnua.department.department.request.UpdateDepartmentRequest;
import vn.edu.vnua.department.department.service.DepartmentService;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.entity.FacultyDTO;

import java.util.List;

@RestController
@RequestMapping("department")
@RequiredArgsConstructor
public class DepartmentController extends BaseController {
    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
//    @PreAuthorize("hasAnyAuthority()")
    public ResponseEntity<?> getFacultyList(@Valid @RequestBody GetDepartmentListRequest request) {
        Page<Department> page = departmentService.getDepartmentList(request);
        List<DepartmentDTO> response = page.getContent().stream().map(
                department -> modelMapper.map(department, DepartmentDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("selection")
    public ResponseEntity<?> getDepartmentSelection(@RequestBody @Valid GetDepartmentSelectionRequest request) {
        List<DepartmentDTO> response = departmentService.getDepartmentSelection(request).stream().map(
                department -> modelMapper.map(department, DepartmentDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("create")
//    @PreAuthorize("hasAnyAuthority()")
    public ResponseEntity<?> createFaculty(@Valid @RequestBody CreateDepartmentRequest request) {
        DepartmentDTO response = modelMapper.map(departmentService.createDepartment(request), DepartmentDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
//    @PreAuthorize("hasAnyAuthority()")
    public ResponseEntity<?> updateFaculty(@PathVariable String id,
                                           @Valid @RequestBody UpdateDepartmentRequest request) {
        DepartmentDTO response = modelMapper.map(departmentService.updateDepartment(id, request), DepartmentDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
//    @PreAuthorize("hasAnyAuthority()")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        DepartmentDTO response = modelMapper.map(departmentService.deleteDepartment(id), DepartmentDTO.class);
        return buildItemResponse(response);
    }
}
