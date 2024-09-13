package vn.edu.vnua.department.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.userjointask.entity.UserTask;

import java.sql.Timestamp;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "start")
    private Timestamp start;

    @Column(name = "deadline")
    private Timestamp deadline;

    @Column(name = "ordinal_number")
    private Integer ordinalNumber;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "task")
    private Collection<UserTask> userJoined;
}
