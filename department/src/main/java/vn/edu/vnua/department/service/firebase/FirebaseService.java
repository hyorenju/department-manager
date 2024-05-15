package vn.edu.vnua.department.service.firebase;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FirebaseService {
    String uploadMultipartFile(MultipartFile file) throws IOException;
    String uploadFileByName(String fileName) throws IOException;
}
