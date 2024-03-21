package vn.edu.vnua.department.common;

public interface Constants {
    interface DateTimeConstants {
        String DATE_FORMAT = "dd/MM/yyyy";
        String MONTH_YEAR_FORMAT = "MM/yyyy";
        String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
        String TIME_ZONE = "Asia/Ho_Chi_Minh";
    }

    interface RoleIdConstant {
        String PRINCIPAL = "PRINCIPAL";
        String DEAN = "DEAN";
        String MANAGER = "MANAGER";
        String DEPUTY = "DEPUTY";
        String LECTURER = "LECTURER";
    }

    interface RoleConstant {
        String ROLE_NOT_FOUND = "Vai trò không hợp lệ";
    }

    interface FacultyConstant {
        String FACULTY_ALREADY_EXIST = "Khoa đã tồn tại";
        String FACULTY_NOT_FOUND = "Khoa không tồn tại";
        String CANNOT_CREATE_USER = "Không thể thêm người dùng của khoa khác";
    }

    interface DepartmentConstant {
        String DEPARTMENT_ALREADY_EXIST = "Bộ môn đã tồn tại";
        String DEPARTMENT_NOT_FOUND = "Bộ môn không tồn tại";
    }

    interface UserConstant {
        String USER_ALREADY_EXIST = "Người dùng đã tồn tại";
        String USER_NOT_FOUND = "Người dùng không tồn tại";
        String DEAN_ALREADY_EXIST = "Quản trị viên của khoa này đã tồn tại";
        String MANAGER_ALREADY_EXIST = "Quản trị viên của bộ môn này đã tồn tại";
        String MANAGE_NOT_BLANK = "Nơi quản lý không được để trống";
        String MANAGE_NOT_FOUND = "Không tìm thấy nơi quản lý hợp lệ";
        String SET_PRINCIPAL_MANAGE = "VNUA";
        String USER_ARE_NOT_LECTURER = "Người dùng này hiện đã là quản trị viên";
        String EMAIL_ALREADY_USE = "Email đã được người dùng khác sử dụng";
    }

    interface AuthenticationConstant {
        String CANNOT_LOGIN = "Tài khoản hoặc mật khẩu không chính xác";
    }
}
