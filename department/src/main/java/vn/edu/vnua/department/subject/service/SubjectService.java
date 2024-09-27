package vn.edu.vnua.department.subject.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.request.*;
import vn.edu.vnua.department.user.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface SubjectService {
    Page<Subject> getSubjectList(GetSubjectListRequest request);
    List<Subject> getSubjectSelection(GetSubjectSelectionRequest request);
    Subject createSubject(CreateSubjectRequest request);
    Subject updateSubject(String id, UpdateSubjectRequest request);
    Subject deleteSubject(String id);
    List<Subject> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String exportToExcel(ExportSubjectListRequest request);
}
