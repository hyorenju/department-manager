package vn.edu.vnua.department.user.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.request.ExportUserListRequest;
import vn.edu.vnua.department.user.request.GetUserListRequest;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomUserRepository {
    public static Specification<User> filterUserList(GetUserListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getId())){
                predicates.add(criteriaBuilder.like(root.get("id"), "%" + request.getId() + "%"));
            }

            if (StringUtils.hasText(request.getFirstName())){
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + request.getFirstName() + "%"));
            }

            if (StringUtils.hasText(request.getLastName())){
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + request.getLastName() + "%"));
            }

            if (StringUtils.hasText(request.getDegreeId())){
                predicates.add(criteriaBuilder.like(root.get("degree").get("id"), request.getDegreeId()));
            }

            if (StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("department").get("faculty").get("id"), request.getFacultyId()));
            }

            if (StringUtils.hasText(request.getDepartmentId())){
                predicates.add(criteriaBuilder.like(root.get("department").get("id"), request.getDepartmentId()));
            }

            if (StringUtils.hasText(request.getRoleId())){
                predicates.add(criteriaBuilder.like(root.get("role").get("id"), request.getRoleId()));
            }

            query.orderBy(
                    criteriaBuilder.desc(root.get("createdAt"))
            );
            predicates.add(criteriaBuilder.notLike(root.get("id"), Constants.DevAccountConstant.DEV));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<User> filterExportUser(ExportUserListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getId())){
                predicates.add(criteriaBuilder.like(root.get("id"), request.getId() + "%"));
            }

            if (StringUtils.hasText(request.getFirstName())){
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + request.getFirstName() + "%"));
            }

            if (StringUtils.hasText(request.getLastName())){
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + request.getLastName() + "%"));
            }

            if (StringUtils.hasText(request.getDegreeId())){
                predicates.add(criteriaBuilder.like(root.get("degree").get("id"), request.getDegreeId()));
            }

            if (StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("department").get("faculty").get("id"), request.getFacultyId()));
            }

            if (StringUtils.hasText(request.getDepartmentId())){
                predicates.add(criteriaBuilder.like(root.get("department").get("id"), request.getDepartmentId()));
            }

            if (StringUtils.hasText(request.getRoleId())){
                predicates.add(criteriaBuilder.like(root.get("role").get("id"), request.getRoleId()));
            }

            query.orderBy(
                    criteriaBuilder.asc(root.get("lastName")),
                    criteriaBuilder.asc(root.get("firstName")),
                    criteriaBuilder.asc(root.get("department").get("faculty").get("name")),
                    criteriaBuilder.asc(root.get("department").get("name"))
            );
            predicates.add(criteriaBuilder.notLike(root.get("id"), Constants.DevAccountConstant.DEV));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}