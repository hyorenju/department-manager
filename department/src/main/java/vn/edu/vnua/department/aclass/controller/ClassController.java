package vn.edu.vnua.department.aclass.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.entity.ClassDTO;
import vn.edu.vnua.department.aclass.request.CreateClassRequest;
import vn.edu.vnua.department.aclass.request.ExportClassListRequest;
import vn.edu.vnua.department.aclass.request.GetClassListRequest;
import vn.edu.vnua.department.aclass.request.UpdateClassRequest;
import vn.edu.vnua.department.aclass.service.ClassService;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("class")
@RequiredArgsConstructor
public class ClassController extends BaseController {
    private final ClassService classService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    private ResponseEntity<?> getClassList(@RequestBody @Valid GetClassListRequest request){
        Page<AClass> page = classService.getClassList(request);
        List<ClassDTO> response = page.getContent().stream().map(
                aClass -> modelMapper.map(aClass, ClassDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("create")
    private ResponseEntity<?> createClass(@RequestBody @Valid CreateClassRequest request) {
        ClassDTO response = modelMapper.map(classService.createClass(request), ClassDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    private ResponseEntity<?> updateClass(@PathVariable String id, @RequestBody @Valid UpdateClassRequest request) {
        ClassDTO response = modelMapper.map(classService.updateClass(id, request), ClassDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    private ResponseEntity<?> updateClass(@PathVariable String id) {
        ClassDTO response = modelMapper.map(classService.deleteClass(id), ClassDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("import")
    public ResponseEntity<?> importClassList(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<ClassDTO> response = classService.importFromExcel(file).stream().map(
                user -> modelMapper.map(user, ClassDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("export")
    public ResponseEntity<?> exportClassList(@RequestBody @Valid ExportClassListRequest request) {
        String response = classService.exportToExcel(request);
        return buildItemResponse(response);
    }
}
