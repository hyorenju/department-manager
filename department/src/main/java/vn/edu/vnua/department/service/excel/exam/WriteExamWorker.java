package vn.edu.vnua.department.service.excel.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.util.MyUtils;

import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class WriteExamWorker implements Callable<Void> {
    private Row row;
    private Exam exam;

    @Override
    public Void call() {
        row.createCell(0).setCellValue(exam.getSchoolYear().getName() != null ? exam.getSchoolYear().getName() : "");
        row.createCell(1).setCellValue(exam.getTerm() !=null ? exam.getTerm().toString() : "");
        row.createCell(2).setCellValue(exam.getSubject().getDepartment().getFaculty().getName() != null ? exam.getSubject().getDepartment().getFaculty().getName() : "");
        row.createCell(3).setCellValue(exam.getSubject().getDepartment().getName() != null ? exam.getSubject().getDepartment().getName() : "");
        row.createCell(4).setCellValue(exam.getSubject().getName() != null ? exam.getSubject().getName() : "");
        row.createCell(5).setCellValue(exam.getForm().getName() != null ? exam.getForm().getName() : "");
        row.createCell(6).setCellValue(exam.getTestDay() != null ? MyUtils.convertTimestampToString(exam.getTestDay()) : "");
        row.createCell(7).setCellValue(exam.getTestRoom() != null ? exam.getTestRoom() : "");
        row.createCell(8).setCellValue(exam.getLessonStart() != null ? exam.getLessonStart().toString() : "");
        row.createCell(9).setCellValue(exam.getLessonsTest() != null ? exam.getLessonsTest().toString() : "");
        row.createCell(10).setCellValue(exam.getClassId() != null ? exam.getClassId() : "");
        row.createCell(11).setCellValue(exam.getExamGroup() != null ? exam.getExamGroup().toString() : "");
        row.createCell(12).setCellValue(exam.getCluster() != null ? exam.getCluster().toString() : "");
        row.createCell(13).setCellValue(exam.getQuantity() != null ? exam.getQuantity().toString() : "");
        row.createCell(14).setCellValue((exam.getLecturerTeach().getFirstName() != null || exam.getLecturerTeach().getLastName() != null) ? exam.getLecturerTeach().getFirstName() + exam.getLecturerTeach().getLastName() : "");
        row.createCell(15).setCellValue((exam.getProctor1().getFirstName() != null || exam.getProctor1().getLastName() != null) ? exam.getProctor1().getFirstName() + exam.getProctor1().getLastName() : "");
        row.createCell(16).setCellValue((exam.getProctor2().getFirstName() != null || exam.getProctor2().getLastName() != null) ? exam.getProctor2().getFirstName() + exam.getProctor2().getLastName() : "");
        row.createCell(17).setCellValue((exam.getMarker1().getFirstName() != null || exam.getMarker1().getLastName() != null) ? exam.getMarker1().getFirstName() + exam.getMarker1().getLastName() : "");
        row.createCell(18).setCellValue((exam.getMarker2().getFirstName() != null || exam.getMarker2().getLastName() != null) ? exam.getMarker2().getFirstName() + exam.getMarker2().getLastName() : "");
        row.createCell(19).setCellValue((exam.getPicker().getFirstName() != null || exam.getPicker().getLastName() != null) ? exam.getPicker().getFirstName() + exam.getPicker().getLastName() : "");
        row.createCell(20).setCellValue((exam.getPrinter().getFirstName() != null || exam.getPrinter().getLastName() != null) ? exam.getPrinter().getFirstName() + exam.getPrinter().getLastName() : "");
        row.createCell(21).setCellValue((exam.getQuestionTaker().getFirstName() != null || exam.getQuestionTaker().getLastName() != null) ? exam.getQuestionTaker().getFirstName() + exam.getQuestionTaker().getLastName() : "");
        row.createCell(22).setCellValue((exam.getExamTaker().getFirstName() != null || exam.getExamTaker().getLastName() != null) ? exam.getExamTaker().getFirstName() + exam.getExamTaker().getLastName() : "");
        row.createCell(23).setCellValue((exam.getExamGiver().getFirstName() != null || exam.getExamGiver().getLastName() != null) ? exam.getExamGiver().getFirstName() + exam.getExamGiver().getLastName() : "");
        row.createCell(24).setCellValue((exam.getPointGiver().getFirstName() != null || exam.getPointGiver().getLastName() != null) ? exam.getPointGiver().getFirstName() + exam.getPointGiver().getLastName() : "");
        row.createCell(25).setCellValue(exam.getNote() != null ? exam.getNote() : "");
        return null;
    }
}
