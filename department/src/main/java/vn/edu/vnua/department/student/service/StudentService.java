package vn.edu.vnua.department.student.service;

import vn.edu.vnua.department.student.entity.Student;
import vn.edu.vnua.department.student.request.CreateStudentRequest;
import vn.edu.vnua.department.student.request.UpdateStudentRequest;

import java.util.List;

public interface StudentService {
    List<Student> getStudentListIntern(Long internId);
    Student createStudent(CreateStudentRequest request);
    Student updateStudent(Long id, UpdateStudentRequest request);
    Student deleteStudent(Long id);
}
