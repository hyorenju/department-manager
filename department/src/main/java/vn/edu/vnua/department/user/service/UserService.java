package vn.edu.vnua.department.user.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.intern.entity.Intern;
import vn.edu.vnua.department.intern.request.ExportInternListRequest;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.request.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {
    Page<User> getUserList(GetUserListRequest request);
    List<User> getUserSelection(GetUserSelectionRequest request);
    List<User> getUserOption();
    User createUser(CreateUserRequest request);
    User updateUser(String id, UpdateUserRequest request);
    User updateProfile(UpdateProfileRequest request);
    User updateAvatar(MultipartFile pic) throws IOException;
    User transferRole(String id);
    User deleteUser(String id);
    List<User> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException;
    String exportToExcel(ExportUserListRequest request);
    User changePassword(ChangePasswordRequest request);
    User lockAccount(String id);

    // For dev
    User createAdmin(String id);
    User devCreateUser(CreateUserRequest request);
}
