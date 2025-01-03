package vn.edu.vnua.department.service.firebase;

import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.config.FirebaseConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FirebaseServiceImpl implements FirebaseService{
    private final FirebaseConfig firebaseConfig;

    @Value("${firebase.storage.bucket}")
    private String bucketName;

    @Override
    public String uploadMultipartFile(MultipartFile file) throws IOException {
        try {
            // Tạo một tên file
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + getFileExtension(originalFileName);

            // Nhận storage service
            StorageOptions storageOptions = StorageOptions.newBuilder().setCredentials(firebaseConfig.getCredentials()).build();
            Storage storage = storageOptions.getService();

            // Tải lên tệp tin vào Firebase Storage
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            InputStream inputStream = file.getInputStream();

            // Upload file và lấy URL
            Blob blob = storage.create(blobInfo, inputStream);
            return blob.signUrl(Constants.FirebaseConstant.EXPIRATION_TIME, TimeUnit.MILLISECONDS).toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String uploadFileByName(String fileName) throws IOException {
        // Nhận storage service
        StorageOptions storageOptions = StorageOptions.newBuilder().setCredentials(firebaseConfig.getCredentials()).build();
        Storage storage = storageOptions.getService();

        // Tải lên tệp tin vào Firebase Storage
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
        FileInputStream fileInputStream = new FileInputStream(fileName);

        return storage.create(blobInfo, fileInputStream)
                .signUrl(Constants.FirebaseConstant.EXPIRATION_TIME, TimeUnit.MILLISECONDS)
                .toString();
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.lastIndexOf(".") != -1) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }
}
