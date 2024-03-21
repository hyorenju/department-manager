package vn.edu.vnua.department.user.repository;

import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.request.GetUserListRequest;
import vn.edu.vnua.department.util.CriteriaBuilderUtil;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomUserRepository {
    public static Specification<User> filterUserList(GetUserListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!Strings.isEmpty(request.getKeyword())) {
                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "id", "firstName", "lastName")
                );
            }
            if (!StringUtils.hasText(request.getDegree())){
                predicates.add(criteriaBuilder.like(root.get("degree"), request.getDegree() + "%"));
            }
            if (!StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if (!StringUtils.hasText(request.getDepartmentId())){
                predicates.add(criteriaBuilder.like(root.get("department").get("id"), request.getDepartmentId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}