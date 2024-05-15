package vn.edu.vnua.department.faculty.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.sql.Timestamp;

@Data
public class FacultyBasicDTO {
    private String id;
    private String name;
}
