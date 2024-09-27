package vn.edu.vnua.department.internship.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.domain.validation.ImportInternValidator;
import vn.edu.vnua.department.intern.model.InternExcelData;
import vn.edu.vnua.department.internship.model.InternshipExcelData;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.student.entity.Student;
import vn.edu.vnua.department.teaching.model.TeachingExcelData;
import vn.edu.vnua.department.user.entity.User;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "internship")
public class Internship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "school_year")
    private MasterData schoolYear;

    @Column
    private Byte term;

    @Column(name = "student_id", length = 100)
    private String studentId;

    @Column(name = "student_name", length = 200)
    private String studentName;

    @Column(name = "class_id", length = 100)
    private String classId;

    @Column(name = "phone_number",length = 100)
    private String phone;

    @Column(name = "company")
    private String company;

    @Column(length = 200, name = "topic_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "type")
    private MasterData type;

    @ManyToOne
    @JoinColumn(name = "instructor")
    private User instructor;

    @Column(name = "outline_file", length = 1000)
    private String outlineFile;

    @Column(name = "progress_file", length = 1000)
    private String progressFile;

    @Column(name = "final_file", length = 1000)
    private String finalFile;

    @Column(name = "status", length = 200)
    private String status;

    @Column
    private String note;

    @Column(name = "is_lock")
    private Boolean isLock;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    public List<InternExcelData.ErrorDetail> validateInformationDetailError(List<InternExcelData.ErrorDetail> errorDetailList){
        if(!ImportInternValidator.validateInternName(name)) {
            errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(2).errorMsg("Tên đề tài không được chứa ký tự đặc biệt").build());
        }
        if (!ImportInternValidator.validateNaturalNum(term)) {
            errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(1).errorMsg("Học kỳ không hợp lệ").build());
        }
        if(!StringUtils.hasText(studentId)){
            errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(5).errorMsg("Tên SV không được trống").build());
        }
        if(!StringUtils.hasText(studentName)){
            errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(6).errorMsg("Mã SV không được trống").build());
        }
        if(!StringUtils.hasText(classId)){
            errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(7).errorMsg("Mã lớp không được trống").build());
        }
        return errorDetailList;
    }
}
