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
//    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public ResponseEntity<?> getUserList(@Valid @RequestBody GetUserListRequest request) {
        Page<User> page = userService.getUserList(request);
        List<UserDTO> response = page.getContent().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).toList();
        return buildPageItemResponse(request.getPage(), response.size(), page.getTotalElements(), response);
    }

    @PostMapping("selection")
    public ResponseEntity<?> getUserSelection(@RequestBody @Valid GetUserSelectionRequest request) {
        List<UserDTO> response = userService.getUserSelection(request).stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("create")
//    @PreAuthorize("hasAnyAuthority('PRINCIPAL', 'DEAN', 'MANAGER', 'DEPUTY')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDTO response = modelMapper.map(userService.createUser(request), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
//    @PreAuthorize("hasAnyAuthority('MANAGER', 'DEPUTY')")
    public ResponseEntity<?> updateUser(@PathVariable String id,
                                        @Valid @RequestBody UpdateUserRequest request) {
        UserDTO response = modelMapper.map(userService.updateUser(id, request), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update-profile")
//    @PreAuthorize("hasAnyAuthority('MANAGER', 'DEPUTY')")
    public ResponseEntity<?> updateMyself(@Valid @RequestBody UpdateProfileRequest request) {
        UserDTO response = modelMapper.map(userService.updateProfile(request), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("transfer/{id}")
//    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public ResponseEntity<?> transferRole(@PathVariable String id) {
        UserDTO response = modelMapper.map(userService.transferRole(id), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
//    @PreAuthorize("hasAnyAuthority('MANAGER', 'DEPUTY')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        UserDTO response = modelMapper.map(userService.deleteUser(id), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("export")
    public ResponseEntity<?> exportUserList(@RequestBody @Valid ExportUserListRequest request) {
        String response = userService.exportToExcel(request);
        return buildItemResponse(response);
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        UserDTO response = modelMapper.map(userService.changePassword(request), UserDTO.class);
        return buildItemResponse(response);
    }


    //Dưới đây là api chỉ dành cho postman, front-end dev vui lòng không sử dụng dưới mọi hình thức
    @PostMapping("create-principal/{id}")
    public ResponseEntity<?> createPrincipal(@PathVariable String id) {
        UserDTO response = modelMapper.map(userService.createPrincipal(id), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("dev-create")
    public ResponseEntity<?> devCreateUser(@Valid @RequestBody CreateUserRequest request) {
        UserDTO response = modelMapper.map(userService.devCreateUser(request), UserDTO.class);
        return buildItemResponse(response);
    }
}
