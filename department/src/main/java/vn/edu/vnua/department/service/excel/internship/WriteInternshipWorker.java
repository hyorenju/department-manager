package vn.edu.vnua.department.service.excel.internship;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.intern.entity.Intern;

import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class WriteInternshipWorker implements Callable<Void> {
    private Row row;
    private Intern intern;

    @Override
    public Void call() {
        row.createCell(0).setCellValue(intern.getSchoolYear().getName() != null ? intern.getSchoolYear().getName() : "");
        row.createCell(1).setCellValue(intern.getTerm() !=null ? intern.getTerm().toString() : "");
        row.createCell(2).setCellValue(intern.getInstructor().getDepartment().getFaculty().getName() != null ? intern.getInstructor().getDepartment().getFaculty().getName() : "");
        row.createCell(3).setCellValue(intern.getInstructor().getDepartment().getName() != null ? intern.getInstructor().getDepartment().getName() : "");
        row.createCell(4).setCellValue((intern.getInstructor().getFirstName() != null || intern.getInstructor().getLastName() != null) ? intern.getInstructor().getFirstName() + " " + intern.getInstructor().getLastName() : "");
        row.createCell(5).setCellValue(intern.getName() != null ? intern.getName() : "");
        row.createCell(6).setCellValue(intern.getType().getName() != null ? intern.getType().getName() : "");
        row.createCell(7).setCellValue(intern.getStatus() != null ? intern.getStatus() : "");
        row.createCell(8).setCellValue(intern.getNote() != null ? intern.getNote() : "");
        return null;
    }
}
