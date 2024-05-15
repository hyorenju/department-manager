package vn.edu.vnua.department.user.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.request.*;

import java.util.List;

public interface UserService {
    Page<User> getUserList(GetUserListRequest request);
    List<User> getUserSelection(GetUserSelectionRequest request);
    User createUser(CreateUserRequest request);
    User updateUser(String id, UpdateUserRequest request);
    User transferRole(String id);
    User deleteUser(String id);
    String exportToExcel(ExportUserListRequest request);

    // For dev
    User createPrincipal(String id);
    User devCreateUser(CreateUserRequest request);
}
