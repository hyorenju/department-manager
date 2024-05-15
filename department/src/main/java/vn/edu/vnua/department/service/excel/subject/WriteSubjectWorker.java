package vn.edu.vnua.department.service.excel.subject;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.user.entity.User;

import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class WriteSubjectWorker implements Callable<Void> {
    private Row row;
    private Subject subject;

    @Override
    public Void call() {
        row.createCell(0).setCellValue(subject.getId());
        row.createCell(1).setCellValue(subject.getName() !=null ? subject.getName() : "");
        row.createCell(2).setCellValue(subject.getDepartment().getFaculty().getName() !=null ? subject.getDepartment().getFaculty().getName() : "");
        row.createCell(3).setCellValue(subject.getDepartment().getName() != null ? subject.getDepartment().getName() : "");
        row.createCell(4).setCellValue(subject.getCredits() != null ? subject.getCredits().toString() : "");
        row.createCell(5).setCellValue(subject.getNote() != null ? subject.getNote() : "");
        return null;
    }
}
