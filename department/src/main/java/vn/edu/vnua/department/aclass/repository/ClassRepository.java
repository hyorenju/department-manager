package vn.edu.vnua.department.aclass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.aclass.entity.AClass;

@Repository
public interface ClassRepository extends JpaRepository<AClass, String>, JpaSpecificationExecutor<AClass> {
}
