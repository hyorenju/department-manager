package vn.edu.vnua.department.userjointask.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.task.entity.TaskDTO;
import vn.edu.vnua.department.user.entity.UserBasicDTO;

import java.sql.Timestamp;

@Data
public class UserTaskDTO {
    private Long id;
    private UserBasicDTO user;
    private TaskDTO task;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp finishedAt;
    private MasterDataDTO taskStatus;
    private String note;
}
