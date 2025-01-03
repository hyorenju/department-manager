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

            query.distinct(true);

            Join<Project, Task> taskJoin = root.join("tasks", JoinType.LEFT);
            Join<Task, UserTask> userTaskJoin = taskJoin.join("userJoined", JoinType.LEFT);

            if (StringUtils.hasText(request.getKeyword())) {
                Predicate projectNameOrDescription =
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "description", "name");
                Predicate taskNameOrDescription =
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root.join("tasks"), criteriaBuilder, request.getKeyword(),
                                "description", "name");
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
                    if (startDate.after(endDate)) {
                        throw new RuntimeException(Constants.ProjectConstant.DATE_BETWEEN_PROBLEM);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                predicates.add(criteriaBuilder.or(criteriaBuilder.between(root.get("start"), startDate, endDate),
                        criteriaBuilder.between(root.get("deadline"), startDate, endDate)));
            }
            if (StringUtils.hasText(request.getStatusId())) {
                predicates.add(criteriaBuilder.like(root.get("projectStatus").get("id"), request.getStatusId()));
            }
            if (StringUtils.hasText(request.getMemberId())) {
                predicates.add(criteriaBuilder.equal(userTaskJoin.get("user").get("id"), request.getMemberId()));
            }

            if (request.getProjectType() != null) {
                if (request.getProjectType()) {
                    predicates.add(criteriaBuilder.isTrue(root.get("isPrivate")));
                } else {
                    predicates.add(criteriaBuilder.isFalse(root.get("isPrivate")));
                }
            }

//            predicates.add(criteriaBuilder.and(
//                    criteriaBuilder.like(root.get("createdBy").get("department").get("id"), request.getGetter().getDepartment().getId()),
//                    criteriaBuilder.or(
//                            criteriaBuilder.isFalse(root.get("isPrivate")),
//
//                            criteriaBuilder.and(
//                                    criteriaBuilder.isTrue(root.get("isPrivate")),
//                                    criteriaBuilder.like(root.get("createdBy").get("id"), request.getGetter().getId())
//                            )
//                    )
//            ));

            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.isFalse(root.get("isPrivate")),
                    criteriaBuilder.and(
                            criteriaBuilder.isTrue(root.get("isPrivate")),
                            criteriaBuilder.like(root.get("createdBy").get("id"), request.getGetter().getId())
                    )
            ));


            predicates.add(criteriaBuilder.like(root.get("createdBy").get("department").get("id"), request.getGetter().getDepartment().getId()));

            query.orderBy(
                    criteriaBuilder.desc(root.get("createdAt"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
