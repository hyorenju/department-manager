package vn.edu.vnua.department.service.excel.teaching;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.teaching.model.TeachingExcelData;
import vn.edu.vnua.department.util.MyUtils;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class WriteErrorTeachingWorker implements Callable<Void> {
    private final Row row;
    private final CellStyle cellStyle;
    private final TeachingExcelData teachingExcelData;

    @Override
    public Void call() throws Exception {
        Teaching teaching = teachingExcelData.getTeaching();

        row.createCell(0).setCellValue(teaching.getSchoolYear() != null ? teaching.getSchoolYear().getName() : "");
        row.createCell(1).setCellValue(teaching.getTerm() != null ? teaching.getTerm().toString() : "");
        row.createCell(2).setCellValue(teaching.getTeacher() != null ?  teaching.getTeacher().getId() : "");
        row.createCell(3).setCellValue(teaching.getSubject() != null ? teaching.getSubject().getId() : "");
        row.createCell(4).setCellValue(teaching.getTeachingGroup() != null ? teaching.getTeachingGroup().toString() : "");
        row.createCell(5).setCellValue(teaching.getClassId() != null ? teaching.getClassId() : "");
//        row.createCell(6).setCellValue(teaching.getDeadline() != null ? MyUtils.convertTimestampToString(teaching.getDeadline()) : "");
        row.createCell(6).setCellValue("");

        teachingExcelData.getErrorDetailList().forEach(errorDetail -> {
            Cell cell = row.getCell(errorDetail.getColumnIndex());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(errorDetail.getErrorMsg());
        });

        return null;
    }
}
