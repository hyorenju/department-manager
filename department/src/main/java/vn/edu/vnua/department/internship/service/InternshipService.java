package vn.edu.vnua.department.internship.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.intern.request.*;
import vn.edu.vnua.department.internship.entity.Internship;
import vn.edu.vnua.department.internship.request.CreateInternshipRequest;
import vn.edu.vnua.department.internship.request.GetInternshipListRequest;
import vn.edu.vnua.department.internship.request.LockInternshipListRequest;
import vn.edu.vnua.department.internship.request.UpdateInternshipRequest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface InternshipService {
    Page<Internship> getInternList(GetInternshipListRequest request);
    Internship createIntern(CreateInternshipRequest request);
    Internship updateIntern(Long id, UpdateInternshipRequest request);
    Internship deleteIntern(Long id);
    Internship lockInternship(Long id);
    List<Internship> lockInternshipList(LockInternshipListRequest request);
    List<Internship> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String exportToExcel(ExportInternListRequest request);
}
