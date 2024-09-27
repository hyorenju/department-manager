package vn.edu.vnua.department.service.excel.internship;

import lombok.AllArgsConstructor;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.model.InternExcelData;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.util.MyUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
public class StoreInternshipWorker implements Callable<InternExcelData> {
    private final MasterDataRepository masterDataRepository;
    private final UserRepository userRepository;
    private final String internStr;
    private final int row;

    @Override
    public InternExcelData call() throws Exception {
        InternExcelData internExcelData = new InternExcelData();

        if (!internStr.isEmpty()) {
            String[] infoList = internStr.strip().split(Constants.AppendCharacterConstant.APPEND_CHARACTER);

            String schoolYearName = infoList[0].strip();
            String term = infoList[1].strip();
            String name = infoList[2].strip();
            String internTypeName = infoList[3].strip();
            String instructorId = infoList[4].strip();

            MasterData schoolYear = masterDataRepository.findByName(schoolYearName);
            MasterData internType = masterDataRepository.findByName(internTypeName);
            User instructor = userRepository.getUserById(instructorId);


            Intern intern = Intern.builder()
                    .name(name)
                    .type(internType)
                    .schoolYear(schoolYear)
                    .term(MyUtils.parseByteFromString(term))
                    .instructor(instructor)
                    .status(Constants.UploadFileStatusConstant.INCOMPLETE)
                    .build();

            List<InternExcelData.ErrorDetail> errorDetailList = intern.validateInformationDetailError(new CopyOnWriteArrayList<>());
            if (schoolYear == null) {
                errorDetailList.add(InternExcelData.ErrorDetail.builder().columnIndex(0).errorMsg("Năm học không hợp lệ").build());
            }
            if(internType == null) {
                errorDetailList.add(InternExcelData.ErrorDetail.builder().columnIndex(3).errorMsg("Loại đề tài không tồn tại").build());
            }
            if(instructor == null) {
                errorDetailList.add(InternExcelData.ErrorDetail.builder().columnIndex(4).errorMsg("GV không tồn tại").build());
            }

            internExcelData.setIntern(intern);
            if (!errorDetailList.isEmpty()) {
                internExcelData.setErrorDetailList(errorDetailList);
                internExcelData.setValid(false);
            }
            internExcelData.setRowIndex(row);
        }

        return internExcelData;
    }
}
