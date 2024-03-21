package vn.edu.vnua.department.department.repository;

import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.request.GetDepartmentListRequest;
import vn.edu.vnua.department.util.CriteriaBuilderUtil;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomDepartmentRepository {
    public static Specification<Department> filterDepartmentList(GetDepartmentListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!Strings.isEmpty(request.getKeyword())) {
                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "id", "name")
                );
            }
            if (!StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("faculty").get("id"), request.getFacultyId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
