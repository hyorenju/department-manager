package vn.edu.vnua.department.user.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.request.*;

import java.io.IOException;
import java.util.List;

public interface UserService {
    Page<User> getUserList(GetUserListRequest request);
    List<User> getUserSelection(GetUserSelectionRequest request);
    User createUser(CreateUserRequest request);
    User updateUser(String id, UpdateUserRequest request);
    User updateProfile(UpdateProfileRequest request);
    User updateAvatar(MultipartFile pic) throws IOException;
    User transferRole(String id);
    User deleteUser(String id);
    String exportToExcel(ExportUserListRequest request);
    User changePassword(ChangePasswordRequest request);

    // For dev
    User createPrincipal(String id);
    User devCreateUser(CreateUserRequest request);
}
