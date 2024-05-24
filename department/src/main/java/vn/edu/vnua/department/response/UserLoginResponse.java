package vn.edu.vnua.department.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.edu.vnua.department.department.entity.DepartmentBasicDTO;
import vn.edu.vnua.department.department.entity.DepartmentDTO;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.role.entity.RoleDTO;

@Data
@AllArgsConstructor
public class UserLoginResponse{
    private String jwt;
    private RoleDTO role;
    private String id;
    private String firstName;
    private String lastName;
    private MasterDataDTO degree;
    private String email;
    private String phoneNumber;
    private DepartmentBasicDTO department;
    private String manage;
}
