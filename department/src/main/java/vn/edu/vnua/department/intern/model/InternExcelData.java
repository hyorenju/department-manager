package vn.edu.vnua.department.intern.model;

import lombok.Data;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.model.excel.ExcelData;
@Data
public class InternExcelData extends ExcelData {
    private Intern intern;
}
