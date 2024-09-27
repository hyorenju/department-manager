package vn.edu.vnua.department.user.model;

import lombok.Data;
import vn.edu.vnua.department.model.excel.ExcelData;
import vn.edu.vnua.department.user.entity.User;

@Data
public class UserExcelData extends ExcelData {
    private User user;
}
