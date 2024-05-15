package vn.edu.vnua.department.aclass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.faculty.entity.FacultyBasicDTO;
import vn.edu.vnua.department.faculty.entity.FacultyDTO;
import vn.edu.vnua.department.user.entity.UserBasicDTO;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.sql.Timestamp;

@Data
public class ClassDTO {
    private String id;
    private String name;
    private FacultyBasicDTO faculty;
    private String hrTeacher;
    private String monitor;
    private String monitorPhone;
    private String monitorEmail;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp createdAt;
    private UserBasicDTO createdBy;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp modifiedAt;
    private UserBasicDTO modifiedBy;
    private String note;
}
