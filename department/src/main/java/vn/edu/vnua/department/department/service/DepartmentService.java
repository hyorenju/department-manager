package vn.edu.vnua.department.department.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.request.CreateDepartmentRequest;
import vn.edu.vnua.department.department.request.GetDepartmentListRequest;
import vn.edu.vnua.department.department.request.UpdateDepartmentRequest;
import vn.edu.vnua.department.faculty.entity.Faculty;

public interface DepartmentService {
    Page<Department> getDepartmentList(GetDepartmentListRequest request);
    Department createDepartment(CreateDepartmentRequest request);
    Department updateDepartment(String id, UpdateDepartmentRequest request);
    Department deleteDepartment(String id);
}
