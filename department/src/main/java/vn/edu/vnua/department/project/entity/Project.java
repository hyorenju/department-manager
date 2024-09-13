package vn.edu.vnua.department.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.user.entity.User;

import java.sql.Timestamp;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String name;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "start")
    private Timestamp start;

    @Column(name = "deadline")
    private Timestamp deadline;

    @OneToMany(mappedBy = "project")
    private Collection<Task> tasks;


}
