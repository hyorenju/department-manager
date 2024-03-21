package vn.edu.vnua.department.teaching.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.user.entity.User;

import java.sql.Timestamp;

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
    @JoinColumn(name = "hr_teacher")
    private User hrTeacher;

    @Column(name = "class_id", length = 100)
    private String classId;

    @Column(name = "teaching_group", length = 200)
    private String teachingGroup;

    @Column(name = "school_year", length = 200)
    private String schoolYear;

    @Column
    private Byte term;

    @Column(name = "component_file", length = 1000)
    private String componentFile;

    @Column(name = "summary_file", length = 1000)
    private String summaryFile;

    @ManyToOne
    @JoinColumn(name = "status")
    private MasterData status;

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
}
