package vn.edu.vnua.department.userjointask.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.project.request.FilterProjectPage;
import vn.edu.vnua.department.userjointask.entity.UserTask;
import vn.edu.vnua.department.userjointask.request.GetUserTaskListRequest;
import vn.edu.vnua.department.userjointask.request.GetUserTaskPageRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomUserTaskRepository {
    public static Specification<UserTask> filterUserTaskList(GetUserTaskListRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getTaskId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("task").get("id"), request.getTaskId()));
            }
            if (StringUtils.hasText(request.getUserId())) {
                predicates.add(criteriaBuilder.like(root.get("user").get("id"), request.getUserId()));
            }
            if (request.getMonthCalendar() != null && request.getYearCalendar() != null) {
                LocalDateTime date = LocalDateTime.of(request.getYearCalendar(), request.getMonthCalendar(), 15, 0, 0);
                LocalDateTime startOfMonth = date.minusMonths(1).withDayOfMonth(15).toLocalDate().atStartOfDay();
                LocalDateTime endOfMonth = date.plusMonths(1).withDayOfMonth(15).toLocalDate().atStartOfDay();
                Timestamp startTimestamp = Timestamp.valueOf(startOfMonth);
                Timestamp endTimestamp = Timestamp.valueOf(endOfMonth);

                predicates.add(criteriaBuilder.between(root.get("task").get("start"), startTimestamp, endTimestamp));
                predicates.add(criteriaBuilder.between(root.get("task").get("deadline"), startTimestamp, endTimestamp));
            }
            if(request.getIsSchedule()){
                predicates.add(criteriaBuilder.like(root.get("personalStatus").get("name"), Constants.MasterDataNameConstant.DOING));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<UserTask> filterUserTaskPage(GetUserTaskPageRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
//            if (StringUtils.hasText(request.getKeyword())) {
//                predicates.add(
//                        CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder, request.getKeyword(),
//                                "task"."description", "task.name", "task.project.description", "task.project.name")
//                );
//            }
            if (StringUtils.hasText(request.getKeyword())) {
                // Tạo điều kiện tìm kiếm không phân biệt chữ hoa/thường
                Predicate keywordPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("task").get("description")), "%" + request.getKeyword().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("task").get("name")), "%" + request.getKeyword().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("task").get("project").get("description")), "%" + request.getKeyword().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("task").get("project").get("name")), "%" + request.getKeyword().toLowerCase() + "%")
                );
                predicates.add(keywordPredicate);
            }
            if (StringUtils.hasText(request.getUserId())) {
                predicates.add(criteriaBuilder.like(root.get("user").get("id"), request.getUserId()));
//                if (request.getIsPrivate()) {
//                    predicates.add(criteriaBuilder.like(root.get("personalStatus").get("name"), Constants.MasterDataNameConstant.DOING));
//                    predicates.add(criteriaBuilder.like(root.get("personalStatus").get("name"), Constants.MasterDataNameConstant.DOING_LATE));
//                } else if (!request.getIsPrivate()){
//                    predicates.add(criteriaBuilder.like(root.get("personalStatus").get("name"), Constants.MasterDataNameConstant.FINISHED_SOONER));
//                    predicates.add(criteriaBuilder.like(root.get("personalStatus").get("name"), Constants.MasterDataNameConstant.FINISHED_ON_TIME));
//                    predicates.add(criteriaBuilder.like(root.get("personalStatus").get("name"), Constants.MasterDataNameConstant.FINISHED_LATE));
//                }
            }
            query.orderBy(
                    criteriaBuilder.asc(root.get("personalStatus").get("name")),
                    criteriaBuilder.asc(root.get("task").get("project").get("createdAt"))
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<UserTask> filterPage(FilterProjectPage request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getMemberId())) {
                predicates.add(criteriaBuilder.like(root.get("user").get("id"), request.getMemberId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}