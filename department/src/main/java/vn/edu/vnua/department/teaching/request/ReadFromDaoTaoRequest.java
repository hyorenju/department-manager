package vn.edu.vnua.department.teaching.request;

import lombok.Data;

@Data
public class ReadFromDaoTaoRequest {
    private String teacherId;
    private Long schoolYearId;
    private Byte term;
}
