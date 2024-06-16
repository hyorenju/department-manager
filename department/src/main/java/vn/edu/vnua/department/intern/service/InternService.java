package vn.edu.vnua.department.intern.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.exam.entity.Exam;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.request.CreateInternRequest;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.intern.request.GetInternListRequest;
import vn.edu.vnua.department.intern.request.UpdateInternRequest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface InternService {
    Page<Intern> getInternList(GetInternListRequest request);
    Intern createIntern(CreateInternRequest request);
    Intern updateIntern(Long id, UpdateInternRequest request);
    Intern deleteIntern(Long id);
    List<Intern> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String exportToExcel(ExportInternListRequest request);
}
