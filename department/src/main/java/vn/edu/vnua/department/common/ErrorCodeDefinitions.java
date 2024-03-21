package vn.edu.vnua.department.common;

public interface ErrorCodeDefinitions {
    int OTHER = 1;
    int VALIDATION_ERROR = 2;
    int PERMISSION_INVALID = 3;
    int TOKEN_INVALID = 4;
    int TOKEN_REQUIRED = 5;
    int TOKEN_EXPIRED = 6;
    int REFRESH_EXPIRED = 7;
    int BAD_REQUEST = 400;
    int SERVER_ERROR = 500;

    static String getErrMsg(int code) {
        return switch (code) {
            case OTHER -> "Các lỗi khác";
            case PERMISSION_INVALID -> "Bạn không có quyền thực hiện hành động này";
            case VALIDATION_ERROR -> "Tham số không hợp lệ";
            case SERVER_ERROR -> "Lỗi hệ thống";
            case BAD_REQUEST -> "Bad request";
            case TOKEN_REQUIRED -> "Yêu cầu token";
            case TOKEN_INVALID -> "Token không hợp lệ";
            case TOKEN_EXPIRED -> "Token hết hạn";
            case REFRESH_EXPIRED -> "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại";
            default -> "Các lỗi khác";
        };
    }
}
