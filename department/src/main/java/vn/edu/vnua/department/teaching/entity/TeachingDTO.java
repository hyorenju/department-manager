package vn.edu.vnua.department.teaching.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.entity.SubjectBasicDTO;
import vn.edu.vnua.department.subject.entity.SubjectDTO;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.entity.UserBasicDTO;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.sql.Timestamp;
@Data
public class TeachingDTO {
    private Long id;
    private SubjectDTO subject;
    private UserBasicDTO teacher;
    private String classId;
    private Integer teachingGroup;
    private MasterDataDTO schoolYear;
    private Byte term;
    private String componentFile;
    private String summaryFile;
    private String status;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp createdAt;
    private UserBasicDTO createdBy;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp modifiedAt;
    private UserBasicDTO modifiedBy;
    private String note;
}
