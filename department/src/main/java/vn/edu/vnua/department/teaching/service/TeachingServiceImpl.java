package vn.edu.vnua.department.teaching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.service.excel.ExcelService;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.repository.SubjectRepository;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.teaching.repository.CustomTeachingRepository;
import vn.edu.vnua.department.teaching.repository.TeachingRepository;
import vn.edu.vnua.department.teaching.request.CreateTeachingRequest;
import vn.edu.vnua.department.teaching.request.ExportTeachingRequest;
import vn.edu.vnua.department.teaching.request.GetTeachingListRequest;
import vn.edu.vnua.department.teaching.request.UpdateTeachingRequest;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class TeachingServiceImpl implements TeachingService {
    private final TeachingRepository teachingRepository;
    private final MasterDataRepository masterDataRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final ExcelService excelService;

    @Override
    public Page<Teaching> getTeachingList(GetTeachingListRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.getUserById(authentication.getPrincipal().toString());
//        request.setDepartment(user.getDepartment());

//        if(request.getSchoolYear()==null || request.getTerm() == null) {
//            Date date = new Date();
//            int year = date.getYear() + 1900;
//            int month = date.getMonth() + 1;
//
//            String schoolYear = year + "-" + (year + 1);
//            Byte term = 1;
//            if ((month + 1) < 7) {
//                schoolYear = (year - 1) + "-" + year;
//                term = 2;
//            }
//            Long schoolYearId = masterDataRepository.findByName(schoolYear).getId();
//
//            request.setTerm(term);
//            request.setSchoolYear(schoolYearId);
//        }
        Specification<Teaching> specification = CustomTeachingRepository.filterTeachingList(request);
        return teachingRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public Teaching createTeaching(CreateTeachingRequest request) {
        User teacher;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());
        if(createdBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)){
            teacher = createdBy;
        } else {
            teacher = userRepository.findById(request.getTeacher().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        }

        if(teachingRepository.existsBySchoolYearIdAndTermAndSubjectIdAndClassIdAndTeachingGroup(request.getSchoolYear().getId(),
                request.getTerm(), request.getSubject().getId(), request.getClassId(), request.getTeachingGroup())) {
            throw new RuntimeException(Constants.TeachingConstant.TEACHING_IS_EXISTED);
        }

        MasterData schoolYear = masterDataRepository.findById(request.getSchoolYear().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.SCHOOL_YEAR_NOT_FOUND));
        Subject subject = subjectRepository.findById(request.getSubject().getId()).orElseThrow(() -> new RuntimeException(Constants.SubjectConstant.SUBJECT_NOT_FOUND));

        String status;
        if (StringUtils.hasText(request.getComponentFile()) && StringUtils.hasText(request.getSummaryFile())) {
            status = Constants.StatusConstant.COMPLETED;
        } else status = Constants.StatusConstant.INCOMPLETE;

        return teachingRepository.saveAndFlush(Teaching.builder()
                .subject(subject)
                .teacher(teacher)
                .classId(request.getClassId())
                .teachingGroup(request.getTeachingGroup())
                .schoolYear(schoolYear)
                .term(request.getTerm())
                .componentFile(request.getComponentFile())
                .summaryFile(request.getSummaryFile())
                .status(status)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .createdBy(createdBy)
                .build());
    }

    @Override
    public Teaching updateTeaching(Long id, UpdateTeachingRequest request) {
        Teaching teaching = teachingRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.TeachingConstant.TEACHING_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedBy = userRepository.getUserById(authentication.getPrincipal().toString());
        if(modifiedBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER) &&
                teaching.getTeacher() != modifiedBy){
            throw new RuntimeException(Constants.TeachingConstant.CANNOT_UPDATE);
        }

        User teacher = userRepository.findById(request.getTeacher().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        
        teaching.setTeacher(teacher);
        teaching.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        teaching.setModifiedBy(modifiedBy);

        if(StringUtils.hasText(request.getComponentFile())){
            teaching.setComponentFile(request.getComponentFile());
        }
        if(StringUtils.hasText(request.getSummaryFile())){
            teaching.setSummaryFile(request.getSummaryFile());
        }

        if (teaching.getComponentFile() != null && teaching.getSummaryFile() != null) {
            teaching.setStatus(Constants.StatusConstant.COMPLETED);
        } else teaching.setStatus(Constants.StatusConstant.INCOMPLETE);

        return teachingRepository.saveAndFlush(teaching);
    }

    @Override
    public Teaching deleteTeaching(Long id) {
        Teaching teaching = teachingRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.TeachingConstant.TEACHING_NOT_FOUND));
        teachingRepository.delete(teaching);
        return teaching;
    }

    @Override
    public List<Teaching> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        return teachingRepository.saveAll(excelService.readTeachingFromExcel(file));
    }

    @Override
    public String exportToExcel(ExportTeachingRequest request) {
        Specification<Teaching> specification = CustomTeachingRepository.filterExportTeaching(request);
        List<Teaching> teachings = teachingRepository.findAll(specification);
        return excelService.writeTeachingToExcel(teachings);
    }
}
