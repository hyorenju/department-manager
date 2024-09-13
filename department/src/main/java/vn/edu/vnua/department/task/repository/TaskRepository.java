package vn.edu.vnua.department.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.task.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Task findByProjectAndOrdinalNumber(Project project, Integer ordinalNumber);
}
