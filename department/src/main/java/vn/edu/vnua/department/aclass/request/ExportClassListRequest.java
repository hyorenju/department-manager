package vn.edu.vnua.department.aclass.request;

import lombok.Data;

@Data
public class ExportClassListRequest {
    private String keyword;
    private String facultyId;
}
