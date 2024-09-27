package vn.edu.vnua.department.service.excel.subject;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.model.SubjectExcelData;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class WriteErrorSubjectWorker implements Callable<Void> {
    private final Row row;
    private final CellStyle cellStyle;
    private final SubjectExcelData subjectExcelData;

    @Override
    public Void call() throws Exception {
        Subject subject = subjectExcelData.getSubject();

        row.createCell(0).setCellValue(subject.getId() != null ? subject.getId() : "");
        row.createCell(1).setCellValue(subject.getName() != null ? subject.getName() : "");
        row.createCell(2).setCellValue(subject.getDepartment() != null ?  subject.getDepartment().getName() : "");
        row.createCell(3).setCellValue(subject.getCredits() != null ? subject.getCredits().toString() : "");
        row.createCell(4).setCellValue("");

        subjectExcelData.getErrorDetailList().forEach(errorDetail -> {
            Cell cell = row.getCell(errorDetail.getColumnIndex());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(errorDetail.getErrorMsg());
        });

        return null;
    }
}
