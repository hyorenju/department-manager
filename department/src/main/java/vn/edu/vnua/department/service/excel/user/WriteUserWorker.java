package vn.edu.vnua.department.service.excel.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.util.MyUtils;

import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class WriteUserWorker implements Callable<Void> {
    private Row row;
    private User user;

    @Override
    public Void call() {
        row.createCell(0).setCellValue(user.getId());
        row.createCell(1).setCellValue(user.getFirstName() !=null ? user.getFirstName() : "");
        row.createCell(2).setCellValue(user.getLastName() != null ? user.getLastName() : "");
        row.createCell(3).setCellValue(user.getDepartment().getFaculty().getName() != null ? user.getDepartment().getFaculty().getName() : "");
        row.createCell(4).setCellValue(user.getDepartment().getName() != null ? user.getDepartment().getName() : "");
        row.createCell(5).setCellValue(user.getDegree().getName() != null ? user.getDegree().getName() : "");
        row.createCell(6).setCellValue(user.getEmail() != null ? user.getEmail() : "");
        row.createCell(7).setCellValue(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
        row.createCell(8).setCellValue(user.getRole().getName() != null ? user.getRole().getName() : "");
        row.createCell(9).setCellValue(user.getNote() != null ? user.getNote() : "");
        return null;
    }
}
