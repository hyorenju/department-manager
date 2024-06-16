package vn.edu.vnua.department.user.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User getUserById(String id);
    List<User> findAllByDepartment(Department department);
    List<User> findAllByDepartmentId(String id);
    List<User> findAllByDepartmentFacultyId(String id);
    boolean existsById(String id);
}
