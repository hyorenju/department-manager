package vn.edu.vnua.department.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.service.firebase.FirebaseService;

import java.io.IOException;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UploadFileController extends BaseController{
    private final FirebaseService firebaseService;

    @PostMapping("upload-file")
    public ResponseEntity<?> UploadFile(MultipartFile file) throws IOException {
        String response = firebaseService.uploadMultipartFile(file);
        return buildItemResponse(response);
    }
}
