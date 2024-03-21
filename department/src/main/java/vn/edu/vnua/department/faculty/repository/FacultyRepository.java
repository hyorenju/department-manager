package vn.edu.vnua.department.faculty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.faculty.entity.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, String> {
}
