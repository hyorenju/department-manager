package vn.edu.vnua.department.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.student.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByIntern(Intern intern);
}
