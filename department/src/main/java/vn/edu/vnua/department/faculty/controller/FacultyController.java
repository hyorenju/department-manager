package vn.edu.vnua.department.faculty.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.entity.FacultyDTO;
import vn.edu.vnua.department.faculty.request.CreateFacultyRequest;
import vn.edu.vnua.department.faculty.request.GetFacultyListRequest;
import vn.edu.vnua.department.faculty.request.UpdateFacultyRequest;
import vn.edu.vnua.department.faculty.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("faculty")
@RequiredArgsConstructor
public class FacultyController extends BaseController {
    private final FacultyService facultyService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
//    @PreAuthorize("hasAnyAuthority()")
    public ResponseEntity<?> getFacultyList(@Valid @RequestBody GetFacultyListRequest request) {
        Page<Faculty> page = facultyService.getFacultyList(request);
        List<FacultyDTO> response = page.getContent().stream().map(
                faculty -> modelMapper.map(faculty, FacultyDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("create")
//    @PreAuthorize("hasAnyAuthority()")
    public ResponseEntity<?> createFaculty(@Valid @RequestBody CreateFacultyRequest request) {
        FacultyDTO response = modelMapper.map(facultyService.createFaculty(request), FacultyDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
//    @PreAuthorize("hasAnyAuthority()")
    public ResponseEntity<?> updateFaculty(@PathVariable String id,
                                           @Valid @RequestBody UpdateFacultyRequest request) {
        FacultyDTO response = modelMapper.map(facultyService.updateFaculty(id, request), FacultyDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
//    @PreAuthorize("hasAnyAuthority()")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        FacultyDTO response = modelMapper.map(facultyService.deleteFaculty(id), FacultyDTO.class);
        return buildItemResponse(response);
    }
}
