package vn.edu.vnua.department.internship.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.user.entity.UserDTO;

import java.sql.Timestamp;

@Data
public class InternshipDTO {
    private Long id;
    private MasterDataDTO schoolYear;
    private Byte term;
    private String studentId;
    private String studentName;
    private String classId;
    private String phone;
    private String company;
    private String name;
    private MasterDataDTO type;
    private UserDTO instructor;
    private String outlineFile;
    private String progressFile;
    private String finalFile;
    private String status;
    private String note;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp createdAt;
    private UserDTO createdBy;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp modifiedAt;
    private UserDTO modifiedBy;
    private Boolean isLock;
}
