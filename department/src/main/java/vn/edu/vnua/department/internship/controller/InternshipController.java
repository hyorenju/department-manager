package vn.edu.vnua.department.internship.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.entity.InternDTO;
import vn.edu.vnua.department.intern.request.CreateInternRequest;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.intern.request.GetInternListRequest;
import vn.edu.vnua.department.intern.request.LockInternListRequest;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.internship.entity.InternshipDTO;
import vn.edu.vnua.department.internship.request.CreateInternshipRequest;
import vn.edu.vnua.department.internship.request.GetInternshipListRequest;
import vn.edu.vnua.department.internship.request.LockInternshipListRequest;
import vn.edu.vnua.department.internship.request.UpdateInternshipRequest;
import vn.edu.vnua.department.internship.service.InternshipService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("internship")
@RequiredArgsConstructor
public class InternshipController  extends BaseController {
    private final InternshipService internshipService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    public ResponseEntity<?> getInternshipList(@RequestBody @Valid GetInternshipListRequest request) {
        Page<Internship> page = internshipService.getInternList(request);
        List<InternshipDTO> response = page.getContent().stream().map(
                internship -> modelMapper.map(internship, InternshipDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("create")
    public ResponseEntity<?> createInternship(@RequestBody @Valid CreateInternshipRequest request) {
        InternshipDTO response = modelMapper.map(internshipService.createIntern(request), InternshipDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    public ResponseEntity<?> updateInternship(@PathVariable Long id, @RequestBody @Valid UpdateInternshipRequest request) {
        InternshipDTO response = modelMapper.map(internshipService.updateIntern(id, request), InternshipDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<?> updateInternship(@PathVariable Long id) {
        InternshipDTO response = modelMapper.map(internshipService.deleteIntern(id), InternshipDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("lock/{id}")
    public ResponseEntity<?> lockInternship(@PathVariable Long id) {
        InternDTO response = modelMapper.map(internshipService.lockInternship(id), InternDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("lock")
    public ResponseEntity<?> lockInternshipList(@RequestBody LockInternshipListRequest request) {
        List<InternDTO> response = internshipService.lockInternshipList(request).stream().map(
                intern -> modelMapper.map(intern, InternDTO.class)).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("import")
    public ResponseEntity<?> importInternshipList(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<InternshipDTO> response = internshipService.importFromExcel(file).stream().map(
                internship -> modelMapper.map(internship, InternshipDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("export")
    public ResponseEntity<?> exportInternshipList(@RequestBody @Valid ExportInternListRequest request) {
        String response = internshipService.exportToExcel(request);
        return buildItemResponse(response);
    }
}
