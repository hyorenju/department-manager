package vn.edu.vnua.department.service.excel;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.exam.model.ExamExcelData;
import vn.edu.vnua.department.exam.repository.ExamRepository;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.model.excel.ExcelData;
import vn.edu.vnua.department.service.excel.aclass.WriteClassWorker;
import vn.edu.vnua.department.service.excel.exam.ReadExamWorker;
import vn.edu.vnua.department.service.excel.exam.StoreExamWorker;
import vn.edu.vnua.department.service.excel.exam.WriteErrorExamWorker;
import vn.edu.vnua.department.service.excel.exam.WriteExamWorker;
import vn.edu.vnua.department.service.excel.intern.WriteInternWorker;
import vn.edu.vnua.department.service.excel.subject.WriteSubjectWorker;
import vn.edu.vnua.department.service.excel.teaching.ReadTeachingWorker;
import vn.edu.vnua.department.service.excel.teaching.StoreTeachingWorker;
import vn.edu.vnua.department.service.excel.teaching.WriteErrorTeachingWorker;
import vn.edu.vnua.department.service.excel.teaching.WriteTeachingWorker;
import vn.edu.vnua.department.service.excel.user.WriteUserWorker;
import vn.edu.vnua.department.service.firebase.FirebaseService;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.repository.SubjectRepository;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.teaching.model.TeachingExcelData;
import vn.edu.vnua.department.teaching.repository.TeachingRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    private final FirebaseService firebaseService;
    private final ExecutorService executor;
    private final MasterDataRepository masterDataRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TeachingRepository teachingRepository;
    private final ExamRepository examRepository;


    @Override
    public String writeUserToExcel(List<User> users) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("danh-sach-giang-vien");

            // Tạo hàng tiêu đề
            createUserListHeader(sheet);

            // Sử dụng đa luồng
            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);

                Callable<Void> callable = new WriteUserWorker(row, user);
                Future<Void> future = executor.submit(callable);
                try {
                    future.get(); // Đợi và nhận giá trị null từ Callable
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = new FileOutputStream("danh-sach-giang-vien.xlsx");
            workbook.write(fos);

            workbook.close();
            fos.close();

            // Gọi hàm upload firebase
            return firebaseService.uploadFileByName("danh-sach-giang-vien.xlsx");

        } catch (IOException e) {
            throw new RuntimeException("Đã có lỗi, không thể ghi file.");
        }
    }

    @Override
    public String writeClassToExcel(List<AClass> classes) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("danh-sach-lop");

            // Tạo hàng tiêu đề
            createClassListHeader(sheet);

            // Sử dụng đa luồng
            int rowNum = 1;
            for (AClass aClass : classes) {
                Row row = sheet.createRow(rowNum++);

                Callable<Void> callable = new WriteClassWorker(row, aClass);
                Future<Void> future = executor.submit(callable);
                try {
                    future.get(); // Đợi và nhận giá trị null từ Callable
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = new FileOutputStream("danh-sach-lop.xlsx");
            workbook.write(fos);

            workbook.close();
            fos.close();

            // Gọi hàm upload firebase
            return firebaseService.uploadFileByName("danh-sach-lop.xlsx");

        } catch (IOException e) {
            throw new RuntimeException("Đã có lỗi, không thể ghi file.");
        }
    }

    @Override
    public String writeSubjectToExcel(List<Subject> subjects) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("danh-sach-mon-hoc");

            // Tạo hàng tiêu đề
            createSubjectListHeader(sheet);

            // Sử dụng đa luồng
            int rowNum = 1;
            for (Subject subject : subjects) {
                Row row = sheet.createRow(rowNum++);

                Callable<Void> callable = new WriteSubjectWorker(row, subject);
                Future<Void> future = executor.submit(callable);
                try {
                    future.get(); // Đợi và nhận giá trị null từ Callable
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = new FileOutputStream("danh-sach-mon-hoc.xlsx");
            workbook.write(fos);

            workbook.close();
            fos.close();

            // Gọi hàm upload firebase
            return firebaseService.uploadFileByName("danh-sach-mon-hoc.xlsx");

        } catch (IOException e) {
            throw new RuntimeException("Đã có lỗi, không thể ghi file.");
        }
    }

    @Override
    public String writeInternToExcel(List<Intern> interns) {

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("danh-sach-de-tai-thuc-tap");

            // Tạo hàng tiêu đề
            createInternListHeader(sheet);

            // Sử dụng đa luồng
            int rowNum = 1;
            for (Intern intern : interns) {
                Row row = sheet.createRow(rowNum++);

                Callable<Void> callable = new WriteInternWorker(row, intern);
                Future<Void> future = executor.submit(callable);
                try {
                    future.get(); // Đợi và nhận giá trị null từ Callable
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = new FileOutputStream("danh-sach-de-tai-thuc-tap.xlsx");
            workbook.write(fos);

            workbook.close();
            fos.close();

            // Gọi hàm upload firebase
            return firebaseService.uploadFileByName("danh-sach-de-tai-thuc-tap.xlsx");

        } catch (IOException e) {
            throw new RuntimeException("Đã có lỗi, không thể ghi file.");
        }
    }

    @Override
    public List<Teaching> readTeachingFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<String> teachingStrList = readTeachingData(file);
        List<TeachingExcelData> teachingExcelDataList = storeTeachingData(teachingStrList);
        if (!isContinue(teachingExcelDataList)) {
            throw new RuntimeException(exportErrorTeachingList(teachingExcelDataList));
        }
        List<Teaching> teachings = new ArrayList<>();
        teachingExcelDataList.forEach(teachingExcelData -> teachings.add(teachingExcelData.getTeaching()));
        return teachings;
    }

    @Override
    public String writeTeachingToExcel(List<Teaching> teachings) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("danh-sach-giang-day");

            // Tạo hàng tiêu đề
            createTeachingListHeader(sheet);

            // Sử dụng đa luồng
            int rowNum = 1;
            for (Teaching teaching : teachings) {
                Row row = sheet.createRow(rowNum++);

                Callable<Void> callable = new WriteTeachingWorker(row, teaching);
                Future<Void> future = executor.submit(callable);
                try {
                    future.get(); // Đợi và nhận giá trị null từ Callable
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = new FileOutputStream("danh-sach-giang-day.xlsx");
            workbook.write(fos);

            workbook.close();
            fos.close();

            // Gọi hàm upload firebase
            return firebaseService.uploadFileByName("danh-sach-giang-day.xlsx");

        } catch (IOException e) {
            throw new RuntimeException("Đã có lỗi, không thể ghi file.");
        }
    }

    @Override
    public List<Exam> readExamFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<String> examStrList = readExamData(file);
        List<ExamExcelData> examExcelDataList = storeExamData(examStrList);
        if (!isContinue(examExcelDataList)) {
            throw new RuntimeException(exportErrorExamList(examExcelDataList));
        }
        List<Exam> exams = new ArrayList<>();
        examExcelDataList.forEach(examExcelData -> exams.add(examExcelData.getExam()));
        return exams;
    }

    @Override
    public String writeExamToExcel(List<Exam> exams) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("danh-sach-ky-thi");

            // Tạo hàng tiêu đề
            createExamListHeader(sheet);

            // Sử dụng đa luồng
            int rowNum = 1;
            for (Exam exam : exams) {
                Row row = sheet.createRow(rowNum++);

                Callable<Void> callable = new WriteExamWorker(row, exam);
                Future<Void> future = executor.submit(callable);
                try {
                    future.get(); // Đợi và nhận giá trị null từ Callable
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = new FileOutputStream("danh-sach-ky-thi.xlsx");
            workbook.write(fos);

            workbook.close();
            fos.close();

            // Gọi hàm upload firebase
            return firebaseService.uploadFileByName("danh-sach-ky-thi.xlsx");

        } catch (IOException e) {
            throw new RuntimeException("Đã có lỗi, không thể ghi file.");
        }
    }

    // Kiểm tra dữ liệu lỗi
    private boolean isContinue(List<? extends ExcelData> excelDataList) {
        for (ExcelData excelData :
                excelDataList) {
            if (!excelData.isValid()) {
                return false;
            }
        }
        return true;
    }

    private List<String> readTeachingData(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<String> stringList = new CopyOnWriteArrayList<>();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        int totalRows = sheet.getPhysicalNumberOfRows();

        // Sử dụng đa luồng đọc dữ liệu từ excel, trả về List<String>
        for (int i = 0; i < totalRows; i++) {
            Row row = sheet.getRow(i);
            if (i != 0) {
                Callable<String> callable = new ReadTeachingWorker(row);
                Future<String> future = executor.submit(callable);
                stringList.add(future.get());
            } else {
                if (row == null || row.getCell(0) == null) {
                    throw new RuntimeException(Constants.ExcelConstant.DATA_NOT_FOUND);
                }
            }
        }

        if (totalRows < 2) {
            throw new RuntimeException(Constants.ExcelConstant.NO_DATA);
        }

        workbook.close();
        return stringList;
    }

    private List<TeachingExcelData> storeTeachingData(List<String> teachingStrList) throws ExecutionException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());

        List<TeachingExcelData> teachingExcelDataList = new CopyOnWriteArrayList<>();

        for (int i = 0; i < teachingStrList.size(); i++) {
            String teachingStr = teachingStrList.get(i);
            Callable<TeachingExcelData> callable = new StoreTeachingWorker(createdBy, teachingRepository, masterDataRepository,userRepository,subjectRepository,teachingStr, i);
            Future<TeachingExcelData> future = executor.submit(callable);
            teachingExcelDataList.add(future.get());
        }
        return teachingExcelDataList;
    }

    // Xuất file lỗi
    private String exportErrorTeachingList(List<TeachingExcelData> teachingExcelDataList) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("loi-import-phan-cong-giang-day");

            // Tạo hàng tiêu đề
            createErrorTeachingHeader(sheet);

            // Tạo style cho error cell
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);

            // Sử dụng đa luồng in dữ liệu lỗi ra excel
            for (TeachingExcelData teachingExcelData :
                    teachingExcelDataList) {
                Row row = sheet.createRow(teachingExcelData.getRowIndex() + 1);

                Callable<Void> callable = new WriteErrorTeachingWorker(row, cellStyle, teachingExcelData);
                Future<Void> future = executor.submit(callable);
                try {
                    future.get(); // Đợi và nhận giá trị null từ Callable
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = new FileOutputStream("loi-import-phan-cong-giang-day.xlsx");
            workbook.write(fos);

            workbook.close();
            fos.close();

            //gọi hàm upload firebase
            return firebaseService.uploadFileByName("loi-import-phan-cong-giang-day.xlsx");

        } catch (IOException e) {
            throw new RuntimeException("Đã có lỗi, không thể ghi file.");
        }
    }

    private List<String> readExamData(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        List<String> stringList = new CopyOnWriteArrayList<>();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        int totalRows = sheet.getPhysicalNumberOfRows();

        // Sử dụng đa luồng đọc dữ liệu từ excel, trả về List<String>
        for (int i = 0; i < totalRows; i++) {
            Row row = sheet.getRow(i);
            if (i != 0) {
                Callable<String> callable = new ReadExamWorker(row);
                Future<String> future = executor.submit(callable);
                stringList.add(future.get());
            } else {
                if (row == null || row.getCell(0) == null) {
                    throw new RuntimeException(Constants.ExcelConstant.DATA_NOT_FOUND);
                }
            }
        }

        if (totalRows < 2) {
            throw new RuntimeException(Constants.ExcelConstant.NO_DATA);
        }

        workbook.close();
        return stringList;
    }

    private List<ExamExcelData> storeExamData(List<String> examStrList) throws ExecutionException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.getUserById(authentication.getPrincipal().toString());

        List<ExamExcelData> examExcelDataList = new CopyOnWriteArrayList<>();

        for (int i = 0; i < examStrList.size(); i++) {
            String examStr = examStrList.get(i);
            Callable<ExamExcelData> callable = new StoreExamWorker(createdBy, examRepository, masterDataRepository, subjectRepository, userRepository, examStr, i);
            Future<ExamExcelData> future = executor.submit(callable);
            examExcelDataList.add(future.get());
        }
        return examExcelDataList;
    }

    // Xuất file lỗi
    private String exportErrorExamList(List<ExamExcelData> examExcelDataList) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("loi-import-phan-cong-ky-thi");

            // Tạo hàng tiêu đề
            createErrorExamHeader(sheet);

            // Tạo style cho error cell
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);

            // Sử dụng đa luồng in dữ liệu lỗi ra excel
            for (ExamExcelData examExcelData :
                    examExcelDataList) {
                Row row = sheet.createRow(examExcelData.getRowIndex() + 1);

                Callable<Void> callable = new WriteErrorExamWorker(row, cellStyle, examExcelData);
                Future<Void> future = executor.submit(callable);
                try {
                    future.get(); // Đợi và nhận giá trị null từ Callable
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = new FileOutputStream("loi-import-phan-cong-ky-thi.xlsx");
            workbook.write(fos);

            workbook.close();
            fos.close();

            //gọi hàm upload firebase
            return firebaseService.uploadFileByName("loi-import-phan-cong-ky-thi.xlsx");

        } catch (IOException e) {
            throw new RuntimeException("Đã có lỗi, không thể ghi file.");
        }
    }

    private void createUserListHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã người dùng");
        headerRow.createCell(1).setCellValue("Họ đệm");
        headerRow.createCell(2).setCellValue("Tên");
        headerRow.createCell(3).setCellValue("Khoa");
        headerRow.createCell(4).setCellValue("Bộ môn");
        headerRow.createCell(5).setCellValue("Trình độ");
        headerRow.createCell(6).setCellValue("Email");
        headerRow.createCell(7).setCellValue("Số điện thoại");
        headerRow.createCell(8).setCellValue("Vai trò");
        headerRow.createCell(9).setCellValue("Ghi chú");
    }

    private void createClassListHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã lớp");
        headerRow.createCell(1).setCellValue("Tên lớp");
        headerRow.createCell(2).setCellValue("Khoa");
        headerRow.createCell(3).setCellValue("GVCN");
        headerRow.createCell(4).setCellValue("Lớp trưởng");
        headerRow.createCell(5).setCellValue("Sđt lớp trưởng");
        headerRow.createCell(6).setCellValue("Email lớp trưởng");
        headerRow.createCell(7).setCellValue("Ghi chú");
    }

    private void createSubjectListHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã ôn học");
        headerRow.createCell(1).setCellValue("Tên môn học");
        headerRow.createCell(2).setCellValue("Khoa");
        headerRow.createCell(3).setCellValue("Bộ môn");
        headerRow.createCell(4).setCellValue("Số tín chỉ");
        headerRow.createCell(5).setCellValue("Ghi chú");
    }

    private void createInternListHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Năm học");
        headerRow.createCell(1).setCellValue("Học kỳ");
        headerRow.createCell(2).setCellValue("Khoa");
        headerRow.createCell(3).setCellValue("Bộ môn");
        headerRow.createCell(4).setCellValue("GV hướng dẫn");
        headerRow.createCell(5).setCellValue("Đề tài thực tập");
        headerRow.createCell(6).setCellValue("Loại đề tài");
        headerRow.createCell(7).setCellValue("Trạng thái");
        headerRow.createCell(8).setCellValue("Ghi chú");
    }

    private void createErrorTeachingHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Năm học");
        headerRow.createCell(1).setCellValue("Học kỳ");
        headerRow.createCell(2).setCellValue("Mã GV");
        headerRow.createCell(3).setCellValue("Mã môn học");
        headerRow.createCell(4).setCellValue("NMH");
        headerRow.createCell(5).setCellValue("Mã lớp");
        headerRow.createCell(6).setCellValue("Ghi chú");
    }

    private void createTeachingListHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Năm học");
        headerRow.createCell(1).setCellValue("Học kỳ");
        headerRow.createCell(2).setCellValue("Khoa");
        headerRow.createCell(3).setCellValue("Bộ môn");
        headerRow.createCell(4).setCellValue("Mã môn học");
        headerRow.createCell(5).setCellValue("Tên môn học");
        headerRow.createCell(6).setCellValue("Giảng viên");
        headerRow.createCell(7).setCellValue("Mã lớp");
        headerRow.createCell(8).setCellValue("NMH");
        headerRow.createCell(9).setCellValue("Trạng thái");
        headerRow.createCell(10).setCellValue("Ghi chú");
    }
    private void createErrorExamHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Năm học");
        headerRow.createCell(1).setCellValue("Học kỳ");
        headerRow.createCell(2).setCellValue("Mã môn thi");
        headerRow.createCell(3).setCellValue("Ngày thi");
        headerRow.createCell(4).setCellValue("TBĐ");
        headerRow.createCell(5).setCellValue("Số tiết");
        headerRow.createCell(6).setCellValue("Phòng thi");
        headerRow.createCell(7).setCellValue("Mã lớp");
        headerRow.createCell(8).setCellValue("Nhóm");
        headerRow.createCell(9).setCellValue("Tổ");
        headerRow.createCell(10).setCellValue("Slg");
        headerRow.createCell(11).setCellValue("Hình thức");
        headerRow.createCell(12).setCellValue("Mã đề thi");
        headerRow.createCell(13).setCellValue("GV giảng dạy");
        headerRow.createCell(14).setCellValue("Bốc đề");
        headerRow.createCell(15).setCellValue("In sao đề");
        headerRow.createCell(16).setCellValue("Coi thi 1");
        headerRow.createCell(17).setCellValue("Coi thi 2");
        headerRow.createCell(18).setCellValue("Chấm thi 1");
        headerRow.createCell(19).setCellValue("Chấm thi 2");
        headerRow.createCell(20).setCellValue("Nhận đề");
        headerRow.createCell(21).setCellValue("Nhận bài");
        headerRow.createCell(22).setCellValue("Giao bài");
        headerRow.createCell(23).setCellValue("Giao điểm");
        headerRow.createCell(24).setCellValue("Ghi chú");
    }

    private void createExamListHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Năm học");
        headerRow.createCell(1).setCellValue("Học kỳ");
        headerRow.createCell(2).setCellValue("Khoa");
        headerRow.createCell(3).setCellValue("Bộ môn");
        headerRow.createCell(4).setCellValue("Mã môn thi");
        headerRow.createCell(5).setCellValue("Tên môn thi");
        headerRow.createCell(6).setCellValue("Hình thức");
        headerRow.createCell(7).setCellValue("Mã đề thi");
        headerRow.createCell(8).setCellValue("Ngày thi");
        headerRow.createCell(9).setCellValue("Phòng thi");
        headerRow.createCell(10).setCellValue("Tiết bắt đầu");
        headerRow.createCell(11).setCellValue("Số tiết");
        headerRow.createCell(12).setCellValue("Lớp thi");
        headerRow.createCell(13).setCellValue("Nhóm");
        headerRow.createCell(14).setCellValue("Tổ");
        headerRow.createCell(15).setCellValue("Sĩ số");
        headerRow.createCell(16).setCellValue("Giáo viên giảng dạy");
        headerRow.createCell(17).setCellValue("Giám thị 1");
        headerRow.createCell(18).setCellValue("Giám thị 2");
        headerRow.createCell(19).setCellValue("GV chấm thi 1");
        headerRow.createCell(20).setCellValue("GV chấm thi 2");
        headerRow.createCell(21).setCellValue("CB bốc đề");
        headerRow.createCell(22).setCellValue("CB in sao đề");
        headerRow.createCell(23).setCellValue("CB nhận đề");
        headerRow.createCell(24).setCellValue("CB nhận bài");
        headerRow.createCell(25).setCellValue("CB giao bài");
        headerRow.createCell(26).setCellValue("CB giao điểm");
        headerRow.createCell(27).setCellValue("Ghi chú");
    }
}
