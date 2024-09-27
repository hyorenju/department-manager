package vn.edu.vnua.department.internship.model;

import lombok.Data;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.model.excel.ExcelData;

@Data
public class InternshipExcelData extends ExcelData {
    private Internship intern;
}
