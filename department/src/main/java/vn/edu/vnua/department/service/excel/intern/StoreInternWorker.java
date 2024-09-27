package vn.edu.vnua.department.service.excel.intern;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.model.InternExcelData;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.internship.model.InternshipExcelData;
import vn.edu.vnua.department.internship.repository.InternshipRepository;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.util.MyUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
public class StoreInternWorker implements Callable<InternshipExcelData> {
    private final MasterDataRepository masterDataRepository;
    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final User createdBy;
    private final String internStr;
    private final int row;

    @Override
    public InternshipExcelData call() throws Exception {
        InternshipExcelData internExcelData = new InternshipExcelData();

        if (!internStr.isEmpty()) {
            String[] infoList = internStr.strip().split(Constants.AppendCharacterConstant.APPEND_CHARACTER);

            String schoolYearName = infoList[0].strip();
            String term = infoList[1].strip();
            String name = infoList[2].strip();
            String internTypeName = infoList[3].strip();
            String instructorId = infoList[4].strip();
            String studentName = infoList[5].strip();
            String studentId = infoList[6].strip();
            String classId = infoList[7].strip();
            String phone = infoList[8].strip();
            String company = infoList[9].strip();

            MasterData schoolYear = masterDataRepository.findByName(schoolYearName);
            MasterData internType = masterDataRepository.findByName(internTypeName);
            User instructor = userRepository.getUserById(instructorId);

            Internship intern = Internship.builder()
                    .schoolYear(schoolYear)
                    .term(MyUtils.parseByteFromString(term))
                    .name(name)
                    .type(internType)
                    .instructor(instructor)
                    .studentId(studentId)
                    .studentName(studentName)
                    .classId(classId)
                    .phone(phone)
                    .company(company)
                    .status(Constants.UploadFileStatusConstant.INCOMPLETE)
                    .createdBy(createdBy)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .isLock(false)
                    .build();

            List<InternshipExcelData.ErrorDetail> errorDetailList = intern.validateInformationDetailError(new CopyOnWriteArrayList<>());
            if (schoolYear == null) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(0).errorMsg("Năm học không hợp lệ").build());
            }
            if(internType == null) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(3).errorMsg("Loại đề tài không tồn tại").build());
            }
            if(instructor == null) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(4).errorMsg("GV không tồn tại").build());
            }
            if(schoolYear!=null && internshipRepository.existsBySchoolYearIdAndTermAndStudentIdAndName(
                    schoolYear.getId(), MyUtils.parseByteFromString(term), studentId, name
            )) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(10).errorMsg("Những thông tin của bản ghi này đang bị trùng với một bản ghi đã tồn tại").build());
            }
            if(instructor!=null &&
                    createdBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
                if(!instructorId.equals(createdBy.getId())) {
                    errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(10).errorMsg("Bạn không có quyền thêm đề tài TT cho GVHD khác").build());
                }
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
