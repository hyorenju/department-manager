package vn.edu.vnua.department.service.excel.teaching;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.repository.SubjectRepository;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.teaching.model.TeachingExcelData;
import vn.edu.vnua.department.teaching.repository.TeachingRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.util.MyUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
public class StoreTeachingWorker implements Callable<TeachingExcelData> {
    private final User createdBy;
    private final TeachingRepository teachingRepository;
    private final MasterDataRepository masterDataRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final String teachingStr;
    private final int row;

    @Override
    public TeachingExcelData call() throws Exception {
        TeachingExcelData teachingExcelData = new TeachingExcelData();

        if (!teachingStr.isEmpty()) {
            String[] infoList = teachingStr.strip().split(Constants.AppendCharacterConstant.APPEND_CHARACTER);

            String schoolYearName = infoList[0].strip();
            String term = infoList[1].strip();
            String userId = infoList[2].strip();
            String subjectId = infoList[3].strip();
            String teachingGroup = infoList[4].strip();
            String classId = infoList[5].strip();

            MasterData schoolYear = masterDataRepository.findByName(schoolYearName);
            User teacher = userRepository.getUserById(userId);
            Subject subject = subjectRepository.getSubjectById(subjectId);

            Teaching teaching = Teaching.builder()
                    .schoolYear(schoolYear)
                    .term(StringUtils.hasText(term) ? MyUtils.parseByteFromString(term) : null)
                    .subject(subject)
                    .teacher(teacher)
                    .classId(classId)
                    .teachingGroup(StringUtils.hasText(teachingGroup) ? MyUtils.parseIntegerFromString(teachingGroup) : null)
                    .status(Constants.StatusConstant.INCOMPLETE)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .createdBy(createdBy)
                    .isLock(false)
                    .build();

            List<TeachingExcelData.ErrorDetail> errorDetailList = teaching.validateInformationDetailError(new CopyOnWriteArrayList<>());
            if (schoolYear == null) {
                errorDetailList.add(TeachingExcelData.ErrorDetail.builder().columnIndex(0).errorMsg("Năm học không hợp lệ").build());
            }
            if (teacher == null) {
                errorDetailList.add(TeachingExcelData.ErrorDetail.builder().columnIndex(2).errorMsg("Giảng viên không tồn tại").build());
            }
            if (subject == null) {
                errorDetailList.add(TeachingExcelData.ErrorDetail.builder().columnIndex(3).errorMsg("Môn học không tồn tại").build());
            }
            if (createdBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER) &&
                    createdBy != teacher) {
                errorDetailList.add(TeachingExcelData.ErrorDetail.builder().columnIndex(6).errorMsg("Bạn không thể thêm phân công của giảng viên khác").build());
            } else if (teachingRepository.existsBySchoolYearIdAndTermAndSubjectIdAndClassIdAndTeachingGroup(
                    schoolYear != null ? teaching.getSchoolYear().getId() : null, teaching.getTerm(), subject != null ? teaching.getSubject().getId() : null, teaching.getClassId(), teaching.getTeachingGroup()
            )) {
                errorDetailList.add(TeachingExcelData.ErrorDetail.builder().columnIndex(6).errorMsg("Phân công đã tồn tại").build());
            }

            teachingExcelData.setTeaching(teaching);
            if (!errorDetailList.isEmpty()) {
                teachingExcelData.setErrorDetailList(errorDetailList);
                teachingExcelData.setValid(false);
            }
            teachingExcelData.setRowIndex(row);
        }

        return teachingExcelData;
    }
}
