package vn.edu.vnua.department.internship.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.intern.request.GetInternListRequest;
import vn.edu.vnua.department.intern.request.LockInternListRequest;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.internship.request.GetInternshipListRequest;
import vn.edu.vnua.department.internship.request.LockInternshipListRequest;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomInternshipRepository {
    public static Specification<Internship> filterInternshipList(GetInternshipListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getSchoolYear()!=null) {
                predicates.add(criteriaBuilder.equal(root.get("schoolYear").get("id"), request.getSchoolYear()));
            }
            if (request.getTerm() != null) {
                predicates.add(criteriaBuilder.equal(root.get("term"), request.getTerm()));
            }
            if (StringUtils.hasText(request.getStudentId())){
                predicates.add(criteriaBuilder.like(root.get("studentId"), "%" + request.getStudentId() + "%"));
            }
            if (StringUtils.hasText(request.getStudentName())){
                predicates.add(criteriaBuilder.like(root.get("studentName"), "%" + request.getStudentName() + "%"));
            }
            if (StringUtils.hasText(request.getClassId())){
                predicates.add(criteriaBuilder.like(root.get("classId"), "%" + request.getClassId() + "%"));
            }
            if (StringUtils.hasText(request.getCompany())){
                predicates.add(criteriaBuilder.like(root.get("company"), "%" + request.getCompany() + "%"));
            }
            if (StringUtils.hasText(request.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            if (StringUtils.hasText(request.getInstructorId())){
                predicates.add(criteriaBuilder.like(root.get("instructor").get("id"), request.getInstructorId()));
            }
            if (request.getTypeId() != null){
                predicates.add(criteriaBuilder.equal(root.get("type").get("id"), request.getTypeId()));
            }
            if (StringUtils.hasText(request.getStatus())){
                predicates.add(criteriaBuilder.like(root.get("status"), request.getStatus()));
            }
            if(StringUtils.hasText(request.getDepartmentId())){
                predicates.add(criteriaBuilder.like(root.get("instructor").get("department").get("id"), request.getDepartmentId()));
            } else if (StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("instructor").get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if (StringUtils.hasText(request.getNote())){
                predicates.add(criteriaBuilder.like(root.get("note"), "%" + request.getNote() + "%"));
            }

            query.orderBy(
                    criteriaBuilder.desc(root.get("createdAt")),
                    criteriaBuilder.desc(root.get("schoolYear").get("name")),
                    criteriaBuilder.desc(root.get("term")),
                    criteriaBuilder.asc(root.get("instructor").get("department").get("faculty").get("name")),
                    criteriaBuilder.asc(root.get("instructor").get("department").get("name")),
                    criteriaBuilder.asc(root.get("instructor").get("id")),
                    criteriaBuilder.asc(root.get("name"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<Internship> filterExportInternship(ExportInternListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getSchoolYear()!=null) {
                predicates.add(criteriaBuilder.equal(root.get("schoolYear").get("id"), request.getSchoolYear()));
            }
            if (request.getTerm() != null) {
                predicates.add(criteriaBuilder.equal(root.get("term"), request.getTerm()));
            }
            if (StringUtils.hasText(request.getStudentId())){
                predicates.add(criteriaBuilder.like(root.get("studentId"), "%" + request.getStudentId() + "%"));
            }
            if (StringUtils.hasText(request.getStudentName())){
                predicates.add(criteriaBuilder.like(root.get("studentName"), "%" + request.getStudentName() + "%"));
            }
            if (StringUtils.hasText(request.getClassId())){
                predicates.add(criteriaBuilder.like(root.get("classId"), "%" + request.getClassId() + "%"));
            }
            if (StringUtils.hasText(request.getCompany())){
                predicates.add(criteriaBuilder.like(root.get("company"), "%" + request.getCompany() + "%"));
            }
            if (StringUtils.hasText(request.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            if (StringUtils.hasText(request.getInstructorId())){
                predicates.add(criteriaBuilder.like(root.get("instructor").get("id"), request.getInstructorId()));
            }
            if (request.getTypeId() != null){
                predicates.add(criteriaBuilder.equal(root.get("type").get("id"), request.getTypeId()));
            }
            if (StringUtils.hasText(request.getStatus())){
                predicates.add(criteriaBuilder.like(root.get("status"), request.getStatus()));
            }
            if(StringUtils.hasText(request.getDepartmentId())){
                predicates.add(criteriaBuilder.like(root.get("instructor").get("department").get("id"), request.getDepartmentId()));
            } else if (StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("instructor").get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if (StringUtils.hasText(request.getNote())){
                predicates.add(criteriaBuilder.like(root.get("note"), "%" + request.getNote() + "%"));
            }

            query.orderBy(
                    criteriaBuilder.desc(root.get("schoolYear").get("name")),
                    criteriaBuilder.desc(root.get("term")),
                    criteriaBuilder.asc(root.get("instructor").get("department").get("faculty").get("name")),
                    criteriaBuilder.asc(root.get("instructor").get("department").get("name")),
                    criteriaBuilder.asc(root.get("instructor").get("id")),
                    criteriaBuilder.asc(root.get("name"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<Internship> filterLockInternshipList(LockInternshipListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getWantLock()){
                predicates.add(criteriaBuilder.isFalse(root.get("isLock")));
            } else if(!request.getWantLock()){
                predicates.add(criteriaBuilder.isTrue(root.get("isLock")));
            }
            if(request.getSchoolYearId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("schoolYear").get("id"), request.getSchoolYearId()));
            }
            if(request.getTerm()!=null){
                predicates.add(criteriaBuilder.equal(root.get("term"), request.getTerm()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
