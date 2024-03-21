package vn.edu.vnua.department.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.edu.vnua.department.department.entity.DepartmentDTO;

@Data
@AllArgsConstructor
public class UserLoginResponse{
    private String jwt;
    private String roleId;
    private String id;
    private String firstName;
    private String lastName;
    private String degree;
    private String email;
    private String phoneNumber;
    private DepartmentDTO department;
    private String manage;
}
