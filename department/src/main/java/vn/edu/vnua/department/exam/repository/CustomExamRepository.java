package vn.edu.vnua.department.exam.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.exam.request.ExportExamRequest;
import vn.edu.vnua.department.exam.request.GetExamListRequest;
import vn.edu.vnua.department.util.CriteriaBuilderUtil;
import vn.edu.vnua.department.util.MyUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomExamRepository {
    public static Specification<Exam> filterExamList(GetExamListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getDepartmentId())) {
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("id"), request.getDepartmentId()));
            } else if (StringUtils.hasText(request.getFacultyId())) {
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if (StringUtils.hasText(request.getSubjectId())) {
                predicates.add(criteriaBuilder.like(root.get("subject").get("id"), "%" + request.getSubjectId() + "%"));
            }
            if (StringUtils.hasText(request.getSubjectName())) {
                predicates.add(criteriaBuilder.like(root.get("subject").get("name"), "%" + request.getSubjectName() + "%"));
            }
            if (request.getSchoolYear() != null) {
                predicates.add(criteriaBuilder.equal(root.get("schoolYear").get("id"), request.getSchoolYear()));
            }
            if (request.getTerm() != null) {
                predicates.add(criteriaBuilder.equal(root.get("term"), request.getTerm()));
            }
            if (request.getFormId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("form").get("id"), request.getFormId()));
            }
            if (StringUtils.hasText(request.getTestDay())) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("testDay"), MyUtils.convertTimestampFromString(request.getTestDay())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (StringUtils.hasText(request.getProctorId())) {
                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getProctorId(),
                                "proctor1", "proctor2")
                );
            }
            if (StringUtils.hasText(request.getClassId())) {
                predicates.add(criteriaBuilder.like(root.get("classId"), "%" + request.getClassId() + "%"));
            }
            if (request.getExamGroup() != null) {
                predicates.add(criteriaBuilder.equal(root.get("examGroup"), request.getExamGroup()));
            }
            if (request.getCluster() != null) {
                predicates.add(criteriaBuilder.equal(root.get("cluster"), request.getCluster()));
            }
            if (request.getQuantity() != null) {
                predicates.add(criteriaBuilder.equal(root.get("quantity"), request.getQuantity()));
            }
            if (StringUtils.hasText(request.getTestRoom())) {
                predicates.add(criteriaBuilder.like(root.get("testRoom"), "%" + request.getTestRoom() + "%"));
            }
            if (request.getLessonStart() != null) {
                predicates.add(criteriaBuilder.equal(root.get("lessonStart"), request.getTestRoom()));
            }
            if (request.getLessonsTest() != null) {
                predicates.add(criteriaBuilder.equal(root.get("lessonsTest"), request.getLessonsTest()));
            }
            if (StringUtils.hasText(request.getExamCode())) {
                predicates.add(criteriaBuilder.equal(root.get("examCode"), "%" + request.getExamCode() + "%"));
            }
            if (StringUtils.hasText(request.getLecturerTeachId())) {
                predicates.add(criteriaBuilder.like(root.get("lecturerTeach").get("id"), "%" + request.getTestRoom() + "%"));
            }

            query.orderBy(
                    criteriaBuilder.desc(root.get("schoolYear").get("name")),
                    criteriaBuilder.desc(root.get("term")),
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
            if (StringUtils.hasText(request.getDepartmentId())) {
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("id"), request.getDepartmentId()));
            } else if (StringUtils.hasText(request.getFacultyId())) {
                predicates.add(criteriaBuilder.like(root.get("subject").get("department").get("faculty").get("id"), request.getFacultyId()));
            }
            if (StringUtils.hasText(request.getSubjectName())) {
                predicates.add(criteriaBuilder.like(root.get("subject").get("name"), "%" + request.getSubjectName() + "%"));
            }
            if (request.getSchoolYear() != null) {
                predicates.add(criteriaBuilder.equal(root.get("schoolYear").get("id"), request.getSchoolYear()));
            }
            if (request.getTerm() != null) {
                predicates.add(criteriaBuilder.equal(root.get("term"), request.getTerm()));
            }
            if (request.getFormId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("form").get("id"), request.getFormId()));
            }
            if (StringUtils.hasText(request.getTestDay())) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("testDay"), MyUtils.convertTimestampFromString(request.getTestDay())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
//            if(StringUtils.hasText(request.getProctorId())){
//                predicates.add(criteriaBuilder.like(root.get("proctor1").get("id"), request.getProctorId()));
//                predicates.add(criteriaBuilder.like(root.get("proctor2").get("id"), request.getProctorId()));
//            }
            if (StringUtils.hasText(request.getProctorId())) {
                predicates.add(
                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getProctorId(),
                                "proctor1", "proctor2")
                );
            }
            if (StringUtils.hasText(request.getClassId())) {
                predicates.add(criteriaBuilder.like(root.get("classId"), "%" + request.getClassId() + "%"));
            }

            query.orderBy(
                    criteriaBuilder.desc(root.get("schoolYear").get("name")),
                    criteriaBuilder.desc(root.get("term")),
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
