package vn.edu.vnua.department.task.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.task.request.GetTaskListRequest;
import vn.edu.vnua.department.util.CriteriaBuilderUtil;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomTaskRepository {
    public static Specification<Task> filterTaskList(GetTaskListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getKeyword())) {
                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "description", "name")
                );
            }
            if (request.getProjectId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("project").get("id"), request.getProjectId()));
            }
            query.orderBy(
                    criteriaBuilder.asc(root.get("ordinalNumber"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
