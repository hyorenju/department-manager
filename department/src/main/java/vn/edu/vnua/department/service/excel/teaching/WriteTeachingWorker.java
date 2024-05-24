package vn.edu.vnua.department.service.excel.teaching;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.user.entity.User;

import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class WriteTeachingWorker implements Callable<Void> {
    private Row row;
    private Teaching teaching;

    @Override
    public Void call() {
        row.createCell(0).setCellValue(teaching.getSchoolYear().getName() != null ? teaching.getSchoolYear().getName() : "");
        row.createCell(1).setCellValue(teaching.getTerm() !=null ? teaching.getTerm().toString() : "");
        row.createCell(2).setCellValue(teaching.getSubject().getDepartment().getFaculty().getName() != null ? teaching.getSubject().getDepartment().getFaculty().getName() : "");
        row.createCell(3).setCellValue(teaching.getSubject().getDepartment().getName() != null ? teaching.getSubject().getDepartment().getName() : "");
        row.createCell(4).setCellValue(teaching.getSubject().getId() != null ? teaching.getSubject().getId() : "");
        row.createCell(5).setCellValue(teaching.getSubject().getName() != null ? teaching.getSubject().getName() : "");
        row.createCell(6).setCellValue((teaching.getTeacher().getFirstName() != null || teaching.getTeacher().getLastName() != null) ? teaching.getTeacher().getFirstName() + " " + teaching.getTeacher().getLastName() : "");
        row.createCell(7).setCellValue(teaching.getClassId() != null ? teaching.getClassId() : "");
        row.createCell(8).setCellValue(teaching.getTeachingGroup() != null ? teaching.getTeachingGroup().toString() : "");
        row.createCell(9).setCellValue(teaching.getStatus() != null ? teaching.getStatus() : "");
        row.createCell(10).setCellValue(teaching.getNote() != null ? teaching.getNote() : "");
        return null;
    }
}
