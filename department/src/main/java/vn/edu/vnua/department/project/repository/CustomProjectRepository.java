package vn.edu.vnua.department.project.repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.project.request.FilterProjectPage;
import vn.edu.vnua.department.project.request.GetProjectListRequest;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.userjointask.entity.UserTask;
import vn.edu.vnua.department.util.CriteriaBuilderUtil;
import vn.edu.vnua.department.util.MyUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomProjectRepository {
    public static Specification<Project> filterProjectList(GetProjectListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getKeyword())) {
                // Thực hiện join giữa Project và Task
                Join<Project, Task> taskJoin = root.join("tasks", JoinType.LEFT); // Giả sử Project có trường 'tasks' thể hiện mối quan hệ với Task

                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "description", "name")
                );
            }
//            if (StringUtils.hasText(request.getFacultyId())){
//                predicates.add(criteriaBuilder.like(root.get("faculty").get("id"), request.getFacultyId()));
//            }
            query.orderBy(
                    criteriaBuilder.desc(root.get("createdAt"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<Project> filterPage(FilterProjectPage request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join bảng Task từ Project
            Join<Project, Task> taskJoin = root.join("tasks", JoinType.LEFT);

            // Join bảng UserTask từ Task
            Join<Task, UserTask> userTaskJoin = taskJoin.join("userJoined", JoinType.LEFT);

            if (StringUtils.hasText(request.getKeyword())) {
                // Điều kiện tìm kiếm trong bảng Project (name hoặc description)
//                Predicate projectNameOrDescription = criteriaBuilder.or(
//                        criteriaBuilder.like(root.get("name"), "%" + request.getKeyword() + "%"),
//                        criteriaBuilder.like(root.get("description"), "%" + request.getKeyword() + "%")
//                );

                Predicate projectNameOrDescription =
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "description", "name");

                // Điều kiện tìm kiếm trong bảng Task (name hoặc description)
//                Predicate taskNameOrDescription = criteriaBuilder.or(
//                        criteriaBuilder.like(taskJoin.get("name"), "%" + request.getKeyword() + "%"),
//                        criteriaBuilder.like(taskJoin.get("description"), "%" + request.getKeyword() + "%")
//                );

                Predicate taskNameOrDescription =
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root.join("tasks"), criteriaBuilder, request.getKeyword(),
                                "description", "name");

                // Kết hợp các điều kiện lại bằng cách sử dụng OR
                predicates.add(criteriaBuilder.or(projectNameOrDescription, taskNameOrDescription));
            }
            if (StringUtils.hasText(request.getCreatedById())) {
                predicates.add(criteriaBuilder.like(root.get("createdBy").get("id"), request.getCreatedById()));
            }
            if (StringUtils.hasText(request.getStartDate()) &&
                    StringUtils.hasText(request.getEndDate())) {
                Timestamp startDate = null;
                Timestamp endDate = null;
                try {
                    startDate = MyUtils.convertTimestampFromString(request.getStartDate());
                    endDate = MyUtils.convertTimestampFromString(request.getEndDate());
                    if(startDate.after(endDate)){
                        throw new RuntimeException(Constants.ProjectConstant.DATE_BETWEEN_PROBLEM);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                predicates.add(criteriaBuilder.between(root.get("start"), startDate, endDate));
//                predicates.add(criteriaBuilder.between(root.get("deadline"), startDate, endDate));
                predicates.add(criteriaBuilder.or(criteriaBuilder.between(root.get("start"), startDate, endDate),
                        criteriaBuilder.between(root.get("deadline"), startDate, endDate)));
            }
            if (StringUtils.hasText(request.getStatusId())) {
                Predicate projectStatus = criteriaBuilder.like(root.get("projectStatus").get("id"), request.getStatusId());
                Predicate taskStatus = criteriaBuilder.like(root.get("task").get("taskStatus").get("id"), request.getStatusId());
                predicates.add(criteriaBuilder.or(projectStatus, taskStatus));
            }
            if(StringUtils.hasText(request.getMemberId())){
                predicates.add(criteriaBuilder.equal(userTaskJoin.get("user").get("id"), request.getMemberId()));
            }

            query.orderBy(
                    criteriaBuilder.desc(root.get("createdAt"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
