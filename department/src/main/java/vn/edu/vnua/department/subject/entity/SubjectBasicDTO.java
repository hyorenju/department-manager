package vn.edu.vnua.department.subject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.DepartmentDTO;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.sql.Timestamp;

@Data
public class SubjectBasicDTO {
    private String id;
    private String name;
}
