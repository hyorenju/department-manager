package vn.edu.vnua.department.subject.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.subject.entity.Subject;
import vn.edu.vnua.department.subject.request.*;
import vn.edu.vnua.department.user.entity.User;

import java.util.List;

public interface SubjectService {
    Page<Subject> getSubjectList(GetSubjectListRequest request);
    List<Subject> getSubjectSelection(GetSubjectSelectionRequest request);
    Subject createSubject(CreateSubjectRequest request);
    Subject updateSubject(String id, UpdateSubjectRequest request);
    Subject deleteSubject(String id);
    String exportToExcel(ExportSubjectListRequest request);
}
