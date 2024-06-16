package vn.edu.vnua.department.masterdata.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnua.department.masterdata.entity.MasterData;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterDataRepository extends JpaRepository<MasterData, Long> {
    List<MasterData> findAllByType(String type, Sort sort);
    MasterData findByName(String name);
    Optional<MasterData> getByName(String name);
}
