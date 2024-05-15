package vn.edu.vnua.department.masterdata.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnua.department.controller.BaseController;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.masterdata.request.CreateMasterDataRequest;
import vn.edu.vnua.department.masterdata.request.GetMasterDataListRequest;
import vn.edu.vnua.department.masterdata.request.UpdateMasterDataRequest;
import vn.edu.vnua.department.masterdata.service.MasterDataService;

import java.util.List;

@RestController
@RequestMapping("master-data")
@RequiredArgsConstructor
public class MasterDataController extends BaseController {
    private final MasterDataService masterDataService;
    private final ModelMapper modelMapper;

    @PostMapping("list")
    private ResponseEntity<?> getMasterDataList(@RequestBody @Valid GetMasterDataListRequest request){
        List<MasterDataDTO> response = masterDataService.getMasterDataList(request).stream().map(
                masterData -> modelMapper.map(masterData, MasterDataDTO.class)
        ).toList();
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("create")
    private ResponseEntity<?> createMasterData(@RequestBody @Valid CreateMasterDataRequest request){
        MasterDataDTO response = modelMapper.map(masterDataService.createMasterData(request), MasterDataDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("update/{id}")
    private ResponseEntity<?> updateMasterData(@PathVariable Long id, @RequestBody @Valid UpdateMasterDataRequest request){
        MasterDataDTO response = modelMapper.map(masterDataService.updateMasterData(id, request), MasterDataDTO.class);
        return buildItemResponse(response);
    }

    @PostMapping("delete/{id}")
    private ResponseEntity<?> deleteMasterData(@PathVariable Long id){
        MasterDataDTO response = modelMapper.map(masterDataService.deleteMasterData(id), MasterDataDTO.class);
        return buildItemResponse(response);
    }
}
