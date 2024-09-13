package vn.edu.vnua.department.intern.request;

import lombok.Data;

@Data
public class LockInternListRequest {
    private Boolean wantLock;
    private Long schoolYearId;
    private Byte term;
}
