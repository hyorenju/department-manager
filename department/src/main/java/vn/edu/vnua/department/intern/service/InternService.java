package vn.edu.vnua.department.intern.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.request.CreateInternRequest;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.intern.request.GetInternListRequest;
import vn.edu.vnua.department.intern.request.UpdateInternRequest;

public interface InternService {
    Page<Intern> getInternList(GetInternListRequest request);
    Intern createIntern(CreateInternRequest request);
    Intern updateIntern(Long id, UpdateInternRequest request);
    Intern deleteIntern(Long id);
    String exportToExcel(ExportInternListRequest request);
}
