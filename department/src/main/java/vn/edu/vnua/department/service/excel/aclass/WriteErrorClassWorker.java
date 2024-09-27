package vn.edu.vnua.department.service.excel.aclass;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.model.ClassExcelData;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.model.UserExcelData;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class WriteErrorClassWorker implements Callable<Void> {
    private final Row row;
    private final CellStyle cellStyle;
    private final ClassExcelData classExcelData;

    @Override
    public Void call() throws Exception {
        AClass aClass = classExcelData.getAClass();

        row.createCell(0).setCellValue(aClass.getId() != null ? aClass.getId() : "");
        row.createCell(1).setCellValue(aClass.getName() != null ? aClass.getName() : "");
        row.createCell(2).setCellValue(aClass.getFaculty() != null ?  aClass.getFaculty().getName() : "");
        row.createCell(3).setCellValue(aClass.getHrTeacher() != null ? aClass.getHrTeacher() : "");
        row.createCell(4).setCellValue(aClass.getMonitor() != null ? aClass.getMonitor() : "");
        row.createCell(5).setCellValue(aClass.getMonitorPhone() != null ? aClass.getMonitorPhone() : "");
        row.createCell(6).setCellValue(aClass.getMonitorEmail() != null ? aClass.getMonitorEmail() : "");
        row.createCell(7).setCellValue("");

        classExcelData.getErrorDetailList().forEach(errorDetail -> {
            Cell cell = row.getCell(errorDetail.getColumnIndex());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(errorDetail.getErrorMsg());
        });

        return null;
    }
}
