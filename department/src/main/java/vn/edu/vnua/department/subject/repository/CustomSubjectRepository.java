package vn.edu.vnua.department.subject.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.request.GetClassListRequest;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.request.ExportSubjectListRequest;
import vn.edu.vnua.department.subject.request.GetSubjectListRequest;
import vn.edu.vnua.department.util.CriteriaBuilderUtil;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomSubjectRepository {
    public static Specification<Subject> filterSubjectList(GetSubjectListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getKeyword())) {
                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "id", "name")
                );
            }
            if(StringUtils.hasText(request.getFacultyId())) {
                predicates.add(criteriaBuilder.equal(root.get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if(StringUtils.hasText(request.getDepartmentId())) {
                predicates.add(criteriaBuilder.equal(root.get("department").get("id"), request.getDepartmentId()));
            }
            query.orderBy(
                    criteriaBuilder.desc(root.get("createdAt"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }


    public static Specification<Subject> filterExportSubject(ExportSubjectListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getKeyword())) {
                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "id", "name")
                );
            }
            if(StringUtils.hasText(request.getFacultyId())) {
                predicates.add(criteriaBuilder.equal(root.get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if(StringUtils.hasText(request.getDepartmentId())) {
                predicates.add(criteriaBuilder.equal(root.get("department").get("id"), request.getDepartmentId()));
            }
            query.orderBy(
                    criteriaBuilder.asc(root.get("department").get("faculty").get("name")),
                    criteriaBuilder.asc(root.get("department").get("name")),
                    criteriaBuilder.asc(root.get("id"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}