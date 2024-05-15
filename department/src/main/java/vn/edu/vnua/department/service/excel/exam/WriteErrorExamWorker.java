package vn.edu.vnua.department.service.excel.exam;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.exam.model.ExamExcelData;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.teaching.model.TeachingExcelData;
import vn.edu.vnua.department.util.MyUtils;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class WriteErrorExamWorker implements Callable<Void> {
    private final Row row;
    private final CellStyle cellStyle;
    private final ExamExcelData examExcelData;

    @Override
    public Void call() throws Exception {
        Exam exam = examExcelData.getExam();

        row.createCell(0).setCellValue(exam.getSchoolYear() != null ? exam.getSchoolYear().getName() : "");
        row.createCell(1).setCellValue(exam.getTerm() != null ? exam.getTerm().toString() : "");
        row.createCell(2).setCellValue(exam.getSubject() != null ? exam.getSubject().getId() : "");
        row.createCell(3).setCellValue(exam.getTestDay() != null ? MyUtils.convertTimestampToString(exam.getTestDay()) : "");
        row.createCell(4).setCellValue(exam.getLessonStart() != null ? exam.getLessonStart().toString() : "");
        row.createCell(5).setCellValue(exam.getLessonsTest() != null ? exam.getLessonsTest().toString() : "");
        row.createCell(6).setCellValue(exam.getTestRoom() != null ? exam.getTestRoom() : "");
        row.createCell(7).setCellValue(exam.getClassId() != null ? exam.getClassId() : "");
        row.createCell(8).setCellValue(exam.getExamGroup() != null ? exam.getExamGroup().toString() : "");
        row.createCell(9).setCellValue(exam.getCluster() != null ? exam.getCluster().toString() : "");
        row.createCell(10).setCellValue(exam.getQuantity() != null ? exam.getQuantity().toString() : "");
        row.createCell(11).setCellValue(exam.getForm() != null ? exam.getForm().getName() : "");
        row.createCell(12).setCellValue(exam.getLecturerTeach() != null ? exam.getLecturerTeach().getId() : "");
        row.createCell(13).setCellValue(exam.getPicker() != null ? exam.getPicker().getId() : "");
        row.createCell(14).setCellValue(exam.getPrinter() != null ? exam.getPrinter().getId() : "");
        row.createCell(15).setCellValue(exam.getProctor1() != null ? exam.getProctor1().getId() : "");
        row.createCell(16).setCellValue(exam.getProctor2() != null ? exam.getProctor2().getId() : "");
        row.createCell(17).setCellValue(exam.getMarker1() != null ? exam.getMarker1().getId() : "");
        row.createCell(18).setCellValue(exam.getMarker2() != null ? exam.getMarker2().getId() : "");
        row.createCell(19).setCellValue(exam.getQuestionTaker() != null ? exam.getQuestionTaker().getId() : "");
        row.createCell(20).setCellValue(exam.getExamTaker() != null ? exam.getExamTaker().getId() : "");
        row.createCell(21).setCellValue(exam.getExamGiver() != null ? exam.getExamGiver().getId() : "");
        row.createCell(22).setCellValue(exam.getPointGiver() != null ? exam.getPointGiver().getId() : "");
        row.createCell(23).setCellValue("");

        examExcelData.getErrorDetailList().forEach(errorDetail -> {
            Cell cell = row.getCell(errorDetail.getColumnIndex());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(errorDetail.getErrorMsg());
        });

        return null;
    }
}
