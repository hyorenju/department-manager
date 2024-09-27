package vn.edu.vnua.department.subject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.repository.CustomClassRepository;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.service.excel.ExcelService;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.repository.CustomSubjectRepository;
import vn.edu.vnua.department.subject.repository.SubjectRepository;
import vn.edu.vnua.department.subject.request.*;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final ExcelService excelService;

    @Override
    public Page<Subject> getSubjectList(GetSubjectListRequest request) {
        Specification<Subject> specification = CustomSubjectRepository.filterSubjectList(request);
        return subjectRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public List<Subject> getSubjectSelection(GetSubjectSelectionRequest request) {
        if (StringUtils.hasText(request.getDepartmentId())) {
            return subjectRepository.findAllByDepartmentId(request.getDepartmentId());
        } else if (StringUtils.hasText(request.getFacultyId())) {
            return subjectRepository.findAllByDepartmentFacultyId(request.getFacultyId());
        } else return subjectRepository.findAll();
    }

    @Override
    public Subject createSubject(CreateSubjectRequest request) {
        if(subjectRepository.existsById(request.getId())){
            throw new RuntimeException(Constants.SubjectConstant.SUBJECT_EXISTED);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());

        Department department = createdBy.getDepartment();

        return subjectRepository.saveAndFlush(Subject.builder()
                .id(request.getId())
                .name(request.getName())
                .department(department)
                .credits(request.getCredits())
                .outline(request.getOutline())
                .lecture(request.getLecture())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .createdBy(createdBy)
                .note(request.getNote())
                .build());
    }

    @Override
    public Subject updateSubject(String id, UpdateSubjectRequest request) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.SubjectConstant.SUBJECT_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedBy = userRepository.getUserById(authentication.getPrincipal().toString());

        subject.setName(request.getName());
        subject.setCredits(request.getCredits());
        subject.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        subject.setModifiedBy(modifiedBy);
        subject.setNote(request.getNote());

        if (StringUtils.hasText(request.getOutline())) {
            subject.setOutline(request.getOutline());
        }
        if (StringUtils.hasText(request.getLecture())) {
            subject.setLecture(request.getLecture());
        }

        return subjectRepository.saveAndFlush(subject);
    }

    @Override
    public Subject deleteSubject(String id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.SubjectConstant.SUBJECT_NOT_FOUND));
        subjectRepository.delete(subject);
        return subject;
    }

    @Override
    public List<Subject> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        return subjectRepository.saveAll(excelService.readSubjectFromExcel(file));
    }

    @Override
    public String exportToExcel(ExportSubjectListRequest request) {
        Specification<Subject> specification = CustomSubjectRepository.filterExportSubject(request);
        List<Subject> subjects = subjectRepository.findAll(specification);
        return excelService.writeSubjectToExcel(subjects);
    }
}
