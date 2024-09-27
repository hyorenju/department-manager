package vn.edu.vnua.department.intern.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.exam.entity.ExamDTO;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.entity.InternDTO;
import vn.edu.vnua.department.intern.request.*;
import vn.edu.vnua.department.intern.service.InternService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("intern")
@RequiredArgsConstructor
public class InternController extends BaseController {
    private final InternService internService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    public ResponseEntity<?> getInternList(@RequestBody @Valid GetInternListRequest request) {
        Page<Intern> page = internService.getInternList(request);
        List<InternDTO> response = page.getContent().stream().map(
                intern -> modelMapper.map(intern, InternDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("create")
    public ResponseEntity<?> createIntern(@RequestBody @Valid CreateInternRequest request) {
        InternDTO response = modelMapper.map(internService.createIntern(request), InternDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    public ResponseEntity<?> updateIntern(@PathVariable Long id, @RequestBody @Valid UpdateInternRequest request) {
        InternDTO response = modelMapper.map(internService.updateIntern(id, request), InternDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<?> deleteIntern(@PathVariable Long id) {
        InternDTO response = modelMapper.map(internService.deleteIntern(id), InternDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("lock/{id}")
    public ResponseEntity<?> lockIntern(@PathVariable Long id) {
        InternDTO response = modelMapper.map(internService.lockIntern(id), InternDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("lock")
    public ResponseEntity<?> lockInternList(@RequestBody LockInternListRequest request) {
        List<InternDTO> response = internService.lockInternList(request).stream().map(
                intern -> modelMapper.map(intern, InternDTO.class)).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("import")
    public ResponseEntity<?> importInternList(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<InternDTO> response = internService.importFromExcel(file).stream().map(
                intern -> modelMapper.map(intern, InternDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("export")
    public ResponseEntity<?> exportInternList(@RequestBody @Valid ExportInternListRequest request) {
        String response = internService.exportToExcel(request);
        return buildItemResponse(response);
    }
}
