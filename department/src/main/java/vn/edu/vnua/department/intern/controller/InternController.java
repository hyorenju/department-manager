package vn.edu.vnua.department.intern.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.entity.InternDTO;
import vn.edu.vnua.department.intern.request.CreateInternRequest;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.intern.request.GetInternListRequest;
import vn.edu.vnua.department.intern.request.UpdateInternRequest;
import vn.edu.vnua.department.intern.service.InternService;

import java.util.List;

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

    @PostMapping("export")
    public ResponseEntity<?> exportInternList(@RequestBody @Valid ExportInternListRequest request) {
        String response = internService.exportToExcel(request);
        return buildItemResponse(response);
    }
}
