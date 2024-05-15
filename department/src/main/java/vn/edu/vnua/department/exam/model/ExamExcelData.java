package vn.edu.vnua.department.exam.model;

import lombok.Data;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.model.excel.ExcelData;

@Data
public class ExamExcelData extends ExcelData {
    private Exam exam;
}
