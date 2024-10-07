package vn.edu.vnua.department.service.excel.exam;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.exam.model.ExamExcelData;
import vn.edu.vnua.department.exam.repository.ExamRepository;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.repository.SubjectRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.util.MyUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
public class StoreExamWorker implements Callable<ExamExcelData> {
    private final User createdBy;
    private final ExamRepository examRepository;
    private final MasterDataRepository masterDataRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final String examStr;
    private final int row;

    @Override
    public ExamExcelData call() throws Exception {
        ExamExcelData examExcelData = new ExamExcelData();

        if (!examStr.isEmpty()) {
            String[] infoList = examStr.strip().split(Constants.AppendCharacterConstant.APPEND_CHARACTER);

            String schoolYearName = infoList[0].strip();
            String term = infoList[1].strip();
            String subjectId = infoList[2].strip();
            String testDay = infoList[3].strip();
            String lessonStart = infoList[4].strip();
            String lessonsTest = infoList[5].strip();
            String testRoom = infoList[6].strip();
            String classId = infoList[7].strip();
            String examGroup = infoList[8].strip();
            String cluster = infoList[9].strip();
            String quantity = infoList[10].strip();
            String examFormName = infoList[11].strip();
            String examCode = infoList[12].strip();
            String lecturerTeachId = infoList[13].strip();
            String pickerId = infoList[14].strip();
            String printerId = infoList[15].strip();
            String proctor1Id = infoList[16].strip();
            String proctor2Id = infoList[17].strip();
            String marker1Id = infoList[18].strip();
            String marker2Id = infoList[19].strip();
            String questionTakerId = infoList[20].strip();
            String examTakerId = infoList[21].strip();
            String examGiverId = infoList[22].strip();
            String pointGiverId = infoList[23].strip();
            String deadline = infoList[24].strip();

            MasterData schoolYear = masterDataRepository.findByName(schoolYearName);
            Subject subject = subjectRepository.getSubjectById(subjectId);
            MasterData examForm = masterDataRepository.findByName(examFormName);
            User lecturerTeach = userRepository.getUserById(lecturerTeachId);
            User picker = userRepository.getUserById(pickerId);
            User printer = userRepository.getUserById(printerId);
            User proctor1 = userRepository.getUserById(proctor1Id);
            User proctor2 = userRepository.getUserById(proctor2Id);
            User marker1 = userRepository.getUserById(marker1Id);
            User marker2 = userRepository.getUserById(marker2Id);
            User questionTaker = userRepository.getUserById(questionTakerId);
            User examTaker = userRepository.getUserById(examTakerId);
            User examGiver = userRepository.getUserById(examGiverId);
            User pointGiver = userRepository.getUserById(pointGiverId);

            Exam exam = Exam.builder()
                    .schoolYear(schoolYear)
                    .term(StringUtils.hasText(term) ? MyUtils.parseByteFromString(term) : null)
                    .subject(subject)
                    .testDay(StringUtils.hasText(testDay) ? MyUtils.convertTimestampFromExcel(testDay) : null)
                    .lessonStart(StringUtils.hasText(lessonStart) ? MyUtils.parseIntegerFromString(lessonStart) : null)
                    .lessonsTest(StringUtils.hasText(lessonsTest) ? MyUtils.parseIntegerFromString(lessonsTest) : null)
                    .testRoom(testRoom)
                    .classId(classId)
                    .examGroup(StringUtils.hasText(examGroup) ? MyUtils.parseIntegerFromString(examGroup) : null)
                    .cluster(StringUtils.hasText(cluster) ? MyUtils.parseIntegerFromString(cluster) : null)
                    .quantity(StringUtils.hasText(quantity) ? MyUtils.parseIntegerFromString(quantity) : null)
                    .form(examForm)
                    .examCode(examCode)
                    .lecturerTeach(lecturerTeach)
                    .deadline(MyUtils.convertTimestampFromExcel(deadline))
                    .picker(picker)
                    .printer(printer)
                    .proctor1(proctor1)
                    .proctor2(proctor2)
                    .marker1(marker1)
                    .marker2(marker2)
                    .questionTaker(questionTaker)
                    .examTaker(examTaker)
                    .examGiver(examGiver)
                    .pointGiver(pointGiver)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .isWarning(true)
                    .createdBy(createdBy)
                    .build();

            List<ExamExcelData.ErrorDetail> errorDetailList = exam.validateInformationDetailError(new CopyOnWriteArrayList<>());
            if (schoolYear == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(0).errorMsg("Học kỳ không hợp lệ").build());
            }
            if (subject == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(2).errorMsg("Môn học không tồn tại").build());
            }
            if (examForm == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(11).errorMsg("Hình thức không tồn tại").build());
            }
            if (!StringUtils.hasText(lecturerTeachId)) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(13).errorMsg("GV giảng dạy không được để trống").build());
            }
            if (StringUtils.hasText(lecturerTeachId) && lecturerTeach == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(13).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(pickerId) && picker == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(14).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(printerId) && printer == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(15).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(proctor1Id) && proctor1 == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(16).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(proctor2Id) && proctor2 == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(17).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(marker1Id) && marker1 == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(18).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(marker2Id) && marker2 == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(19).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(questionTakerId) && questionTaker == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(20).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(examTakerId) && examTaker == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(21).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(examGiverId) && examGiver == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(22).errorMsg("GV không tồn tại").build());
            }
            if (StringUtils.hasText(pointGiverId) && pointGiver == null) {
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(23).errorMsg("GV không tồn tại").build());
            }
            if(examRepository.existsBySubjectIdAndExamGroupAndSchoolYearIdAndTermAndCluster(
                    subject != null ? exam.getSubject().getId() : null, exam.getExamGroup(), schoolYear != null ? exam.getSchoolYear().getId(): null, exam.getTerm(), exam.getCluster()
            )){
                errorDetailList.add(ExamExcelData.ErrorDetail.builder().columnIndex(25).errorMsg("Phân công đã tồn tại").build());
            }

            examExcelData.setExam(exam);
            if (!errorDetailList.isEmpty()) {
                examExcelData.setErrorDetailList(errorDetailList);
                examExcelData.setValid(false);
            }
            examExcelData.setRowIndex(row);
        }

        return examExcelData;
    }
}
