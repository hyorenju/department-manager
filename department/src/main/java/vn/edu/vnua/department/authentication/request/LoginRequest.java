package vn.edu.vnua.department.authentication.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String id;
    private String password;
}
