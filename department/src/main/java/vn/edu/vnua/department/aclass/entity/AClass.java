package vn.edu.vnua.department.aclass.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.user.entity.User;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "classes")
public class AClass {
    @Id
    @Column(length = 100)
    private String id;

    @Column(length = 200)
    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @Column(name = "homeroom_teacher", length = 200)
    private String hrTeacher;

    @Column(name = "monitor", length = 200)
    private String monitor;

    @Column(name = "monitor_phone", length = 200)
    private String monitorPhone;

    @Column(name = "monitor_email", length = 200)
    private String monitorEmail;

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
