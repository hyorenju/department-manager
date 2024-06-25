package vn.edu.vnua.department.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.repository.InternRepository;
import vn.edu.vnua.department.student.entity.Student;
import vn.edu.vnua.department.student.repository.StudentRepository;
import vn.edu.vnua.department.student.request.CreateStudentRequest;
import vn.edu.vnua.department.student.request.UpdateStudentRequest;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final InternRepository internRepository;
    private final UserRepository userRepository;

    @Override
    public List<Student> getStudentListIntern(Long internId) {
        Intern intern = internRepository.findById(internId).orElseThrow(() -> new RuntimeException(Constants.InternConstant.INTERN_NOT_FOUND));
        return studentRepository.findAllByIntern(intern);
    }

    @Override
    public Student createStudent(CreateStudentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserById(authentication.getPrincipal().toString());

        Intern intern = internRepository.findById(request.getIntern().getId()).orElseThrow(() -> new RuntimeException(Constants.InternConstant.INTERN_NOT_FOUND));

        if (user != intern.getInstructor()) {
            throw new RuntimeException(Constants.StudentConstant.CANNOT_UPDATE);
        }

        return studentRepository.saveAndFlush(Student.builder()
                .studentId(request.getStudentId())
                .name(request.getName())
                .classId(request.getClassId())
                .phoneNumber(request.getPhoneNumber())
                .company(request.getCompany())
                .intern(intern)
                .note(request.getNote())
                .build());
    }

    @Override
    public Student updateStudent(Long id, UpdateStudentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserById(authentication.getPrincipal().toString());

        Student student = studentRepository.findById(id).orElseThrow(()->new RuntimeException(Constants.StudentConstant.STUDENT_NOT_FOUND));

        if (user != student.getIntern().getInstructor()) {
            throw new RuntimeException(Constants.StudentConstant.CANNOT_UPDATE);
        }

        student.setStudentId(request.getStudentId());
        student.setName(request.getName());
        student.setClassId(request.getClassId());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setCompany(request.getCompany());
        student.setNote(request.getNote());

        return studentRepository.saveAndFlush(student);
    }

    @Override
    public Student deleteStudent(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserById(authentication.getPrincipal().toString());
        Student student = studentRepository.findById(id).orElseThrow(()->new RuntimeException(Constants.StudentConstant.STUDENT_NOT_FOUND));

        if (user != student.getIntern().getInstructor()) {
            throw new RuntimeException(Constants.StudentConstant.CANNOT_UPDATE);
        }

        studentRepository.delete(student);
        return student;
    }
}
