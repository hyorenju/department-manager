package vn.edu.vnua.department.common;

public interface Constants {
    interface RoleIdConstant {
        String ADMIN = "ADMIN";
        String DEAN = "DEAN";
        String MANAGER = "MANAGER";
        String DEPUTY = "DEPUTY";
        String LECTURER = "LECTURER";
    }

    interface DevAccountConstant {
        String DEV = "DEV";
    }

    interface DateTimeConstants {
        String YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd";
        String DATE_FORMAT = "dd/MM/yyyy";
        String MONTH_YEAR_FORMAT = "MM/yyyy";
        String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
        String TIME_ZONE = "Asia/Ho_Chi_Minh";
    }

    interface RoleConstant {
        String ROLE_NOT_FOUND = "Vai trò không tồn tại";
    }

    interface  SchoolNameAbbreviationConstant{
        String SCHOOL_NAME = "VNUA";
    }

    interface FacultyConstant {
        String FACULTY_EXISTED = "Khoa đã tồn tại";
        String FACULTY_NOT_FOUND = "Khoa không tồn tại";
        String CANNOT_DELETE = "Khoa này đang ràng buộc với những bản ghi khác, không thể xóa";
    }

    interface DepartmentConstant {
        String DEPARTMENT_EXISTED = "Bộ môn đã tồn tại";
        String DEPARTMENT_NOT_FOUND = "Bộ môn không tồn tại";
        String CANNOT_DELETE = "Bộ môn này đang ràng buộc với những bản ghi khác, không thể xóa";
    }

    interface UserConstant {
        String USER_EXISTED = "Người dùng đã tồn tại";
        String USER_NOT_FOUND = "Người dùng không tồn tại";
        String USER_ARE_NOT_LECTURER = "Người dùng này hiện có vai trò cao hơn giảng viên, không thể chuyển";
        String USER_IN_OTHER_DEPARTMENTS = "Người dùng này thuộc bộ môn khác, không thể chuyển vai trò";
        String EMAIL_ALREADY_USE = "Email đã được người dùng khác sử dụng";
        String CANNOT_DELETE_HIGH_ROLE = "Người dùng này đang có vai trò quản trị, không thể xóa";
        String CANNOT_DELETE_USER = "Thông tin người dùng đã bị ràng buộc với các hành động quản lý dữ liệu hệ thống, không thể xóa";
        String CANNOT_MANAGE_YOURSELF = "Không thể quản lý bản thân mình. Nếu muốn sửa thông tin cá nhân, vui lòng nhấn vào avata của bạn ở góc trên bên phải";
        String CANNOT_MANAGE_ANOTHER_DEPARTMENT = "Bạn không thể thêm, sửa giảng viên bộ môn khác";
        String CANNOT_MANAGE_ANOTHER_FACULTY = "Bạn không thể thêm, sửa giảng viên bộ khoa khác";
        String CANNOT_MANAGE_SUPERIOR = "Bạn không thể cập nhật người dùng có chức vụ cao hơn mình";
        String DEFAULT_PASSWORD = "123";
        String ID_REGEX = "^[\\p{L}\\d]*$";
        String NAME_REGEX = "^[\\p{L}\\s]*$";
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
        String PHONE_NUMBER_REGEX = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$";
    }

    interface AuthenticationConstant {
        String CANNOT_LOGIN = "Tài khoản hoặc mật khẩu không chính xác";
        String ACCOUNT_LOCKED = "Tài khoản này đã bị khóa";
    }

    interface MasterDataConstant {
        String INTERN_TYPE_NOT_FOUND = "Không tìm thấy loại đề tài";
        String SCHOOL_YEAR_NOT_FOUND = "Không tìm thấy năm học";
        String EXAM_FORM_NOT_FOUND = "Không tìm thấy hình thức thi";
        String DEGREE_NOT_FOUND = "Không tìm thấy trình độ";
        String TASK_STATUS_NOT_FOUND = "Không tìm thấy tiến độ";
        String DATA_NOT_FOUND = "Không tìm thấy bản ghi trong hệ thống";
        String DATA_IS_EXISTED = "Giá trị đã tồn tại trong hệ thống";
        String CANNOT_DELETE = "Bản ghi này đang ràng buộc với những bản ghi khác, không thể xóa";
    }

    interface MasterDataTypeConstant {
        String USER_DEGREE = "USER_DEGREE";
        String INTERN_TYPE = "INTERN_TYPE";
        String SCHOOL_YEAR = "SCHOOL_YEAR";
        String EXAM_FORM = "EXAM_FORM";
        String TASK_STATUS = "TASK_STATUS";
    }

    interface MasterDataNameConstant {
        String DOING = "Chưa hoàn thành";
        String FINISHED_SOONER = "Hoàn thành trước hạn";
        String FINISHED_ON_TIME = "Hoàn thành đúng hạn";
        String DOING_LATE = "Đã quá hạn";
        String FINISHED_LATE = "Hoàn thành trễ hạn";
    }

    interface InternConstant {
        String INTERN_NOT_FOUND = "Không tìm thấy đề tài thực tập";
        String CANNOT_UPDATE = "Bạn chỉ có thể sửa, xóa nếu bạn là giáo viên hướng dẫn của đề tài này";
        String NAME_IS_EXISTED = "Tên đề tài đã tồn tại";
        String INTERN_NAME_REGEX = "^[\\p{L}\\d\\s.,;…\\-\"()\\[\\]{}&]*$";
        String CANNOT_LOCK = "Bạn không có quyền lock đề tài";
        String LIST_EMPTY = "Không tìm thấy dữ liệu theo điều kiện";
    }

    interface InternshipConstant{
        String INTERNSHIP_EXISTED = "Những thông tin của bản ghi này đang bị trùng với một bản ghi đã tồn tại";
        String INTERNSHIP_NOT_FOUND = "Không tìm thấy bản ghi đề tài thực tập";
        String CANNOT_UPDATE = "Bạn chỉ có thể sửa, xóa nếu bạn là giáo viên hướng dẫn của đề tài này";
        String TOPIC_NAME_REGEX = "^[\\p{L}\\d\\s.,;…\\-\"()\\[\\]{}&]*$";
    }

    interface StudentConstant {
        String STUDENT_NOT_FOUND = "Sinh viên không tồn tại";
        String CANNOT_UPDATE = "Bạn chỉ có thể thao tác nếu bạn là giáo viên hướng dẫn của đề tài này";
    }

    interface FirebaseConstant{
        Long EXPIRATION_TIME = System.currentTimeMillis() + 100L * 365 * 24 * 60 * 60 * 1000;
    }

    interface ClassConstant{
        String CLASS_NOT_FOUND = "Không tìm thấy lớp";
        String CLASS_EXISTED = "Mã lớp đã tồn tại";
        String CANNOT_DELETE = "Lớp này đang ràng buộc với những bản ghi khác, không thể xóa";
        String ID_REGEX = "^[\\p{L}\\d]*$";
    }

    interface SubjectConstant{
        String SUBJECT_NOT_FOUND = "Không tìm thấy môn học";
        String SUBJECT_EXISTED = "Mã môn học đã tồn tại";
        String SUBJECT_NAME_REGEX = "^[\\p{L}\\d\\s.,;…\\-\"()\\[\\]{}&]*$";
        String ID_REGEX = "^[\\p{L}\\d]*$";
    }

    interface UploadFileStatusConstant {
        String INCOMPLETE = "Chưa hoàn thiện";
        String COMPLETED = "Đã hoàn thiện";
    }

    interface TeachingConstant{
        String TEACHING_NOT_FOUND = "Không tìm thấy bản ghi giảng dạy";
        String TEACHING_IS_EXISTED = "Bản ghi giảng dạy này đã tồn tại";
        String CANNOT_UPDATE = "Bạn không thể thêm, sửa, xóa bản ghi giảng dạy của giảng viên khác";
    }

    interface ExamConstant{
        String EXAM_NOT_FOUND = "Không tìm thấy bản ghi lịch thi";
        String PROCTORS_NOT_BE_SAME = "Hai giám thị không được trùng nhau";
        String EXAM_IS_EXISTED = "Lịch thi này đã tồn tại, vui lòng kiểm tra lại";
        String LECTURER_TEACH_NOT_BLANK = "Giáo viên giảng dạy không được để trống";
    }

    interface ThreadsNumConstant{
        int MAX_THREADS = 4;
    }

    interface AppendCharacterConstant {
        String APPEND_CHARACTER = "`";
    }

    interface ExcelConstant{
        String DATA_NOT_FOUND = "Không tìm thấy dữ liệu. Hãy chắc chắn rằng file excel được nhập từ ô A1";
    }

    interface PasswordConstant{
        String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        String WRONG_OLD_PASSWORD = "Mật khẩu cũ không chính xác";
        String WRONG_PASSWORD_REGEX = "Mật khẩu phải có 8 ký tự trở lên, có cả ký tự chữ và số";
        String NOT_BE_SAME_OLD_PASSWORD = "Mật khẩu mới không được trùng mật khẩu cũ";
        String BE_SAME_NEW_PASSWORD = "Xác nhận mật khẩu không trùng khớp";
    }

    interface ProjectConstant {
        String PROJECT_NOT_FOUND = "Không tìm thấy dự án";
        String CANNOT_UPDATE = "Chỉ người tạo dự án mới có thể chỉnh sửa";
        String DATE_BETWEEN_PROBLEM = "Ngày bắt đầu không được sau ngày kết thúc";
        String START_INVALID = "Ngày bắt đầu không hợp lệ";
        String DEADLINE_INVALID = "Ngày kết thúc không hợp lệ";
    }

    interface TaskConstant {
        String CANNOT_UPDATE = "Chỉ người tạo dự án mới có thể chỉnh sửa công việc";
        String TASK_NOT_FOUND = "Không tìm thấy công việc";
        String DATE_BETWEEN_PROBLEM = "Ngày bắt đầu không được sau ngày kết thúc";
        String START_PROBLEM = "Ngày bắt đầu không được trước ngày %s và sau ngày %s";
        String DEADLINE_PROBLEM = "Hạn chót không được trước ngày %s và sau ngày %s";
    }

    interface UserTaskConstant {
        String CANNOT_UPDATE = "Chỉ người tạo dự án mới có thể chỉnh sửa chi tiết công việc";
        String USER_TASK_NOT_FOUND = "Không tìm thấy chi tiết công việc";
    }

    interface FilePathConstant{
        String USER_IMPORT_FILE = "upload/danh-sach-giang-vien.xlsx";
        String USER_ERROR_FILE = "upload/loi-import-nguoi-dung.xlsx";

        String CLASS_IMPORT_FILE = "upload/danh-sach-lop.xlsx";
        String CLASS_ERROR_FILE = "upload/loi-import-ds-lop.xlsx";

        String SUBJECT_IMPORT_FILE = "upload/danh-sach-mon-hoc.xlsx";
        String SUBJECT_ERROR_FILE = "upload/loi-import-ds-mon-hoc.xlsx";

        String INTERN_IMPORT_FILE = "upload/danh-sach-de-tai-thuc-tap.xlsx";
        String INTERN_ERROR_FILE = "upload/loi-import-de-tai-thuc-tap.xlsx";

        String TEACHING_IMPORT_FILE = "upload/danh-sach-giang-day.xlsx";
        String TEACHING_ERROR_FILE = "upload/loi-import-phan-cong-giang-day.xlsx";

        String EXAM_IMPORT_FILE = "upload/danh-sach-ky-thi.xlsx";
        String EXAM_ERROR_FILE = "upload/loi-import-phan-cong-ky-thi.xlsx";
    }
}
