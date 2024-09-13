package vn.edu.vnua.department.teaching.request;

import lombok.Data;

@Data
public class LockTeachingListRequest {
    private Boolean wantLock;
    private Long schoolYearId;
    private Byte term;
}
