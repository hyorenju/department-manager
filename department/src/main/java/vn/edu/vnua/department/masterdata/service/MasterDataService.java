package vn.edu.vnua.department.masterdata.service;

import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.masterdata.request.GetMasterDataListRequest;
import vn.edu.vnua.department.masterdata.request.UpdateMasterDataRequest;

import java.util.List;

public interface MasterDataService {
    List<MasterData> getMasterDataList(GetMasterDataListRequest request);
    MasterData createMasterData(CreateMasterDataRequest request);
    MasterData updateMasterData(Long id, UpdateMasterDataRequest request);
    MasterData deleteMasterData(Long id);
}
