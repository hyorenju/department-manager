package vn.edu.vnua.department.userjointask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.userjointask.entity.UserTask;

import java.util.List;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long>, JpaSpecificationExecutor<UserTask> {
    List<UserTask> findAllByTask(Task task);
}
