package vn.edu.vnua.department.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.subject.entity.Subject;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long>, JpaSpecificationExecutor<Exam> {
    List<Exam> findAllByTestDay(Timestamp testDay);

    boolean existsBySubjectIdAndExamGroupAndSchoolYearIdAndTermAndCluster(String subjectId,
                                                                                    Integer examGroup,
                                                                                    Long schoolYearId,
                                                                                    Byte Term,
                                                                                    Integer cluster);

    List<Exam> findAllByIsWarning(Boolean isWarning);
}
