package vn.edu.vnua.department.intern.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.domain.validation.ImportInternValidator;
import vn.edu.vnua.department.domain.validation.ImportTeachingValidator;
import vn.edu.vnua.department.intern.model.InternExcelData;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.model.excel.ExcelData;
import vn.edu.vnua.department.student.entity.Student;
import vn.edu.vnua.department.teaching.model.TeachingExcelData;
import vn.edu.vnua.department.user.entity.User;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "intern_topics")
public class Intern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String name;

    @ManyToOne
    @JoinColumn(name = "type")
    private MasterData type;

    @ManyToOne
    @JoinColumn(name = "school_year")
    private MasterData schoolYear;

    @Column
    private Byte term;

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

    @OneToMany(mappedBy = "intern")
    private Collection<Student> students;

    public List<InternExcelData.ErrorDetail> validateInformationDetailError(List<InternExcelData.ErrorDetail> errorDetailList){
        if(!ImportInternValidator.validateInternName(name)) {
            errorDetailList.add(TeachingExcelData.ErrorDetail.builder().columnIndex(2).errorMsg("Tên đề tài không được chứa ký tự đặc biệt").build());
        }
        if (!ImportInternValidator.validateNaturalNum(term)) {
            errorDetailList.add(TeachingExcelData.ErrorDetail.builder().columnIndex(1).errorMsg("Học kỳ không hợp lệ").build());
        }
        return errorDetailList;
    }
}
