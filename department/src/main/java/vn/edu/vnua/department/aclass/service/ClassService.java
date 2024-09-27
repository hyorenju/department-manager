package vn.edu.vnua.department.aclass.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.aclass.entity.AClass;
import vn.edu.vnua.department.aclass.request.CreateClassRequest;
import vn.edu.vnua.department.aclass.request.ExportClassListRequest;
import vn.edu.vnua.department.aclass.request.GetClassListRequest;
import vn.edu.vnua.department.aclass.request.UpdateClassRequest;
import vn.edu.vnua.department.user.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ClassService {
    Page<AClass> getClassList(GetClassListRequest request);
    AClass createClass(CreateClassRequest request);
    AClass updateClass(String id, UpdateClassRequest request);
    AClass deleteClass(String id);
    List<AClass> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String exportToExcel(ExportClassListRequest request);
}
