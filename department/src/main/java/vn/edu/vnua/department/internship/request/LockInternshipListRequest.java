package vn.edu.vnua.department.internship.request;

import lombok.Data;

@Data
public class LockInternshipListRequest {
    private Boolean wantLock;
    private Long schoolYearId;
    private Byte term;
}
