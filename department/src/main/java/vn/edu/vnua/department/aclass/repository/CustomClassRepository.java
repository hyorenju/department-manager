package vn.edu.vnua.department.aclass.repository;

import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.request.ExportClassListRequest;
import vn.edu.vnua.department.aclass.request.GetClassListRequest;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.request.GetDepartmentListRequest;
import vn.edu.vnua.department.util.CriteriaBuilderUtil;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomClassRepository {
    public static Specification<AClass> filterClassList(GetClassListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getKeyword())) {
                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "id", "name")
                );
            }
            if (StringUtils.hasText(request.getId())){
                predicates.add(criteriaBuilder.like(root.get("id"), "%" + request.getId() + "%"));
            }
            if (StringUtils.hasText(request.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            if (StringUtils.hasText(request.getHrTeacher())){
                predicates.add(criteriaBuilder.like(root.get("hrTeacher"), "%" + request.getHrTeacher() + "%"));
            }
            if (StringUtils.hasText(request.getMonitor())){
                predicates.add(criteriaBuilder.like(root.get("monitor"), "%" + request.getMonitor() + "%"));
            }
            if (StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("faculty").get("id"), "%" + request.getFacultyId() + "%"));
            }
            query.orderBy(
                    criteriaBuilder.desc(root.get("createdAt"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<AClass> filterExportClass(ExportClassListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getKeyword())) {
                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
                                "id", "name")
                );
            }
            if (StringUtils.hasText(request.getId())){
                predicates.add(criteriaBuilder.like(root.get("id"), "%" + request.getId() + "%"));
            }
            if (StringUtils.hasText(request.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            if (StringUtils.hasText(request.getHrTeacher())){
                predicates.add(criteriaBuilder.like(root.get("hrTeacher"), "%" + request.getHrTeacher() + "%"));
            }
            if (StringUtils.hasText(request.getMonitor())){
                predicates.add(criteriaBuilder.like(root.get("monitor"), "%" + request.getMonitor() + "%"));
            }
            if (StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("faculty").get("id"), "%" + request.getFacultyId() + "%"));
            }
            query.orderBy(
                    criteriaBuilder.asc(root.get("id"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}