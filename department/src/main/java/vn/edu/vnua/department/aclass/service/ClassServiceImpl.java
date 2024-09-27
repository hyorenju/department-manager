package vn.edu.vnua.department.aclass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.repository.ClassRepository;
import vn.edu.vnua.department.aclass.repository.CustomClassRepository;
import vn.edu.vnua.department.aclass.request.CreateClassRequest;
import vn.edu.vnua.department.aclass.request.ExportClassListRequest;
import vn.edu.vnua.department.aclass.request.GetClassListRequest;
import vn.edu.vnua.department.aclass.request.UpdateClassRequest;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.repository.FacultyRepository;
import vn.edu.vnua.department.service.excel.ExcelService;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.CustomUserRepository;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService{
    private final ClassRepository classRepository;
    private final FacultyRepository facultyRepository;
    private final UserRepository userRepository;
    private final ExcelService excelService;

    @Override
    public Page<AClass> getClassList(GetClassListRequest request) {
        Specification<AClass> specification = CustomClassRepository.filterClassList(request);
        return classRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public AClass createClass(CreateClassRequest request) {
        if(classRepository.existsById(request.getId())){
            throw new RuntimeException(Constants.ClassConstant.CLASS_EXISTED);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());

        Faculty faculty = facultyRepository.findById(request.getFaculty().getId()).orElseThrow(()->new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));

        return classRepository.saveAndFlush(AClass.builder()
                .id(request.getId())
                .name(request.getName())
                .faculty(faculty)
                .hrTeacher(request.getHrTeacher())
                .monitor(request.getMonitor())
                .monitorPhone(request.getMonitorPhone())
                .monitorEmail(request.getMonitorEmail())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .createdBy(createdBy)
                .note(request.getNote())
                .build());
    }

    @Override
    public AClass updateClass(String id, UpdateClassRequest request) {
        AClass aClass = classRepository.findById(id).orElseThrow(()->new RuntimeException(Constants.ClassConstant.CLASS_NOT_FOUND));

        Faculty faculty = facultyRepository.findById(request.getFaculty().getId()).orElseThrow(()->new RuntimeException(Constants.FacultyConstant.FACULTY_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedBy = userRepository.getUserById(authentication.getPrincipal().toString());

        aClass.setName(request.getName());
        aClass.setFaculty(faculty);
        aClass.setHrTeacher(request.getHrTeacher());
        aClass.setMonitor(request.getMonitor());
        aClass.setMonitorPhone(request.getMonitorPhone());
        aClass.setMonitorEmail(request.getMonitorEmail());
        aClass.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        aClass.setModifiedBy(modifiedBy);
        aClass.setNote(request.getNote());

        return classRepository.saveAndFlush(aClass);
    }

    @Override
    public AClass deleteClass(String id) {
        try {
            AClass aClass = classRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.ClassConstant.CLASS_NOT_FOUND));
            classRepository.delete(aClass);
            return aClass;
        } catch (Exception e) {
            throw new RuntimeException(Constants.ClassConstant.CANNOT_DELETE);
        }
    }

    @Override
    public List<AClass> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        return classRepository.saveAll(excelService.readClassFromExcel(file));
    }

    @Override
    public String exportToExcel(ExportClassListRequest request) {
        Specification<AClass> specification = CustomClassRepository.filterExportClass(request);
        List<AClass> classes = classRepository.findAll(specification);
        return excelService.writeClassToExcel(classes);
    }
}
