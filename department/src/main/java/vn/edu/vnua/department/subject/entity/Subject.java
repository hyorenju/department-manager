package vn.edu.vnua.department.subject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.student.entity.Student;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.user.entity.User;

import java.sql.Timestamp;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String name;

    @Column
    private Byte credits;

    @Column(length = 1000)
    private String outline;

    @Column(length = 1000)
    private String lecture;

    @Column(length = 1000)
    private String curriculum;

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

    @OneToMany(mappedBy = "subject")
    private Collection<Teaching> teachingAssignments;

    @OneToMany(mappedBy = "subject")
    private Collection<Exam> examAssignments;
}
