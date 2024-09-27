package vn.edu.vnua.department.aclass.model;

import lombok.Data;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.model.excel.ExcelData;

@Data
public class ClassExcelData extends ExcelData {
    private AClass aClass;
}
