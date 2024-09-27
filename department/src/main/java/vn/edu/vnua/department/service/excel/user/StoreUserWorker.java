package vn.edu.vnua.department.service.excel.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.repository.DepartmentRepository;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.internship.model.InternshipExcelData;
import vn.edu.vnua.department.internship.repository.InternshipRepository;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.role.repository.RoleRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.model.UserExcelData;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.util.MyUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
public class StoreUserWorker implements Callable<UserExcelData> {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final MasterDataRepository masterDataRepository;
    private final DepartmentRepository departmentRepository;
    private final Role role;
    private final User createdBy;
    private final String userStr;
    private final int row;

    @Override
    public UserExcelData call() throws Exception {
        UserExcelData userExcelData = new UserExcelData();

        if (!userStr.isEmpty()) {
            String[] infoList = userStr.strip().split(Constants.AppendCharacterConstant.APPEND_CHARACTER);

            String id = infoList[0].strip();
            String firstName = infoList[1].strip();
            String lastName = infoList[2].strip();
            String degreeName = infoList[3].strip();
            String email = infoList[4].strip();
            String phoneNumber = infoList[5].strip();
            String departmentName = infoList[6].strip();

            MasterData degree = masterDataRepository.findByName(degreeName);
            Department department = departmentRepository.findByName(departmentName);

            User user = User.builder()
                    .id(id)
                    .firstName(firstName)
                    .lastName(lastName)
                    .degree(degree)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .department(department)
                    .password(encoder.encode(Constants.UserConstant.DEFAULT_PASSWORD))
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .role(role)
                    .isLock(false)
                    .build();

            List<UserExcelData.ErrorDetail> errorDetailList = user.validateInformationDetailError(new CopyOnWriteArrayList<>());
            if (userRepository.existsById(id)) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(0).errorMsg("Mã người dùng đã tồn tại").build());
            }
            if (degree == null) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(3).errorMsg("Trình độ không tồn tại").build());
            }
            if (department == null) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(6).errorMsg("Bộ môn không tồn tại").build());
            } else if (createdBy.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) ||
                    createdBy.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY)) {
                if (!department.getId().equals(createdBy.getDepartment().getId())) {
                    errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(7).errorMsg("Bạn không có quyền thêm GV của Bộ môn khác").build());
                }
            }

            userExcelData.setUser(user);
            if (!errorDetailList.isEmpty()) {
                userExcelData.setErrorDetailList(errorDetailList);
                userExcelData.setValid(false);
            }
            userExcelData.setRowIndex(row);
        }

        return userExcelData;
    }
}
