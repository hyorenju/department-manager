package vn.edu.vnua.department.service.excel.intern;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.user.entity.User;

import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class WriteInternWorker implements Callable<Void> {
    private Row row;
    private Internship intern;

    @Override
    public Void call() {
        row.createCell(0).setCellValue(intern.getSchoolYear().getName() != null ? intern.getSchoolYear().getName() : "");
        row.createCell(1).setCellValue(intern.getTerm() !=null ? intern.getTerm().toString() : "");
        row.createCell(2).setCellValue(intern.getInstructor().getDepartment().getFaculty().getName() != null ? intern.getInstructor().getDepartment().getFaculty().getName() : "");
        row.createCell(3).setCellValue(intern.getInstructor().getDepartment().getName() != null ? intern.getInstructor().getDepartment().getName() : "");
        row.createCell(4).setCellValue(intern.getStudentId() != null ? intern.getStudentId() : "");
        row.createCell(5).setCellValue(intern.getStudentName() != null ? intern.getStudentName() : "");
        row.createCell(6).setCellValue(intern.getClassId() != null ? intern.getClassId() : "");
        row.createCell(7).setCellValue(intern.getPhone() != null ? intern.getPhone() : "");
        row.createCell(8).setCellValue(intern.getCompany() != null ? intern.getCompany() : "");
        row.createCell(9).setCellValue((intern.getInstructor().getFirstName() != null || intern.getInstructor().getLastName() != null) ? intern.getInstructor().getFirstName() + " " + intern.getInstructor().getLastName() : "");
        row.createCell(10).setCellValue(intern.getName() != null ? intern.getName() : "");
        row.createCell(11).setCellValue(intern.getType().getName() != null ? intern.getType().getName() : "");
        row.createCell(12).setCellValue(intern.getStatus() != null ? intern.getStatus() : "");
        row.createCell(13).setCellValue(intern.getNote() != null ? intern.getNote() : "");
        return null;
    }
}
