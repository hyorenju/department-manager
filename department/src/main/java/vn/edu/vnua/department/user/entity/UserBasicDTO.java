package vn.edu.vnua.department.user.entity;

import lombok.Data;
import vn.edu.vnua.department.department.entity.DepartmentBasicDTO;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.role.entity.RoleDTO;

@Data
public class UserBasicDTO {
    private String id;
    private String firstName;
    private String lastName;
}
