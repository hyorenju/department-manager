package vn.edu.vnua.department.subject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.aclass.entity.ClassDTO;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.entity.SubjectDTO;
import vn.edu.vnua.department.subject.request.*;
import vn.edu.vnua.department.subject.service.SubjectService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("subject")
@RequiredArgsConstructor
public class SubjectController extends BaseController {
    private final SubjectService subjectService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    private ResponseEntity<?> getSubjectList(@RequestBody @Valid GetSubjectListRequest request) {
        Page<Subject> page = subjectService.getSubjectList(request);
        List<SubjectDTO> response = page.getContent().stream().map(
                subject -> modelMapper.map(subject, SubjectDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("selection")
    private ResponseEntity<?> getSubjectSelection(@RequestBody @Valid GetSubjectSelectionRequest request){
        List<SubjectDTO> response = subjectService.getSubjectSelection(request).stream().map(
                subject -> modelMapper.map(subject, SubjectDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("create")
    private ResponseEntity<?> createSubject(@RequestBody @Valid CreateSubjectRequest request){
        SubjectDTO response = modelMapper.map(subjectService.createSubject(request), SubjectDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    private ResponseEntity<?> updateSubject(@PathVariable String id, @RequestBody @Valid UpdateSubjectRequest request){
        SubjectDTO response = modelMapper.map(subjectService.updateSubject(id, request), SubjectDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    private ResponseEntity<?> deleteSubject(@PathVariable String id){
        SubjectDTO response = modelMapper.map(subjectService.deleteSubject(id), SubjectDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("import")
    public ResponseEntity<?> importSubjectList(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<SubjectDTO> response = subjectService.importFromExcel(file).stream().map(
                subject -> modelMapper.map(subject, SubjectDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("export")
    public ResponseEntity<?> exportSubjectList(@RequestBody @Valid ExportSubjectListRequest request) {
        String response = subjectService.exportToExcel(request);
        return buildItemResponse(response);
    }
}
