package vn.edu.vnua.department.aclass.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.request.CreateClassRequest;
import vn.edu.vnua.department.aclass.request.ExportClassListRequest;
import vn.edu.vnua.department.aclass.request.GetClassListRequest;
import vn.edu.vnua.department.aclass.request.UpdateClassRequest;

public interface ClassService {
    Page<AClass> getClassList(GetClassListRequest request);
    AClass createClass(CreateClassRequest request);
    AClass updateClass(String id, UpdateClassRequest request);
    AClass deleteClass(String id);
    String exportToExcel(ExportClassListRequest request);
}
