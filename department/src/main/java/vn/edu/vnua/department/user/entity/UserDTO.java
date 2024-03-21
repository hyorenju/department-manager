package vn.edu.vnua.department.user.entity;

import lombok.Data;
import vn.edu.vnua.department.department.entity.DepartmentDTO;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.role.entity.RoleDTO;

@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String degree;
    private String email;
    private String phoneNumber;
    private DepartmentDTO department;
    private RoleDTO role;
    private String manage;
    private String note;
}
