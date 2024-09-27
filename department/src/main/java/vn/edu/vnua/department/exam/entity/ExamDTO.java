package vn.edu.vnua.department.exam.entity;

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
public class ExamDTO {
    private Long id;
    private SubjectDTO subject;
    private String classId;
    private Integer examGroup;
    private MasterDataDTO schoolYear;
    private Byte term;
    private Integer cluster;
    private Integer quantity;
    @JsonFormat(pattern = Constants.DateTimeConstants.YEAR_MONTH_DAY_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp testDay;
    private String testRoom;
    private Integer lessonStart;
    private Integer lessonsTest;
    private UserBasicDTO lecturerTeach;
    private UserBasicDTO proctor1;
    private UserBasicDTO proctor2;
    private UserBasicDTO marker1;
    private UserBasicDTO marker2;
    private MasterDataDTO form;
    private String examCode;
    private UserBasicDTO picker;
    private UserBasicDTO printer;
    private UserBasicDTO questionTaker;
    private UserBasicDTO examTaker;
    private UserBasicDTO examGiver;
    private UserBasicDTO pointGiver;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp createdAt;
    private UserBasicDTO createdBy;
    @JsonFormat(pattern = Constants.DateTimeConstants.DATE_TIME_FORMAT, timezone = Constants.DateTimeConstants.TIME_ZONE)
    private Timestamp modifiedAt;
    private UserBasicDTO modifiedBy;
    private String note;
}
