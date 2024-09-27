package vn.edu.vnua.department.exam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.domain.validation.ImportExamValidator;
import vn.edu.vnua.department.exam.model.ExamExcelData;
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
@Table(name = "exam_assignments")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "class_id")
    private String classId;

    @Column(name = "exam_group")
    private Integer examGroup;

    @ManyToOne
    @JoinColumn(name = "school_year")
    private MasterData schoolYear;

    @Column
    private Byte term;

    @Column
    private Integer cluster;

    @Column
    private Integer quantity;

    @Column(name = "test_day")
    private Timestamp testDay;

    @Column(name = "test_room", length = 200)
    private String testRoom;

    @Column(name = "lesson_start")
    private Integer lessonStart;

    @Column(name = "lessons_test")
    private Integer lessonsTest;

    @ManyToOne
    @JoinColumn(name = "lecturer_teach")
    private User lecturerTeach;

    @ManyToOne
    @JoinColumn(name = "proctor1")
    private User proctor1;

    @ManyToOne
    @JoinColumn(name = "proctor2")
    private User proctor2;

    @ManyToOne
    @JoinColumn(name = "marker1")
    private User marker1;

    @ManyToOne
    @JoinColumn(name = "marker2")
    private User marker2;

    @ManyToOne
    @JoinColumn(name = "form")
    private MasterData form;

    @Column(name = "exam_code")
    private String examCode;

    @ManyToOne
    @JoinColumn(name = "picker")
    private User picker;

    @ManyToOne
    @JoinColumn(name = "printer")
    private User printer;

    @ManyToOne
    @JoinColumn(name = "question_taker")
    private User questionTaker;

    @ManyToOne
    @JoinColumn(name = "exam_taker")
    private User examTaker;

    @ManyToOne
    @JoinColumn(name = "exam_giver")
    private User examGiver;

    @ManyToOne
    @JoinColumn(name = "point_giver")
    private User pointGiver;

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

    public List<ExamExcelData.ErrorDetail> validateInformationDetailError(List<ExamExcelData.ErrorDetail> errorDetailList){
        if (!ImportExamValidator.validateNaturalNum(examGroup)) {
            errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(8).errorMsg("Nhóm không hợp lệ").build());
        }
        if (!ImportExamValidator.validateNaturalNum(term)) {
            errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(1).errorMsg("HK không hợp lệ").build());
        }
        if (!ImportExamValidator.validateNaturalNum(cluster)) {
            errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(9).errorMsg("Tổ không hợp lệ").build());
        }
        if (!ImportExamValidator.validateNaturalNum(quantity)) {
            errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(10).errorMsg("Slg không hợp lệ").build());
        }
        if (!ImportExamValidator.validateNaturalNum(lessonStart)) {
            errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(4).errorMsg("TBĐ không hợp lệ").build());
        }
        if (!ImportExamValidator.validateNaturalNum(lessonsTest)) {
            errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(5).errorMsg("Số tiết không hợp lệ").build());
        }
        if (testDay != null && !ImportExamValidator.validateDob(testDay)) {
            errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(3).errorMsg("Dạng dd/MM/yyyy").build());
        }
        return errorDetailList;
    }
}
