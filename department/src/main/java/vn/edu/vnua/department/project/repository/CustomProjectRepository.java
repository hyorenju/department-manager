package vn.edu.vnua.department.project.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.request.GetClassListRequest;
import vn.edu.vnua.department.project.entity.Project;
import vn.edu.vnua.department.project.request.GetProjectListRequest;
import vn.edu.vnua.department.util.CriteriaBuilderUtil;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomProjectRepository {
    public static Specification<Project> filterProjectList(GetProjectListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getKeyword())) {
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
}
