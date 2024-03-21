package vn.edu.vnua.department.department.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.repository.CustomDepartmentRepository;
import vn.edu.vnua.department.department.repository.DepartmentRepository;
import vn.edu.vnua.department.department.request.CreateDepartmentRequest;
import vn.edu.vnua.department.department.request.GetDepartmentListRequest;
import vn.edu.vnua.department.department.request.UpdateDepartmentRequest;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.repository.FacultyRepository;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    @Override
    public Page<Department> getDepartmentList(GetDepartmentListRequest request) {
        Specification<Department> specification = CustomDepartmentRepository.filterDepartmentList(request);
        return departmentRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public Department createDepartment(CreateDepartmentRequest request) {
        if (departmentRepository.existsById(request.getId())) {
            throw new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_ALREADY_EXIST);
        }
        Faculty faculty = facultyRepository.findById(request.getFaculty().getId()).orElseThrow(() -> new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));
        return departmentRepository.saveAndFlush(
                Department.builder()
                        .id(request.getId())
                        .name(request.getName())
                        .faculty(faculty)
                        .build()
        );
    }

    @Override
    public Department updateDepartment(String id, UpdateDepartmentRequest request) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_NOT_FOUND));
        Faculty faculty = facultyRepository.findById(request.getFaculty().getId()).orElseThrow(() -> new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));

        department.setName(request.getName());
        department.setFaculty(faculty);
        return departmentRepository.saveAndFlush(department);
    }

    @Override
    public Department deleteDepartment(String id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_NOT_FOUND));
        departmentRepository.delete(department);
        return department;
    }
}
