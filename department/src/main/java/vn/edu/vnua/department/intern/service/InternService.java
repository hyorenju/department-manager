package vn.edu.vnua.department.intern.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.request.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface InternService {
    Page<Intern> getInternList(GetInternListRequest request);
    Intern createIntern(CreateInternRequest request);
    Intern updateIntern(Long id, UpdateInternRequest request);
    Intern deleteIntern(Long id);
    Intern lockIntern(Long id);
    List<Intern> lockInternList(LockInternListRequest request);
    List<Intern> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String exportToExcel(ExportInternListRequest request);
}
