package vn.edu.vnua.department.exam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.exam.repository.CustomExamRepository;
import vn.edu.vnua.department.exam.repository.ExamRepository;
import vn.edu.vnua.department.exam.request.*;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.service.excel.ExcelService;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.repository.SubjectRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.util.MyUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final MasterDataRepository masterDataRepository;
    private final SubjectRepository subjectRepository;
    private final ExcelService excelService;

    @Override
    public Page<Exam> getExamList(GetExamListRequest request) {
        if(!request.getIsAll()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User proctor = userRepository.getUserById(authentication.getPrincipal().toString());

            Timestamp currentDate = Timestamp.valueOf(LocalDateTime.now());
            int month = currentDate.getMonth() + 1;
            int year = currentDate.getYear() + 1900;
            int term = month > 6 ? 1 : 2;
            String schoolYearName = month > 6 ? (year+"-"+(year+1)) : ((year-1)+"-"+year);

            MasterData schoolYear = masterDataRepository.findByName(schoolYearName);

            request.setProctorId(proctor.getId());
            request.setTerm((byte) term);
            request.setSchoolYear(schoolYear.getId());
        }
        Specification<Exam> specification = CustomExamRepository.filterExamList(request);
        return examRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public Exam createExam(CreateExamRequest request) throws ParseException {
        if(examRepository.existsBySubjectIdAndClassIdAndExamGroupAndSchoolYearIdAndTermAndCluster(
                request.getSubject().getId(), request.getClassId(), request.getExamGroup(), request.getSchoolYear().getId(), request.getTerm(), request.getCluster()
        )) {
            throw new RuntimeException(Constants.ExamConstant.EXAM_IS_EXISTED);
        }

        Subject subject = subjectRepository.findById(request.getSubject().getId()).orElseThrow(() -> new RuntimeException(Constants.SubjectConstant.SUBJECT_NOT_FOUND));
        MasterData schoolYear = masterDataRepository.findById(request.getSchoolYear().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.SCHOOL_YEAR_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());

        return examRepository.saveAndFlush(Exam.builder()
                .subject(subject)
                .schoolYear(schoolYear)
                .term(request.getTerm())
                .testRoom(request.getTestRoom())
                .testDay(MyUtils.convertTimestampFromString(request.getTestDay()))
                .lessonStart(request.getLessonStart())
                .lessonsTest(request.getLessonsTest())
                .classId(request.getClassId())
                .examGroup(request.getExamGroup())
                .quantity(request.getQuantity())
                .cluster(request.getCluster())
                .note(request.getNote())

                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .createdBy(createdBy)

                .build());
    }

    @Override
    public Exam updateExam(Long id, UpdateExamRequest request) {
        Exam exam = examRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.ExamConstant.EXAM_NOT_FOUND));
        if(request.getProctor1().getId().equals(request.getProctor2().getId())){
            throw new RuntimeException(Constants.ExamConstant.PROCTORS_NOT_BE_SAME);
        }

        User lecturerTeach = userRepository.findById(request.getLecturerTeach().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User proctor1 = userRepository.findById(request.getProctor1().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User proctor2 = userRepository.findById(request.getProctor2().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User marker1 = userRepository.findById(request.getMarker1().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User marker2 = userRepository.findById(request.getMarker2().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User picker = userRepository.findById(request.getPicker().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User printer = userRepository.findById(request.getPicker().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User questionTaker = userRepository.findById(request.getPicker().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User examTaker = userRepository.findById(request.getPicker().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User examGiver = userRepository.findById(request.getPicker().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User pointGiver = userRepository.findById(request.getPicker().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

        MasterData form = masterDataRepository.findById(request.getForm().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.EXAM_FORM_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedBy = userRepository.getUserById(authentication.getPrincipal().toString());

        exam.setTestRoom(request.getTestRoom());
        exam.setQuantity(request.getQuantity());
        exam.setForm(form);
        exam.setExamCode(request.getExamCode());
        exam.setLecturerTeach(lecturerTeach);
        exam.setProctor1(proctor1);
        exam.setProctor2(proctor2);
        exam.setMarker1(marker1);
        exam.setMarker2(marker2);
        exam.setPicker(picker);
        exam.setPrinter(printer);
        exam.setQuestionTaker(questionTaker);
        exam.setExamTaker(examTaker);
        exam.setExamGiver(examGiver);
        exam.setPointGiver(pointGiver);
        exam.setNote(request.getNote());

        exam.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        exam.setModifiedBy(modifiedBy);

        return examRepository.saveAndFlush(exam);
    }

    @Override
    public List<User> getUsersNotAssigned(Long id) {
        Exam examNotAssigned = examRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.ExamConstant.EXAM_NOT_FOUND));
        Timestamp testDay = examNotAssigned.getTestDay();
        Integer lessonStartNotAssigned = examNotAssigned.getLessonStart();
        Integer lessonsTestNotAssigned = examNotAssigned.getLessonsTest();
        List<Integer> lessonSeriesNotAssigned = createLessonSeries(lessonStartNotAssigned, lessonsTestNotAssigned);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User assigner = userRepository.getUserById(authentication.getPrincipal().toString());

        Department department = assigner.getDepartment();
        List<User> usersNotAssigned = userRepository.findAllByDepartment(department);
        List<Exam> exams = examRepository.findAllByTestDay(testDay);


        for (Exam exam :
                exams) {
            Integer lessonStart = exam.getLessonStart();
            Integer lessonsTest = exam.getLessonsTest();
            List<Integer> lessonSeries = createLessonSeries(lessonStart, lessonsTest);
            for (int i = 0; i < lessonSeries.size(); i++) {
                for (int j = 0; j < lessonSeriesNotAssigned.size(); j++) {
                    if (lessonSeries.get(i).equals(lessonSeriesNotAssigned.get(j))) {
                        usersNotAssigned.remove(exam.getProctor1());
                        usersNotAssigned.remove(exam.getProctor2());
                    }
                }
            }
        }

        return usersNotAssigned;
    }

    @Override
    public Exam deleteExam(Long id) {
        Exam exam = examRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.ExamConstant.EXAM_NOT_FOUND));
        examRepository.delete(exam);
        return exam;
    }

    @Override
    public List<Exam> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        return examRepository.saveAll(excelService.readExamFromExcel(file));
    }

    @Override
    public String exportToExcel(ExportExamRequest request) {
        if(!request.getIsAll()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User proctor = userRepository.getUserById(authentication.getPrincipal().toString());

            Timestamp currentDate = Timestamp.valueOf(LocalDateTime.now());
            int month = currentDate.getMonth() + 1;
            int year = currentDate.getYear() + 1900;
            int term = month > 6 ? 1 : 2;
            String schoolYearName = month > 6 ? (year+"-"+(year+1)) : ((year-1)+"-"+year);

            MasterData schoolYear = masterDataRepository.findByName(schoolYearName);

            request.setProctorId(proctor.getId());
            request.setTerm((byte) term);
            request.setSchoolYear(schoolYear.getId());
        }
        Specification<Exam> specification = CustomExamRepository.filterExportExam(request);
        List<Exam> exams = examRepository.findAll(specification);
        return excelService.writeExamToExcel(exams);
    }

    @Override
    public Exam updateProctor(Long id, UpdateProctorExamRequest request) {
        Exam exam = examRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.ExamConstant.EXAM_NOT_FOUND));
        if(request.getProctor1().getId().equals(request.getProctor2().getId())){
            throw new RuntimeException(Constants.ExamConstant.PROCTORS_NOT_BE_SAME);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedBy = userRepository.getUserById(authentication.getPrincipal().toString());

        User proctor1 = userRepository.findById(request.getProctor1().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        User proctor2 = userRepository.findById(request.getProctor2().getId()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

        exam.setProctor1(proctor1);
        exam.setProctor2(proctor2);
        exam.setModifiedBy(modifiedBy);

        return examRepository.saveAndFlush(exam);
    }

    private List<Integer> createLessonSeries(Integer lessonStart, Integer lessonsTest) {
        List<Integer> lessonSeries = new ArrayList<>();
        for (Integer i = lessonStart; i < (lessonStart + lessonsTest); i++) {
            lessonSeries.add(i);
        }
        return lessonSeries;
    }
}
