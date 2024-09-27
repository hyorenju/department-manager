package vn.edu.vnua.department.subject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.domain.validation.ImportSubjectValidator;
import vn.edu.vnua.department.domain.validation.ImportSubjectValidator;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.model.excel.ExcelData;
import vn.edu.vnua.department.student.entity.Student;
import vn.edu.vnua.department.subject.model.SubjectExcelData;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.user.entity.User;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @Column(length = 100)
    private String id;

    @Column(length = 200)
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column
    private Integer credits;

    @Column(length = 1000)
    private String outline;

    @Column(length = 1000)
    private String lecture;

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

    public List<SubjectExcelData.ErrorDetail> validateInformationDetailError(List<SubjectExcelData.ErrorDetail> errorDetailList){
        if(!ImportSubjectValidator.validateSubjectId(id)) {
            errorDetailList.add(SubjectExcelData.ErrorDetail.builder().columnIndex(0).errorMsg("Mã môn học chỉ được chứa ký tự chữ và số").build());
        }
        if(!ImportSubjectValidator.validateSubjectName(name)) {
            errorDetailList.add(SubjectExcelData.ErrorDetail.builder().columnIndex(1).errorMsg("Tên môn học không được chứa ký tự đặc biệt").build());
        }
        if(!ImportSubjectValidator.validateSubjectCredits(credits)) {
            errorDetailList.add(SubjectExcelData.ErrorDetail.builder().columnIndex(3).errorMsg("Số TC không hợp lệ").build());
        }
        return errorDetailList;
    }

    @OneToMany(mappedBy = "subject")
    private Collection<Teaching> teachingAssignments;

    @OneToMany(mappedBy = "subject")
    private Collection<Exam> examAssignments;
}
