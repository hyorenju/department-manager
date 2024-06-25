package vn.edu.vnua.department.teaching.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.teaching.request.ExportTeachingRequest;
import vn.edu.vnua.department.teaching.request.GetTeachingListRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomTeachingRepository {
    public static Specification<Teaching> filterTeachingList(GetTeachingListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getSchoolYear()!=null) {
                predicates.add(criteriaBuilder.equal(root.get("schoolYear").get("id"), request.getSchoolYear()));
            }
            if (request.getTerm() != null) {
                predicates.add(criteriaBuilder.equal(root.get("term"), request.getTerm()));
            }
            if(StringUtils.hasText(request.getDepartmentId())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("id"), request.getDepartmentId()));
            } else if(StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if(StringUtils.hasText(request.getSubjectId())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("id"), "%" + request.getSubjectId() + "%"));
            }
            if(StringUtils.hasText(request.getSubjectName())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("name"), "%" + request.getSubjectName() + "%"));
            }
            if(StringUtils.hasText(request.getTeacherId())){
                predicates.add(criteriaBuilder.like(root.get("teacher").get("id"), request.getTeacherId()));
            }
            if(StringUtils.hasText(request.getClassId())){
                predicates.add(criteriaBuilder.like(root.get("classId"), "%" + request.getClassId() + "%"));
            }
            if (StringUtils.hasText(request.getStatus())){
                predicates.add(criteriaBuilder.like(root.get("status"), request.getStatus()));
            }
            query.orderBy(
                    criteriaBuilder.desc(root.get("schoolYear").get("name")),
                    criteriaBuilder.desc(root.get("term")),
                    criteriaBuilder.asc(root.get("teacher").get("department").get("faculty").get("name")),
                    criteriaBuilder.asc(root.get("teacher").get("department").get("name")),
                    criteriaBuilder.asc(root.get("subject").get("name")),
                    criteriaBuilder.asc(root.get("teacher").get("lastName")),
                    criteriaBuilder.asc(root.get("status"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<Teaching> filterExportTeaching(ExportTeachingRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getSchoolYear()!=null) {
                predicates.add(criteriaBuilder.equal(root.get("schoolYear").get("id"), request.getSchoolYear()));
            }
            if (request.getTerm() != null) {
                predicates.add(criteriaBuilder.equal(root.get("term"), request.getTerm()));
            }
            if(StringUtils.hasText(request.getDepartmentId())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("id"), request.getDepartmentId()));
            } else if(StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if(StringUtils.hasText(request.getSubjectName())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("name"), "%" + request.getSubjectName() + "%"));
            }
            if(StringUtils.hasText(request.getTeacherId())){
                predicates.add(criteriaBuilder.like(root.get("teacher").get("id"), request.getTeacherId()));
            }
            if(StringUtils.hasText(request.getClassId())){
                predicates.add(criteriaBuilder.like(root.get("classId"), "%" + request.getClassId() + "%"));
            }
            if (StringUtils.hasText(request.getStatus())){
                predicates.add(criteriaBuilder.like(root.get("status"), request.getStatus()));
            }
            query.orderBy(
                    criteriaBuilder.desc(root.get("schoolYear").get("name")),
                    criteriaBuilder.desc(root.get("term")),
                    criteriaBuilder.asc(root.get("teacher").get("department").get("faculty").get("name")),
                    criteriaBuilder.asc(root.get("teacher").get("department").get("name")),
                    criteriaBuilder.asc(root.get("subject").get("name")),
                    criteriaBuilder.asc(root.get("teacher").get("lastName")),
                    criteriaBuilder.asc(root.get("status"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
