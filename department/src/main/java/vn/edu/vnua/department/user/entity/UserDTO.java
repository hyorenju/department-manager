package vn.edu.vnua.department.user.entity;

import lombok.Data;
import vn.edu.vnua.department.department.entity.DepartmentBasicDTO;
import vn.edu.vnua.department.department.entity.DepartmentDTO;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.role.entity.RoleDTO;

import java.sql.Timestamp;

@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private MasterDataDTO degree;
    private String email;
    private String phoneNumber;
    private DepartmentBasicDTO department;
    private String note;
    private RoleDTO role;
    private String manage;
    private String avatar;
    private Boolean isLock;
}
