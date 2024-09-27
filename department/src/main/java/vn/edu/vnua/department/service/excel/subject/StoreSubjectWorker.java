package vn.edu.vnua.department.service.excel.subject;

import lombok.AllArgsConstructor;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.repository.DepartmentRepository;
import vn.edu.vnua.department.internship.model.InternshipExcelData;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.model.SubjectExcelData;
import vn.edu.vnua.department.subject.repository.SubjectRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.model.UserExcelData;
import vn.edu.vnua.department.util.MyUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
public class StoreSubjectWorker implements Callable<SubjectExcelData> {
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final User createdBy;
    private final String subjectStr;
    private final int row;

    @Override
    public SubjectExcelData call() throws Exception {
        SubjectExcelData subjectExcelData = new SubjectExcelData();

        if (!subjectStr.isEmpty()) {
            String[] infoList = subjectStr.strip().split(Constants.AppendCharacterConstant.APPEND_CHARACTER);

            String id = infoList[0].strip();
            String name = infoList[1].strip();
            String departmentName = infoList[2].strip();
            String credits = infoList[3].strip();

            Department department = departmentRepository.findByName(departmentName);

            Subject subject = Subject.builder()
                    .id(id)
                    .name(name)
                    .department(department)
                    .credits(MyUtils.parseIntegerFromString(credits))
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .createdBy(createdBy)
                    .build();

            List<SubjectExcelData.ErrorDetail> errorDetailList = subject.validateInformationDetailError(new CopyOnWriteArrayList<>());
            if (subjectRepository.existsById(id)) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(0).errorMsg("Mã môn học đã tồn tại").build());
            }
            if (department == null) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(2).errorMsg("Bộ môn không tồn tại").build());
            } else if (createdBy.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) ||
                    createdBy.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY)) {
                if (!department.getId().equals(createdBy.getDepartment().getId())) {
                    errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(4).errorMsg("Bạn không có quyền thêm môn học của Bộ môn khác").build());
                }
            }

            subjectExcelData.setSubject(subject);
            if (!errorDetailList.isEmpty()) {
                subjectExcelData.setErrorDetailList(errorDetailList);
                subjectExcelData.setValid(false);
            }
            subjectExcelData.setRowIndex(row);
        }

        return subjectExcelData;
    }
}
