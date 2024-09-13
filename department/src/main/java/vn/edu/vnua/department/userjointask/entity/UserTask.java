package vn.edu.vnua.department.userjointask.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.project.entity.Project;
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
@Table(name = "users_tasks")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "finished_at")
    private Timestamp finishedAt;

    @ManyToOne
    @JoinColumn(name = "task_status")
    private MasterData taskStatus;

    @Column(name = "note")
    private String note;


}
