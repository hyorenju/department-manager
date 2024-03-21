package vn.edu.vnua.department.authentication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnua.department.authentication.request.LoginRequest;
import vn.edu.vnua.department.authentication.service.AuthenticationService;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.response.UserLoginResponse;


@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class AuthenticationController extends BaseController {
    private final AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        UserLoginResponse response = authenticationService.authenticateUser(request.getId(), request.getPassword());
        return buildItemResponse(response);
    }
}
