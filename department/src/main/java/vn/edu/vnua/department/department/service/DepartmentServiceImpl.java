package vn.edu.vnua.department.department.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.repository.CustomDepartmentRepository;
import vn.edu.vnua.department.department.repository.DepartmentRepository;
import vn.edu.vnua.department.department.request.CreateDepartmentRequest;
import vn.edu.vnua.department.department.request.GetDepartmentListRequest;
import vn.edu.vnua.department.department.request.GetDepartmentSelectionRequest;
import vn.edu.vnua.department.department.request.UpdateDepartmentRequest;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.repository.FacultyRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Department> getDepartmentList(GetDepartmentListRequest request) {
        Specification<Department> specification = CustomDepartmentRepository.filterDepartmentList(request);
        return departmentRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public List<Department> getDepartmentSelection(GetDepartmentSelectionRequest request) {
        if(!StringUtils.hasText(request.getFacultyId())){
            return departmentRepository.findAll();
        }
        return departmentRepository.findAllByFacultyId(request.getFacultyId(), Sort.by("name").ascending());
    }

    @Override
    public Department createDepartment(CreateDepartmentRequest request) {
        if (departmentRepository.existsById(request.getId())) {
            throw new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_ALREADY_EXIST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());

        Faculty faculty = facultyRepository.findById(request.getFaculty().getId()).orElseThrow(() -> new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));
        return departmentRepository.saveAndFlush(
                Department.builder()
                        .id(request.getId())
                        .name(request.getName())
                        .faculty(faculty)
                        .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                        .createdBy(createdBy)
                        .build()
        );
    }

    @Override
    public Department updateDepartment(String id, UpdateDepartmentRequest request) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_NOT_FOUND));
        Faculty faculty = facultyRepository.findById(request.getFaculty().getId()).orElseThrow(() -> new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedBy = userRepository.getUserById(authentication.getPrincipal().toString());

        department.setName(request.getName());
        department.setFaculty(faculty);
        department.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        department.setModifiedBy(modifiedBy);

        return departmentRepository.saveAndFlush(department);
    }

    @Override
    public Department deleteDepartment(String id) {
        try {

        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_NOT_FOUND));
        departmentRepository.delete(department);
        return department;
        } catch (Exception e){
            throw new RuntimeException(Constants.DepartmentConstant.CANNOT_DELETE);
        }
    }
}
