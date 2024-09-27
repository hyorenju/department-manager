package vn.edu.vnua.department.internship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.masterdata.entity.MasterData;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long>, JpaSpecificationExecutor<Internship> {
    boolean existsBySchoolYearIdAndTermAndStudentIdAndName(Long schoolYearId,
                                                                Byte term,
                                                                String studentId,
                                                                String name);
}
