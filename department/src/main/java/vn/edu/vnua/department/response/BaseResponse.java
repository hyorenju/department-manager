package vn.edu.vnua.department.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse {
    private boolean success = false;
    private Error error;

    public void setFailed(int code, String message) {
        error = new Error(code, message, null);
        success = false;
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Error {
        private int code;
        private String message;
        private List<ErrorDetail> errorDetailList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ErrorDetail {
        private String id;
        private String message;
    }
}
