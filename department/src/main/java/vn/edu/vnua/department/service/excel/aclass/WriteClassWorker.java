package vn.edu.vnua.department.service.excel.aclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.user.entity.User;

import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class WriteClassWorker implements Callable<Void> {
    private Row row;
    private AClass aClass;

    @Override
    public Void call() {
        row.createCell(0).setCellValue(aClass.getId());
        row.createCell(1).setCellValue(aClass.getName() !=null ? aClass.getName() : "");
        row.createCell(2).setCellValue(aClass.getFaculty().getName() != null ? aClass.getFaculty().getName() : "");
        row.createCell(3).setCellValue(aClass.getHrTeacher() != null ? aClass.getHrTeacher() : "");
        row.createCell(4).setCellValue(aClass.getMonitor() != null ? aClass.getMonitor() : "");
        row.createCell(5).setCellValue(aClass.getMonitorPhone() != null ? aClass.getMonitorPhone() : "");
        row.createCell(6).setCellValue(aClass.getMonitorEmail() != null ? aClass.getMonitorEmail() : "");
        row.createCell(7).setCellValue(aClass.getNote() != null ? aClass.getNote() : "");
        return null;
    }
}
