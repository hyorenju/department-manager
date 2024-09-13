package vn.edu.vnua.department.teaching.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.teaching.entity.Teaching;
import vn.edu.vnua.department.teaching.request.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TeachingService {
    Page<Teaching> getTeachingList(GetTeachingListRequest request);
    Teaching createTeaching(CreateTeachingRequest request);
    Teaching updateTeaching(Long id, UpdateTeachingRequest request);
    Teaching deleteTeaching(Long id);
    Teaching lockTeaching(Long id);
    List<Teaching> lockTeachingList(LockTeachingListRequest request);
    List<Teaching> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String exportToExcel(ExportTeachingRequest request);
    List<Teaching> readFromDaoTao(String teacherId) throws IOException;
}
