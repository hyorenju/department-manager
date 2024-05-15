package vn.edu.vnua.department.teaching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.teaching.entity.Teaching;

@Repository
public interface TeachingRepository extends JpaRepository<Teaching, Long> , JpaSpecificationExecutor<Teaching> {
    boolean existsBySchoolYearIdAndTermAndSubjectIdAndClassIdAndTeachingGroup(Long schoolYearId,
                                                                              Byte term,
                                                                              String subjectId,
                                                                              String classId,
                                                                              Integer teachingGroup);
}
