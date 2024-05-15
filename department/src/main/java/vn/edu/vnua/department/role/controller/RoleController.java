package vn.edu.vnua.department.role.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.role.entity.RoleDTO;
import vn.edu.vnua.department.role.service.RoleService;

import java.util.List;

@RequestMapping("role")
@RestController
@RequiredArgsConstructor
public class RoleController extends BaseController {
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @PostMapping("selection")
    private ResponseEntity<?> getRoleSelection() {
        List<RoleDTO> response = roleService.getRoleSelection().stream().map(
                role -> modelMapper.map(role, RoleDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("all")
    private ResponseEntity<?> getAllRole() {
        List<RoleDTO> response = roleService.getAllRole().stream().map(
                role -> modelMapper.map(role, RoleDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }
}
