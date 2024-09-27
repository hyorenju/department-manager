package vn.edu.vnua.department.department.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.department.entity.Department;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String>, JpaSpecificationExecutor<Department> {
    List<Department> findAllByFacultyId(String facultyId, Sort sort);
    boolean existsByName(String name);
    Department findByName(String name);
}
