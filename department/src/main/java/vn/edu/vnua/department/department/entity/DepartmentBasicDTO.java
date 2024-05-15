package vn.edu.vnua.department.department.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.faculty.entity.FacultyBasicDTO;
import vn.edu.vnua.department.faculty.entity.FacultyDTO;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.sql.Timestamp;

@Data
public class DepartmentBasicDTO {
    private String id;
    private String name;
    private FacultyBasicDTO faculty;
}
