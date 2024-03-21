package vn.edu.vnua.department.department.entity;

import lombok.Data;
import vn.edu.vnua.department.faculty.entity.FacultyDTO;

@Data
public class DepartmentDTO {
    private String id;
    private String name;
    private FacultyDTO faculty;
}
