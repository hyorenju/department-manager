package vn.edu.vnua.department.exam.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.exam.entity.ExamDTO;
import vn.edu.vnua.department.exam.request.*;
import vn.edu.vnua.department.exam.service.ExamService;
import vn.edu.vnua.department.teaching.entity.TeachingDTO;
import vn.edu.vnua.department.teaching.request.ExportTeachingRequest;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("exam")
@RequiredArgsConstructor
public class ExamController extends BaseController {
    private final ExamService examService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    private ResponseEntity<?> getExamList(@RequestBody @Valid GetExamListRequest request) {
        Page<Exam> page = examService.getExamList(request);
        List<ExamDTO> response = page.getContent().stream().map(
                exam -> modelMapper.map(exam, ExamDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("assign-selection/{id}")
    private ResponseEntity<?> getAssignSelection(@PathVariable Long id){
        List<UserDTO> response = examService.getUsersNotAssigned(id).stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("create")
    private ResponseEntity<?> createExam(@RequestBody @Valid CreateExamRequest request) throws ParseException {
        ExamDTO response = modelMapper.map(examService.createExam(request), ExamDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    private ResponseEntity<?> updateExam(@PathVariable Long id, @RequestBody @Valid UpdateExamRequest request) throws ParseException {
        ExamDTO response = modelMapper.map(examService.updateExam(id,request), ExamDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    private ResponseEntity<?> deleteExam(@PathVariable Long id) {
        ExamDTO response = modelMapper.map(examService.deleteExam(id), ExamDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("import")
    public ResponseEntity<?> importExamList(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<ExamDTO> response = examService.importFromExcel(file).stream().map(
                exam -> modelMapper.map(exam, ExamDTO.class)
        ).toList();
        return buildItemResponse(response);
    }

    @PostMapping("export")
    public ResponseEntity<?> exportTeachingList(@RequestBody @Valid ExportExamRequest request) {
        String response = examService.exportToExcel(request);
        return buildItemResponse(response);
    }

    @PostMapping("update-proctor/{id}")
    private ResponseEntity<?> UpdateProctorExam(@PathVariable Long id, @RequestBody @Valid UpdateProctorExamRequest request) throws ParseException {
        ExamDTO response = modelMapper.map(examService.updateProctor(id,request), ExamDTO.class);
        return buildItemResponse(response);
    }
}
