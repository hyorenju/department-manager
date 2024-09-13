package vn.edu.vnua.department.aclass.request;

import lombok.Data;

@Data
public class ExportClassListRequest {
    private String keyword;
    private String id;
    private String name;
    private String hrTeacher;
    private String monitor;
    private String facultyId;
}
