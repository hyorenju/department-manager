package vn.edu.vnua.department.authentication.service;

import vn.edu.vnua.department.response.UserLoginResponse;

public interface AuthenticationService {
    UserLoginResponse authenticateUser(String username, String password);
}
