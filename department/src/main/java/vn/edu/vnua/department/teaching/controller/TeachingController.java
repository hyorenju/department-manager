package vn.edu.vnua.department.teaching.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.teaching.entity.TeachingDTO;
import vn.edu.vnua.department.teaching.request.*;
import vn.edu.vnua.department.teaching.service.TeachingService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("teaching")
@RequiredArgsConstructor
public class TeachingController extends BaseController {
    private final TeachingService teachingService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    private ResponseEntity<?> getTeachingList(@RequestBody @Valid GetTeachingListRequest request){
        Page<Teaching> page = teachingService.getTeachingList(request);
        List<TeachingDTO> response = page.getContent().stream().map(
                teaching -> modelMapper.map(teaching, TeachingDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("create")
    private ResponseEntity<?> createTeaching(@RequestBody @Valid CreateTeachingRequest request) throws ParseException {
        TeachingDTO response = modelMapper.map(teachingService.createTeaching(request), TeachingDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    private ResponseEntity<?> updateTeaching(@PathVariable Long id, @RequestBody @Valid UpdateTeachingRequest request){
        TeachingDTO response = modelMapper.map(teachingService.updateTeaching(id, request), TeachingDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    private ResponseEntity<?> updateTeaching(@PathVariable Long id){
        TeachingDTO response = modelMapper.map(teachingService.deleteTeaching(id), TeachingDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("lock/{id}")
    private ResponseEntity<?> lockTeaching(@PathVariable Long id){
        TeachingDTO response = modelMapper.map(teachingService.lockTeaching(id), TeachingDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("warn/{id}")
    private ResponseEntity<?> warnTeaching(@PathVariable Long id){
        TeachingDTO response = modelMapper.map(teachingService.warnTeaching(id), TeachingDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("lock")
    private ResponseEntity<?> lockTeachingList(@RequestBody LockTeachingListRequest request){
        List<TeachingDTO> response = teachingService.lockTeachingList(request).stream().map(
                teaching -> modelMapper.map(teaching, TeachingDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("import")
    public ResponseEntity<?> importTeachingList(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<TeachingDTO> response = teachingService.importFromExcel(file).stream().map(
                teaching -> modelMapper.map(teaching, TeachingDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("export")
    public ResponseEntity<?> exportTeachingList(@RequestBody @Valid ExportTeachingRequest request) {
        String response = teachingService.exportToExcel(request);
        return buildItemResponse(response);
    }

    @PostMapping("read-from-daotao/{teacherId}")
    public ResponseEntity<?> readFromDaoTao(@PathVariable String teacherId) throws IOException {
        List<TeachingDTO> response = teachingService.readFromDaoTao(teacherId).stream().map(
                teaching -> modelMapper.map(teaching, TeachingDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }
}
