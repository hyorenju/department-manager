package vn.edu.vnua.department.masterdata.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.masterdata.request.GetMasterDataListRequest;
import vn.edu.vnua.department.masterdata.request.UpdateMasterDataRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterDataServiceImpl implements MasterDataService{
    private final MasterDataRepository masterDataRepository;


    @Override
    public List<MasterData> getMasterDataList(GetMasterDataListRequest request) {
        Sort sort;
        if (Constants.MasterDataTypeConstant.SCHOOL_YEAR.equals(request.getType())) {
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
}
