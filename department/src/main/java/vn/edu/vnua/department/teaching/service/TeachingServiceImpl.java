package vn.edu.vnua.department.teaching.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
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
import vn.edu.vnua.department.util.MyUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        if(!request.getIsAll()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User teacher = userRepository.getUserById(authentication.getPrincipal().toString());

            Timestamp currentDate = Timestamp.valueOf(LocalDateTime.now());
            int month = currentDate.getMonth() + 1;
            int year = currentDate.getYear() + 1900;
            int term;
            if(month<=5) {
                term = 2;
            } else if(month<=8) {
                term = 3;
            } else {
                term = 1;
            }
            String schoolYearName = month >= 9 ? (year+"-"+(year+1)) : ((year-1)+"-"+year);

            MasterData schoolYear = masterDataRepository.findByName(schoolYearName);

            request.setTeacherId(teacher.getId());
            request.setTerm((byte) term);
            request.setSchoolYear(schoolYear.getId());
        }
        Specification<Teaching> specification = CustomTeachingRepository.filterTeachingList(request);
        return teachingRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public Teaching createTeaching(CreateTeachingRequest request) {
        User teacher = userRepository.findById(request.getTeacher().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());
        if (createdBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            if (createdBy != teacher) {
                throw new RuntimeException(Constants.TeachingConstant.CANNOT_UPDATE);
            }
        }

        if (teachingRepository.existsBySchoolYearIdAndTermAndSubjectIdAndClassIdAndTeachingGroup(request.getSchoolYear().getId(),
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
        if (modifiedBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER) &&
                teaching.getTeacher() != modifiedBy) {
            throw new RuntimeException(Constants.TeachingConstant.CANNOT_UPDATE);
        }

        User teacher = userRepository.findById(request.getTeacher().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

        teaching.setTeacher(teacher);
        teaching.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        teaching.setModifiedBy(modifiedBy);

        if (StringUtils.hasText(request.getComponentFile())) {
            teaching.setComponentFile(request.getComponentFile());
        }
        if (StringUtils.hasText(request.getSummaryFile())) {
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User deleteBy = userRepository.getUserById(authentication.getPrincipal().toString());
        if (deleteBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER) &&
                teaching.getTeacher() != deleteBy) {
            throw new RuntimeException(Constants.TeachingConstant.CANNOT_UPDATE);
        }

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

    @Override
    public List<Teaching> readFromDaoTao(String teacherId) throws IOException {
        if(!userRepository.existsById(teacherId)) {
            throw new RuntimeException(Constants.UserConstant.USER_NOT_FOUND);
        }

        User teacher = userRepository.getUserById(teacherId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());

        if (createdBy.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            if (createdBy != teacher) {
                throw new RuntimeException(Constants.TeachingConstant.CANNOT_UPDATE);
            }
        }

        String teacherFirsName = teacher.getFirstName();
        String teacherLastName = teacher.getLastName();

        String link = "https://daotao.vnua.edu.vn/default.aspx?page=thoikhoabieu&sta=1&id=" + teacherId;
        Document document = Jsoup.connect(link).timeout(20000).get();

        String time = document.select("option[selected=selected][value=20233]").first().ownText();
        Byte term = MyUtils.parseByteFromString(time.split(" ")[2]);
        String schoolYearName = time.split(" ")[6];
        MasterData schoolYear = masterDataRepository.getByName(schoolYearName).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.SCHOOL_YEAR_NOT_FOUND));

        List<Teaching> teachingList = new ArrayList<>();
        Element table = document.select("table[width=100%][class=body-table][cellspacing=0][cellpadding=0]").first();
        if (table == null) {
            throw new RuntimeException("Chưa có thông tin giảng dạy của người dùng " + teacherId + " - " + teacherFirsName + " " + teacherLastName + " trong " + time);
        } else {
            int rowNum = document.select("td[width=56px][align=center]").size();
            for (int i = 0; i < rowNum; i++) {
                String subjectId = document.select("td[width=56px][align=center]").get(i).ownText();
                Integer teachingGroup = MyUtils.parseIntegerFromString(document.select("td[width=35px][align=center]").get(i).ownText());
                String classId = document.select("td[width=70px][align=center]").get(i).ownText();

                if (teachingRepository.existsBySchoolYearIdAndTermAndSubjectIdAndClassIdAndTeachingGroup(schoolYear.getId(),
                        term, subjectId, classId, teachingGroup)) {
                    throw new RuntimeException("Có lỗi ở bản ghi thứ " + (i + 1) + ": " + Constants.TeachingConstant.TEACHING_IS_EXISTED);
                }
                int finalI = i;
                Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("Có lỗi ở bản ghi thứ " + (finalI + 1) + ": " + Constants.SubjectConstant.SUBJECT_NOT_FOUND));

                Teaching teaching = Teaching.builder()
                        .subject(subject)
                        .teacher(teacher)
                        .classId(classId)
                        .teachingGroup(teachingGroup)
                        .schoolYear(schoolYear)
                        .term(term)
                        .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                        .createdBy(createdBy)
                        .status(Constants.StatusConstant.INCOMPLETE)
                        .build();

                teachingList.add(teaching);
            }
        }
        return teachingRepository.saveAll(teachingList);
    }
}
