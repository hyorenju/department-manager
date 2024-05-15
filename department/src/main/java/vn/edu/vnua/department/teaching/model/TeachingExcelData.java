package vn.edu.vnua.department.teaching.model;

import lombok.Data;
import vn.edu.vnua.department.model.excel.ExcelData;
import vn.edu.vnua.department.teaching.entity.Teaching;

@Data
public class TeachingExcelData extends ExcelData {
    private Teaching teaching;
}
