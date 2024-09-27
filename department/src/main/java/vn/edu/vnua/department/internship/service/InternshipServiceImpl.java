package vn.edu.vnua.department.internship.service;

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
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.repository.CustomInternRepository;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.internship.repository.CustomInternshipRepository;
import vn.edu.vnua.department.internship.repository.InternshipRepository;
import vn.edu.vnua.department.internship.request.CreateInternshipRequest;
import vn.edu.vnua.department.internship.request.GetInternshipListRequest;
import vn.edu.vnua.department.internship.request.LockInternshipListRequest;
import vn.edu.vnua.department.internship.request.UpdateInternshipRequest;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.service.excel.ExcelService;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class InternshipServiceImpl implements InternshipService {
    private final InternshipRepository internshipRepository;
    private final MasterDataRepository masterDataRepository;
    private final UserRepository userRepository;
    private final ExcelService excelService;

    @Override
    public Page<Internship> getInternList(GetInternshipListRequest request) {
        if (!request.getIsAll()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User instructor = userRepository.getUserById(authentication.getPrincipal().toString());

            request.setInstructorId(instructor.getId());
        }
        Specification<Internship> specification = CustomInternshipRepository.filterInternshipList(request);
        return internshipRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public Internship createIntern(CreateInternshipRequest request) {
        if(internshipRepository.existsBySchoolYearIdAndTermAndStudentIdAndName(
                request.getSchoolYear().getId(), request.getTerm(), request.getStudentId(), request.getName()
        )){
            throw new RuntimeException(Constants.InternshipConstant.INTERNSHIP_EXISTED);
        }

        MasterData type = masterDataRepository.findById(request.getType().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.INTERN_TYPE_NOT_FOUND));
        MasterData schoolYear = masterDataRepository.findById(request.getSchoolYear().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.SCHOOL_YEAR_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());
        User instructor;
        if (createdBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            instructor = createdBy;
        } else {
            instructor = userRepository.getUserById(request.getInstructor().getId());
        }

        return internshipRepository.saveAndFlush(Internship.builder()
                .schoolYear(schoolYear)
                .term(request.getTerm())
                .studentId(request.getStudentId())
                .studentName(request.getStudentName())
                .classId(request.getClassId())
                .phone(request.getPhone())
                .company(request.getCompany())
                .name(request.getName())
                .type(type)
                .instructor(instructor)
                .outlineFile(request.getOutlineFile())
                .progressFile(request.getProgressFile())
                .finalFile(request.getFinalFile())
                .status(StringUtils.hasText(request.getFinalFile()) ? Constants.UploadFileStatusConstant.COMPLETED : Constants.UploadFileStatusConstant.INCOMPLETE)
                .note(request.getNote())
                .createdBy(createdBy)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .isLock(false)
                .build());
    }

    @Override
    public Internship updateIntern(Long id, UpdateInternshipRequest request) {
        if(internshipRepository.existsBySchoolYearIdAndTermAndStudentIdAndName(
                request.getSchoolYear().getId(), request.getTerm(), request.getStudentId(), request.getName()
        )){
            throw new RuntimeException(Constants.InternshipConstant.INTERNSHIP_EXISTED);
        }

        Internship internship = internshipRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.InternshipConstant.INTERNSHIP_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedBy = userRepository.getUserById(authentication.getPrincipal().toString());
        User instructor = internship.getInstructor();
        if (modifiedBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            if (modifiedBy != internship.getInstructor()) {
                throw new RuntimeException(Constants.InternshipConstant.CANNOT_UPDATE);
            }
        } else {
            instructor = userRepository.getUserById(request.getInstructor().getId());
        }

        MasterData type = masterDataRepository.findById(request.getType().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.INTERN_TYPE_NOT_FOUND));
        MasterData schoolYear = masterDataRepository.findById(request.getSchoolYear().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.SCHOOL_YEAR_NOT_FOUND));

        internship.setSchoolYear(schoolYear);
        internship.setTerm(request.getTerm());
        internship.setStudentId(internship.getStudentId());
        internship.setStudentName(request.getStudentName());
        internship.setClassId(internship.getClassId());
        internship.setPhone(request.getPhone());
        internship.setCompany(request.getCompany());
        internship.setName(request.getName());
        internship.setType(type);
        internship.setInstructor(instructor);
        internship.setNote(request.getNote());

        if (StringUtils.hasText(request.getOutlineFile())) {
            internship.setOutlineFile(request.getOutlineFile());
        }
        if (StringUtils.hasText(request.getProgressFile())) {
            internship.setProgressFile(request.getProgressFile());
        }
        if (StringUtils.hasText(request.getFinalFile())) {
            internship.setFinalFile(request.getFinalFile());
        }

        if (internship.getFinalFile() != null) {
            internship.setStatus(Constants.UploadFileStatusConstant.COMPLETED);
        } else internship.setStatus(Constants.UploadFileStatusConstant.INCOMPLETE);


        return internshipRepository.saveAndFlush(internship);
    }

    @Override
    public Internship deleteIntern(Long id) {
        Internship internship = internshipRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.InternshipConstant.INTERNSHIP_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserById(authentication.getPrincipal().toString());
        if (user.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            if (user != internship.getInstructor())
                throw new RuntimeException(Constants.InternshipConstant.CANNOT_UPDATE);
        }

        internshipRepository.delete(internship);
        return internship;
    }

    @Override
    public Internship lockInternship(Long id) {
        Internship internship = internshipRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.InternshipConstant.INTERNSHIP_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserById(authentication.getPrincipal().toString());

        if (user.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            throw new RuntimeException(Constants.InternConstant.CANNOT_LOCK);
        }

        if(!internship.getIsLock()){
            internship.setIsLock(true);
        } else if (internship.getIsLock()){
            internship.setIsLock(false);
        }

        return internshipRepository.saveAndFlush(internship);
    }

    @Override
    public List<Internship> lockInternshipList(LockInternshipListRequest request) {
        Specification<Internship> specification = CustomInternshipRepository.filterLockInternshipList(request);
        List<Internship> internshipList = internshipRepository.findAll(specification);

        Boolean isLock = request.getWantLock();

        for (Internship internship:
                internshipList) {
            internship.setIsLock(isLock);
        }

        return internshipRepository.saveAllAndFlush(internshipList);
    }

    @Override
    public List<Internship> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        return internshipRepository.saveAll(excelService.readInternFromExcel(file));
    }

    @Override
    public String exportToExcel(ExportInternListRequest request) {
        if (!request.getIsAll()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User instructor = userRepository.getUserById(authentication.getPrincipal().toString());

            request.setInstructorId(instructor.getId());
        }
        Specification<Internship> specification = CustomInternshipRepository.filterExportInternship(request);
        List<Internship> interns = internshipRepository.findAll(specification);
        return excelService.writeInternToExcel(interns);
    }
}
