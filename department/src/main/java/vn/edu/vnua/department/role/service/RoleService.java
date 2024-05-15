package vn.edu.vnua.department.role.service;

import vn.edu.vnua.department.role.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getRoleSelection();
    List<Role> getAllRole();
}
