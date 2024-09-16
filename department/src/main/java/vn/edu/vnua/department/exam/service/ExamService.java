package vn.edu.vnua.department.exam.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.exam.request.*;
import vn.edu.vnua.department.masterdata.request.GetUsersNotAssignedRequest;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.user.entity.User;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ExamService {
    Page<Exam> getExamList(GetExamListRequest request);
    Exam createExam(CreateExamRequest request) throws ParseException;
    Exam updateExam(Long id, UpdateExamRequest request) throws ParseException;
    List<User> getUsersNotAssigned(Long id);
    List<User> getUsersNotAssigned(GetUsersNotAssignedRequest request) throws ParseException;
    Exam deleteExam(Long id);
    List<Exam> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String exportToExcel(ExportExamRequest request);
    Exam updateProctor(Long id, UpdateProctorExamRequest request);
}
