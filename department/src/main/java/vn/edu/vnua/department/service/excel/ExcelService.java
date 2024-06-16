package vn.edu.vnua.department.service.excel;

import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.user.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ExcelService {

    String writeUserToExcel(List<User> users);

    String writeClassToExcel(List<AClass> classes);

    String writeSubjectToExcel(List<Subject> subjects);

    List<Intern> readInternFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String writeInternToExcel(List<Intern> interns);

    List<Teaching> readTeachingFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String writeTeachingToExcel(List<Teaching> teachings);

    List<Exam> readExamFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String writeExamToExcel(List<Exam> exams);
}
