package vn.edu.vnua.department.exam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.user.entity.User;

import java.sql.Timestamp;

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

    @Column(name = "class_id", length = 100)
    private String classId;

    @Column(name = "exam_group")
    private Byte examGroup;

    @Column(name = "school_year", length = 200)
    private String schoolYear;

    @Column
    private Byte term;

    @Column
    private Byte cluster;

    @Column
    private Byte quantity;

    @Column(name = "test_day")
    private Timestamp testDay;

    @Column(name = "test_room", length = 200)
    private String testRoom;

    @Column(name = "lesson_start")
    private Byte lessonStart;

    @Column(name = "lessons_test")
    private Byte lessonsTest;

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

    @ManyToOne
    @JoinColumn(name = "picker")
    private User picker;

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
