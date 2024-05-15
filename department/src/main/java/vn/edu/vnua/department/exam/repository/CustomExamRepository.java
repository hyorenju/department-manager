package vn.edu.vnua.department.exam.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.exam.request.ExportExamRequest;
import vn.edu.vnua.department.exam.request.GetExamListRequest;
import vn.edu.vnua.department.util.MyUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomExamRepository {
    public static Specification<Exam> filterExamList(GetExamListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(request.getDepartmentId())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("id"), request.getDepartmentId()));
            } else if(StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if(StringUtils.hasText(request.getSubjectName())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("name"), "%" + request.getSubjectName() + "%"));
            }
            if (request.getSchoolYear()!=null) {
                predicates.add(criteriaBuilder.equal(root.get("schoolYear").get("id"), request.getSchoolYear()));
            }
            if (request.getTerm() != null) {
                predicates.add(criteriaBuilder.equal(root.get("term"), request.getTerm()));
            }
            if (request.getFormId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("form").get("id"), request.getFormId()));
            }
            if (StringUtils.hasText(request.getTestDay())){
                try {
                    predicates.add(criteriaBuilder.equal(root.get("testDay"), MyUtils.convertTimestampFromString(request.getTestDay())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(StringUtils.hasText(request.getProctor1Id())){
                predicates.add(criteriaBuilder.like(root.get("proctor1").get("id"), request.getProctor1Id()));
            }
            if(StringUtils.hasText(request.getProctor2Id())){
                predicates.add(criteriaBuilder.like(root.get("proctor2").get("id"), request.getProctor2Id()));
            }
            if(StringUtils.hasText(request.getClassId())){
                predicates.add(criteriaBuilder.like(root.get("classId"), request.getClassId() + "%"));
            }

            query.orderBy(
                    criteriaBuilder.desc(root.get("schoolYear").get("name")),
                    criteriaBuilder.asc(root.get("term")),
                    criteriaBuilder.asc(root.get("subject").get("department").get("faculty").get("name")),
                    criteriaBuilder.asc(root.get("subject").get("department").get("name")),
                    criteriaBuilder.asc(root.get("testDay")),
                    criteriaBuilder.asc(root.get("lessonStart")),
                    criteriaBuilder.asc(root.get("subject").get("name"))
            );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<Exam> filterExportExam(ExportExamRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(request.getDepartmentId())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("id"), request.getDepartmentId()));
            } else if(StringUtils.hasText(request.getFacultyId())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if(StringUtils.hasText(request.getSubjectName())){
                predicates.add(criteriaBuilder.like(root.get("subject").get("name"), "%" + request.getSubjectName() + "%"));
            }
            if (request.getSchoolYear()!=null) {
                predicates.add(criteriaBuilder.equal(root.get("schoolYear").get("id"), request.getSchoolYear()));
            }
            if (request.getTerm() != null) {
                predicates.add(criteriaBuilder.equal(root.get("term"), request.getTerm()));
            }
            if (request.getFormId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("form").get("id"), request.getFormId()));
            }
            if (StringUtils.hasText(request.getTestDay())){
                try {
                    predicates.add(criteriaBuilder.equal(root.get("testDay"), MyUtils.convertTimestampFromString(request.getTestDay())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(StringUtils.hasText(request.getProctor1Id())){
                predicates.add(criteriaBuilder.like(root.get("proctor1").get("id"), request.getProctor1Id()));
            }
            if(StringUtils.hasText(request.getProctor2Id())){
                predicates.add(criteriaBuilder.like(root.get("proctor2").get("id"), request.getProctor2Id()));
            }
            if(StringUtils.hasText(request.getClassId())){
                predicates.add(criteriaBuilder.like(root.get("classId"), request.getClassId() + "%"));
            }

            query.orderBy(
                    criteriaBuilder.desc(root.get("schoolYear").get("name")),
                    criteriaBuilder.asc(root.get("term")),
                    criteriaBuilder.asc(root.get("subject").get("department").get("faculty").get("name")),
                    criteriaBuilder.asc(root.get("subject").get("department").get("name")),
                    criteriaBuilder.asc(root.get("testDay")),
                    criteriaBuilder.asc(root.get("lessonStart")),
                    criteriaBuilder.asc(root.get("subject").get("name"))
            );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
