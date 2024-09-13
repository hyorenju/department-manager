package vn.edu.vnua.department.intern.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
import vn.edu.vnua.department.intern.repository.InternRepository;
import vn.edu.vnua.department.intern.request.*;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.service.excel.ExcelService;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class InternServiceImpl implements InternService {
    private final InternRepository internRepository;
    private final MasterDataRepository masterDataRepository;
    private final UserRepository userRepository;
    private final ExcelService excelService;

    @Override
    public Page<Intern> getInternList(GetInternListRequest request) {
        if(!request.getIsAll()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User instructor = userRepository.getUserById(authentication.getPrincipal().toString());

//            Timestamp currentDate = Timestamp.valueOf(LocalDateTime.now());
//            int month = currentDate.getMonth() + 1;
//            int year = currentDate.getYear() + 1900;
//            int term = month > 6 ? 1 : 2;
//            String schoolYearName = month > 6 ? (year+"-"+(year+1)) : ((year-1)+"-"+year);
//
//            MasterData schoolYear = masterDataRepository.findByName(schoolYearName);

            request.setInstructorId(instructor.getId());
//            request.setTerm((byte) term);
//            request.setSchoolYear(schoolYear.getId());
        }
        Specification<Intern> specification = CustomInternRepository.filterInternList(request);
        return internRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public Intern createIntern(CreateInternRequest request) {
        try {
            MasterData type = masterDataRepository.findById(request.getType().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.INTERN_TYPE_NOT_FOUND));
            MasterData schoolYear = masterDataRepository.findById(request.getSchoolYear().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.SCHOOL_YEAR_NOT_FOUND));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User instructor = userRepository.getUserById(authentication.getPrincipal().toString());

            return internRepository.saveAndFlush(Intern.builder()
                    .name(request.getName())
                    .type(type)
                    .schoolYear(schoolYear)
                    .term(request.getTerm())
                    .instructor(instructor)
                    .outlineFile(request.getOutlineFile())
                    .progressFile(request.getProgressFile())
                    .finalFile(request.getFinalFile())
                    .status(StringUtils.hasText(request.getFinalFile()) ? Constants.StatusConstant.COMPLETED : Constants.StatusConstant.INCOMPLETE)
                    .note(request.getNote())
                    .isLock(false)
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(Constants.InternConstant.NAME_IS_EXISTED);
        }
    }

    @Override
    public Intern updateIntern(Long id, UpdateInternRequest request) {
        try {
            Intern intern = internRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.InternConstant.INTERN_NOT_FOUND));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.getUserById(authentication.getPrincipal().toString());

            if (user != intern.getInstructor() && user.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
                throw new RuntimeException(Constants.InternConstant.CANNOT_UPDATE);
            }

            MasterData type = masterDataRepository.findById(request.getType().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.INTERN_TYPE_NOT_FOUND));
            MasterData schoolYear = masterDataRepository.findById(request.getSchoolYear().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.SCHOOL_YEAR_NOT_FOUND));

            intern.setName(request.getName());
            intern.setType(type);
            intern.setSchoolYear(schoolYear);
            intern.setTerm(request.getTerm());
            intern.setNote(request.getNote());

            if (StringUtils.hasText(request.getOutlineFile())) {
                intern.setOutlineFile(request.getOutlineFile());
            }
            if (StringUtils.hasText(request.getProgressFile())) {
                intern.setProgressFile(request.getProgressFile());
            }
            if (StringUtils.hasText(request.getFinalFile())) {
                intern.setFinalFile(request.getFinalFile());
            }

            if (intern.getFinalFile() != null) {
                intern.setStatus(Constants.StatusConstant.COMPLETED);
            } else intern.setStatus(Constants.StatusConstant.INCOMPLETE);


            return internRepository.saveAndFlush(intern);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(Constants.InternConstant.NAME_IS_EXISTED);
        }
    }

    @Override
    public Intern deleteIntern(Long id) {
        Intern intern = internRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.InternConstant.INTERN_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserById(authentication.getPrincipal().toString());

        if (user != intern.getInstructor() && user.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            throw new RuntimeException(Constants.InternConstant.CANNOT_UPDATE);
        }

        internRepository.delete(intern);
        return intern;
    }

    @Override
    public Intern lockIntern(Long id) {
        Intern intern = internRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.InternConstant.INTERN_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserById(authentication.getPrincipal().toString());

        if (user.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            throw new RuntimeException(Constants.InternConstant.CANNOT_LOCK);
        }

        if(!intern.getIsLock()){
            intern.setIsLock(true);
        } else if (intern.getIsLock()){
            intern.setIsLock(false);
        }

        return internRepository.saveAndFlush(intern);
    }

    @Override
    public List<Intern> lockInternList(LockInternListRequest request) {
        Specification<Intern> specification = CustomInternRepository.filterLockInternList(request);
        List<Intern> internList = internRepository.findAll(specification);

        Boolean isLock = request.getWantLock();

        for (Intern intern:
             internList) {
            intern.setIsLock(isLock);
        }

        return internRepository.saveAllAndFlush(internList);
    }

    @Override
    public List<Intern> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        return internRepository.saveAll(excelService.readInternFromExcel(file));
    }

    @Override
    public String exportToExcel(ExportInternListRequest request) {
        if(!request.getIsAll()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User instructor = userRepository.getUserById(authentication.getPrincipal().toString());

//            Timestamp currentDate = Timestamp.valueOf(LocalDateTime.now());
//            int month = currentDate.getMonth() + 1;
//            int year = currentDate.getYear() + 1900;
//            int term = month > 6 ? 1 : 2;
//            String schoolYearName = month > 6 ? (year+"-"+(year+1)) : ((year-1)+"-"+year);
//
//            MasterData schoolYear = masterDataRepository.findByName(schoolYearName);

            request.setInstructorId(instructor.getId());
//            request.setTerm((byte) term);
//            request.setSchoolYear(schoolYear.getId());
        }
        Specification<Intern> specification = CustomInternRepository.filterExportIntern(request);
        List<Intern> interns = internRepository.findAll(specification);
        return excelService.writeInternToExcel(interns);
    }
}
