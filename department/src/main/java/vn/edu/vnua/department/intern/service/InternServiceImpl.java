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
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.repository.CustomInternRepository;
import vn.edu.vnua.department.intern.repository.InternRepository;
import vn.edu.vnua.department.intern.request.CreateInternRequest;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.intern.request.GetInternListRequest;
import vn.edu.vnua.department.intern.request.UpdateInternRequest;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.service.excel.ExcelService;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternServiceImpl implements InternService {
    private final InternRepository internRepository;
    private final MasterDataRepository masterDataRepository;
    private final UserRepository userRepository;
    private final ExcelService excelService;

    @Override
    public Page<Intern> getInternList(GetInternListRequest request) {
        Specification<Intern> specification = CustomInternRepository.filterInternList(request);
        return internRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public Intern createIntern(CreateInternRequest request) {
        try {
            MasterData type = masterDataRepository.findById(request.getType().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.INTERN_TYPE_NOT_FOUND));
            MasterData schoolYear = masterDataRepository.findById(request.getSchoolYear().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.SCHOOL_YEAR_NOT_FOUND));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User instructor = userRepository.findById(authentication.getPrincipal().toString()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

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
            User user = userRepository.findById(authentication.getPrincipal().toString()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

            if (user != intern.getInstructor()) {
                throw new RuntimeException(Constants.InternConstant.CANNOT_UPDATE);
            }

            MasterData type = masterDataRepository.findById(request.getType().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.INTERN_TYPE_NOT_FOUND));
            MasterData schoolYear = masterDataRepository.findById(request.getSchoolYear().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.SCHOOL_YEAR_NOT_FOUND));

            intern.setName(request.getName());
            intern.setType(type);
            intern.setSchoolYear(schoolYear);
            intern.setTerm(request.getTerm());
            intern.setOutlineFile(request.getOutlineFile());
            intern.setProgressFile(request.getProgressFile());
            intern.setFinalFile(request.getFinalFile());
            intern.setNote(request.getNote());

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
        User user = userRepository.findById(authentication.getPrincipal().toString()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

        if (user != intern.getInstructor()) {
            throw new RuntimeException(Constants.InternConstant.CANNOT_UPDATE);
        }

        internRepository.delete(intern);
        return intern;
    }

    @Override
    public String exportToExcel(ExportInternListRequest request) {
        Specification<Intern> specification = CustomInternRepository.filterExportIntern(request);
        List<Intern> interns = internRepository.findAll(specification);
        return excelService.writeInternToExcel(interns);
    }
}
