package vn.edu.vnua.department.intern.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.intern.entity.Intern;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long>, JpaSpecificationExecutor<Intern> {

}
