package vn.edu.vnua.department.subject.model;

import lombok.Data;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.model.excel.ExcelData;
import vn.edu.vnua.department.subject.entity.Subject;

@Data
public class SubjectExcelData extends ExcelData {
    private Subject subject;
}
