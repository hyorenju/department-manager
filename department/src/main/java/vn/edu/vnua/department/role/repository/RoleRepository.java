package vn.edu.vnua.department.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.role.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
