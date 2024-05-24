package vn.edu.vnua.department.masterdata.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.masterdata.request.GetMasterDataListRequest;
import vn.edu.vnua.department.masterdata.request.UpdateMasterDataRequest;
import vn.edu.vnua.department.util.MyUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterDataServiceImpl implements MasterDataService{
    private final MasterDataRepository masterDataRepository;


    @Override
    public List<MasterData> getMasterDataList(GetMasterDataListRequest request) {
        Sort sort;
        if (request.getType().equals(Constants.MasterDataTypeConstant.SCHOOL_YEAR)) {
            sort = Sort.by("name").descending();
        } else {
            sort = Sort.by("name").ascending();
        }
        return masterDataRepository.findAllByType(request.getType(), sort);
    }

    @Override
    public MasterData createMasterData(CreateMasterDataRequest request) {
        try {
            return masterDataRepository.saveAndFlush(MasterData.builder()
                    .name(request.getName())
                    .type(request.getType())
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(Constants.MasterDataConstant.DATA_IS_EXISTED);
        }
    }

    @Override
    public MasterData updateMasterData(Long id, UpdateMasterDataRequest request) {
        try {
            MasterData masterData = masterDataRepository.findById(id).orElseThrow(()->new RuntimeException(Constants.MasterDataConstant.DATA_NOT_FOUND));
            masterData.setName(request.getName());
            return masterDataRepository.saveAndFlush(masterData);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(Constants.MasterDataConstant.DATA_IS_EXISTED);
        }
    }

    @Override
    public MasterData deleteMasterData(Long id) {
        try {
            MasterData masterData = masterDataRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.DATA_NOT_FOUND));
            masterDataRepository.delete(masterData);
            return masterData;
        } catch (Exception e) {
            throw new RuntimeException(Constants.MasterDataConstant.CANNOT_DELETE);
        }
    }

    @Override
    @Scheduled(cron = "0 0 0 1 5 *")
    public void createSchoolYear() {
        MasterData theLastSchoolYear = masterDataRepository.findAllByType(Constants.MasterDataTypeConstant.SCHOOL_YEAR, Sort.by("name").descending()).get(0);
        Integer lastYear = MyUtils.parseIntegerFromString(theLastSchoolYear.getName().split("-")[0]);
        String newSchoolYearName = (lastYear+1) + "-" + (lastYear+2);

//        System.out.println(theLastSchoolYear.getName());
//        System.out.println(newSchoolYearName);

        masterDataRepository.saveAndFlush(MasterData.builder()
                .name(newSchoolYearName)
                .type(Constants.MasterDataTypeConstant.SCHOOL_YEAR)
                .build());
    }
}
