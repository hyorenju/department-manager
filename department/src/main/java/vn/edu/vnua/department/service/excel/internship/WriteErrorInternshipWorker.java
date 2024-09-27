package vn.edu.vnua.department.service.excel.internship;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.model.InternExcelData;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class WriteErrorInternshipWorker implements Callable<Void> {
    private final Row row;
    private final CellStyle cellStyle;
    private final InternExcelData internExcelData;

    @Override
    public Void call() throws Exception {
        Intern intern = internExcelData.getIntern();

        row.createCell(0).setCellValue(intern.getSchoolYear() != null ? intern.getSchoolYear().getName() : "");
        row.createCell(1).setCellValue(intern.getTerm() != null ? intern.getTerm().toString() : "");
        row.createCell(2).setCellValue(intern.getName() != null ?  intern.getName() : "");
        row.createCell(3).setCellValue(intern.getType() != null ? intern.getType().getName() : "");
        row.createCell(4).setCellValue(intern.getInstructor() != null ? intern.getInstructor().getId() : "");
        row.createCell(5).setCellValue("");

        internExcelData.getErrorDetailList().forEach(errorDetail -> {
            Cell cell = row.getCell(errorDetail.getColumnIndex());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(errorDetail.getErrorMsg());
        });

        return null;
    }
}
