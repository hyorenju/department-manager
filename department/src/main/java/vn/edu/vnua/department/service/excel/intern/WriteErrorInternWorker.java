package vn.edu.vnua.department.service.excel.intern;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.model.InternExcelData;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.internship.model.InternshipExcelData;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.teaching.model.TeachingExcelData;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class WriteErrorInternWorker implements Callable<Void> {
    private final Row row;
    private final CellStyle cellStyle;
    private final InternshipExcelData internExcelData;

    @Override
    public Void call() throws Exception {
        Internship intern = internExcelData.getIntern();

        row.createCell(0).setCellValue(intern.getSchoolYear() != null ? intern.getSchoolYear().getName() : "");
        row.createCell(1).setCellValue(intern.getTerm() != null ? intern.getTerm().toString() : "");
        row.createCell(2).setCellValue(intern.getName() != null ?  intern.getName() : "");
        row.createCell(3).setCellValue(intern.getType() != null ? intern.getType().getName() : "");
        row.createCell(4).setCellValue(intern.getInstructor() != null ? intern.getInstructor().getId() : "");
        row.createCell(5).setCellValue(intern.getStudentName() != null ? intern.getStudentName() : "");
        row.createCell(6).setCellValue(intern.getStudentId() != null ? intern.getStudentId() : "");
        row.createCell(7).setCellValue(intern.getClassId() != null ? intern.getClassId() : "");
        row.createCell(8).setCellValue(intern.getPhone() != null ? intern.getPhone() : "");
        row.createCell(9).setCellValue(intern.getCompany() != null ? intern.getCompany() : "");

        row.createCell(10).setCellValue("");

        internExcelData.getErrorDetailList().forEach(errorDetail -> {
            Cell cell = row.getCell(errorDetail.getColumnIndex());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(errorDetail.getErrorMsg());
        });

        return null;
    }
}
