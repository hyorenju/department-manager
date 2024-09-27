package vn.edu.vnua.department.service.excel.user;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.internship.model.InternshipExcelData;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.model.UserExcelData;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class WriteErrorUserWorker implements Callable<Void> {
    private final Row row;
    private final CellStyle cellStyle;
    private final UserExcelData userExcelData;

    @Override
    public Void call() throws Exception {
        User user = userExcelData.getUser();

        row.createCell(0).setCellValue(user.getId() != null ? user.getId() : "");
        row.createCell(1).setCellValue(user.getFirstName() != null ? user.getFirstName() : "");
        row.createCell(2).setCellValue(user.getLastName() != null ?  user.getLastName() : "");
        row.createCell(3).setCellValue(user.getDegree() != null ? user.getDegree().getName() : "");
        row.createCell(4).setCellValue(user.getEmail() != null ? user.getEmail() : "");
        row.createCell(5).setCellValue(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
        row.createCell(6).setCellValue(user.getDepartment() != null ? user.getDepartment().getName() : "");
        row.createCell(7).setCellValue("");

        userExcelData.getErrorDetailList().forEach(errorDetail -> {
            Cell cell = row.getCell(errorDetail.getColumnIndex());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(errorDetail.getErrorMsg());
        });

        return null;
    }
}
