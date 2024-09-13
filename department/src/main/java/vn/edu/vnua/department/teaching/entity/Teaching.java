package vn.edu.vnua.department.teaching.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.domain.validation.ImportTeachingValidator;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.teaching.model.TeachingExcelData;
import vn.edu.vnua.department.user.entity.User;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "teaching_assignments")
public class Teaching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher")
    private User teacher;

    @Column(name = "class_id", length = 200)
    private String classId;

    @Column(name = "teaching_group", length = 200)
    private Integer teachingGroup;

    @ManyToOne
    @JoinColumn(name = "school_year")
    private MasterData schoolYear;

    @Column
    private Byte term;

    @Column(name = "component_file", length = 1000)
    private String componentFile;

    @Column(name = "summary_file", length = 1000)
    private String summaryFile;

    @Column(name = "status", length = 200)
    private String status;

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

    @Column
    private String note;

    @Column(name = "is_lock")
    private Boolean isLock;

    public List<TeachingExcelData.ErrorDetail> validateInformationDetailError(List<TeachingExcelData.ErrorDetail> errorDetailList){
        if (!ImportTeachingValidator.validateNaturalNum(term)) {
            errorDetailList.add(TeachingExcelData.ErrorDetail.builder().columnIndex(1).errorMsg("Học kỳ không hợp lệ").build());
        }
        if (!ImportTeachingValidator.validateNaturalNum(teachingGroup)) {
            errorDetailList.add(TeachingExcelData.ErrorDetail.builder().columnIndex(4).errorMsg("NMH không hợp lệ").build());
        }
        return errorDetailList;
    }
}
