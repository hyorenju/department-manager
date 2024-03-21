package vn.edu.vnua.department.user.service;

import org.springframework.data.domain.Page;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.request.*;

public interface UserService {
    Page<User> getUserList(GetUserListRequest request);
    User createUser(CreateUserRequest request);
    User createDean(String id, CreateDeanRequest request);
    User createManager(String id, CreateManagerRequest request);
    User updateUser(String id, UpdateUserRequest request);
    User transferRole(String id);
    User deleteUser(String id);

    User createPrincipal(String id);
}
