package vn.edu.vnua.department.faculty.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.user.entity.User;

import java.sql.Timestamp;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "faculties")
public class Faculty {
    @Id
    @Column(length = 100)
    private String id;

    @Column(length = 200)
    private String name;

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

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private Collection<Department> departments;
}
