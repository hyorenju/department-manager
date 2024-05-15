package vn.edu.vnua.department.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.subject.entity.Subject;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String>, JpaSpecificationExecutor<Subject> {
    Subject getSubjectById(String id);
    List<Subject> findAllByDepartmentId(String departmentId);
    List<Subject> findAllByDepartmentFacultyId(String facultyId);
}
