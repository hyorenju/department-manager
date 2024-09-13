package vn.edu.vnua.department.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.entity.UserDTO;
import vn.edu.vnua.department.user.request.*;
import vn.edu.vnua.department.user.service.UserService;

import java.io.IOException;
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

    @PostMapping("pick")
    public ResponseEntity<?> getUsersPicked() {
        List<UserDTO> response = userService.getUserPicked().stream().map(
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
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserDTO response = modelMapper.map(userService.updateProfile(request), UserDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update-avatar")
    public ResponseEntity<?> updateAvatar(@RequestBody MultipartFile file) throws IOException {
        UserDTO response = modelMapper.map(userService.updateAvatar(file), UserDTO.class);
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

    @PostMapping("lock/{id}")
    public ResponseEntity<?> lockAccount(@PathVariable String id){
        UserDTO response = modelMapper.map(userService.lockAccount(id), UserDTO.class);
        return buildItemResponse(response);
    }


    //Dưới đây là api để khởi tạo superadmin bằng postman, front-end dev vui lòng không đưa vào giao diện dưới mọi hình thức
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
