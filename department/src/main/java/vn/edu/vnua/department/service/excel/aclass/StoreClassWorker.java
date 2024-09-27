package vn.edu.vnua.department.service.excel.aclass;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.model.ClassExcelData;
import vn.edu.vnua.department.aclass.repository.ClassRepository;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.repository.DepartmentRepository;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.repository.FacultyRepository;
import vn.edu.vnua.department.internship.model.InternshipExcelData;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.model.UserExcelData;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
public class StoreClassWorker implements Callable<ClassExcelData> {
    private final FacultyRepository facultyRepository;
    private final ClassRepository classRepository;
    private final User createdBy;
    private final String classStr;
    private final int row;

    @Override
    public ClassExcelData call() throws Exception {
        ClassExcelData classExcelData = new ClassExcelData();

        if (!classStr.isEmpty()) {
            String[] infoList = classStr.strip().split(Constants.AppendCharacterConstant.APPEND_CHARACTER);

            String id = infoList[0].strip();
            String name = infoList[1].strip();
            String facultyName = infoList[2].strip();
            String hrTeacher = infoList[3].strip();
            String monitor = infoList[4].strip();
            String monitorPhone = infoList[5].strip();
            String monitorEmail = infoList[6].strip();

            Faculty faculty = facultyRepository.findByName(facultyName);

            AClass aClass = AClass.builder()
                    .id(id)
                    .name(name)
                    .faculty(faculty)
                    .hrTeacher(hrTeacher)
                    .monitor(monitor)
                    .monitorPhone(monitorPhone)
                    .monitorEmail(monitorEmail)
                    .createdBy(createdBy)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build();

            List<ClassExcelData.ErrorDetail> errorDetailList = aClass.validateInformationDetailError(new CopyOnWriteArrayList<>());
            if (faculty == null) {
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(2).errorMsg("Khoa không tồn tại").build());
            }
            if(classRepository.existsById(id)){
                errorDetailList.add(InternshipExcelData.ErrorDetail.builder().columnIndex(7).errorMsg("Mã lớp đã tồn tại").build());
            }

            classExcelData.setAClass(aClass);
            if (!errorDetailList.isEmpty()) {
                classExcelData.setErrorDetailList(errorDetailList);
                classExcelData.setValid(false);
            }
            classExcelData.setRowIndex(row);
        }

        return classExcelData;
    }
}
