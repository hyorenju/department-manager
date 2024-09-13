package vn.edu.vnua.department.student.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.intern.entity.InternDTO;
import vn.edu.vnua.department.student.entity.StudentDTO;
import vn.edu.vnua.department.student.request.CreateStudentRequest;
import vn.edu.vnua.department.student.request.GetStudentListRequest;
import vn.edu.vnua.department.student.request.UpdateStudentRequest;
import vn.edu.vnua.department.student.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("student")
@RequiredArgsConstructor
public class StudentController extends BaseController {
    private final StudentService studentService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    public ResponseEntity<?> getStudentList(@RequestBody GetStudentListRequest request) {
        List<StudentDTO> response = studentService.getStudentListIntern(request.getInternId()).stream().map(
                student -> modelMapper.map(student, StudentDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("create")
    public ResponseEntity<?> createStudent(@RequestBody @Valid CreateStudentRequest request) {
        StudentDTO response = modelMapper.map(studentService.createStudent(request), StudentDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody @Valid UpdateStudentRequest request) {
        StudentDTO response = modelMapper.map(studentService.updateStudent(id, request), StudentDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        StudentDTO response = modelMapper.map(studentService.deleteStudent(id), StudentDTO.class);
        return buildItemResponse(response);
    }
}
