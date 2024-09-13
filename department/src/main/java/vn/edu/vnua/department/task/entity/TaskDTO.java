package vn.edu.vnua.department.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.project.entity.ProjectDTO;

import java.sql.Timestamp;

@Data
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp createdAt;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp start;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp deadline;
    private Integer ordinalNumber;
    private ProjectDTO project;
}
