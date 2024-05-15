package vn.edu.vnua.department.subject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.DepartmentBasicDTO;
import vn.edu.vnua.department.department.entity.DepartmentDTO;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.entity.UserBasicDTO;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.sql.Timestamp;

@Data
public class SubjectDTO {
    private String id;
    private String name;
    private DepartmentBasicDTO department;
    private Integer credits;
    private String outline;
    private String lecture;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp createdAt;
    private UserBasicDTO createdBy;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp modifiedAt;
    private UserBasicDTO modifiedBy;
    private String note;
}
