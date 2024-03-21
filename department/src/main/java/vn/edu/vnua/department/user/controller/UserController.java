package vn.edu.vnua.department.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.entity.UserDTO;
import vn.edu.vnua.department.user.request.*;
import vn.edu.vnua.department.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    @PreAuthorize("hasAnyAuthority()")
    public ResponseEntity<?> getUserList(@Valid @RequestBody GetUserListRequest request) {
        Page<User> page = userService.getUserList(request);
        List<UserDTO> response = page.getContent().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("create-user")
    @PreAuthorize("hasAnyAuthority('PRINCIPAL', 'DEAN', 'MANAGER', 'DEPUTY')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDTO response = modelMapper.map(userService.createUser(request), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("create-dean/{id}")
    @PreAuthorize("hasAnyAuthority('PRINCIPAL')")
    public ResponseEntity<?> createDean(@PathVariable String id, @Valid @RequestBody CreateDeanRequest request) {
        UserDTO response = modelMapper.map(userService.createDean(id, request), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("create-manager/{id}")
    @PreAuthorize("hasAnyAuthority('DEAN')")
    public ResponseEntity<?> createManager(@PathVariable String id, @Valid @RequestBody CreateManagerRequest request) {
        UserDTO response = modelMapper.map(userService.createManager(id, request), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'DEPUTY')")
    public ResponseEntity<?> updateUser(@PathVariable String id,
                                           @Valid @RequestBody UpdateUserRequest request) {
        UserDTO response = modelMapper.map(userService.updateUser(id, request), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("transfer/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public ResponseEntity<?> transferRole(@PathVariable String id) {
        UserDTO response = modelMapper.map(userService.transferRole(id), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'DEPUTY')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        UserDTO response = modelMapper.map(userService.deleteUser(id), UserDTO.class);
        return buildItemResponse(response);
    }




    //Dưới đây là api tạo PRINCIPAL chỉ dành cho test, front-end dev vui lòng không sử dụng dưới mọi hình thức
    @PostMapping("create-principal/{id}")
    public ResponseEntity<?> createPrincipal(@PathVariable String id) {
        UserDTO response = modelMapper.map(userService.createPrincipal(id), UserDTO.class);
        return buildItemResponse(response);
    }
}
