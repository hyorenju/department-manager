package vn.edu.vnua.department.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.repository.DepartmentRepository;
import vn.edu.vnua.department.masterdata.entity.MasterData;
import vn.edu.vnua.department.masterdata.repository.MasterDataRepository;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.role.repository.RoleRepository;
import vn.edu.vnua.department.service.excel.ExcelService;
import vn.edu.vnua.department.service.firebase.FirebaseService;
import vn.edu.vnua.department.task.entity.Task;
import vn.edu.vnua.department.task.repository.TaskRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.CustomUserRepository;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.user.request.*;
import vn.edu.vnua.department.userjointask.entity.UserTask;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder encoder;
    private final MasterDataRepository masterDataRepository;
    private final ExcelService excelService;
    private final FirebaseService firebaseService;
    private final TaskRepository taskRepository;

    @Override
    public Page<User> getUserList(GetUserListRequest request) {
        Specification<User> specification = CustomUserRepository.filterUserList(request);
        return userRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public List<User> getUserSelection(GetUserSelectionRequest request) {
        List<User> users;
        if (StringUtils.hasText(request.getDepartmentId())) {
            users = userRepository.findAllByDepartmentId(request.getDepartmentId());
            return users;
        } else if (StringUtils.hasText(request.getFacultyId())) {
            users = userRepository.findAllByDepartmentFacultyId(request.getFacultyId());
            return users;
        } else {
            users = userRepository.findAll();
            return users;
        }
    }

    @Override
    public List<User> getUserOption(GetUserListPickRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = userRepository.getUserById(authentication.getPrincipal().toString());

        List<User> users = userRepository.findAllByDepartment(me.getDepartment());

        if (request.getIsUpdateParticipant()) {
            Task task = taskRepository.findById(request.getTaskId()).orElseThrow(() -> new RuntimeException(Constants.TaskConstant.TASK_NOT_FOUND));
            List<UserTask> userJoined = task.getUserJoined();
            userJoined.forEach(userTask -> {
                users.remove(userTask.getUser());
            });
        }

        return users;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        try {
            if (userRepository.existsById(request.getId())) {
                throw new RuntimeException(Constants.UserConstant.USER_EXISTED);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            User user = userRepository.getUserById(authentication.getPrincipal().toString());
            User user = userRepository.getUserById(authentication.getPrincipal().toString());

            String roleId = request.getRole().getId();
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
            Department department = departmentRepository.findById(request.getDepartment().getId()).orElseThrow(() -> new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_NOT_FOUND));

            if (user.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) ||
                    user.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY)) {
                if (user.getDepartment() != department) {
                    throw new RuntimeException(Constants.UserConstant.CANNOT_MANAGE_ANOTHER_DEPARTMENT);
                }
            } else if (user.getRole().getId().equals(Constants.RoleIdConstant.DEAN)) {
                if (user.getDepartment().getFaculty() != department.getFaculty()) {
                    throw new RuntimeException(Constants.UserConstant.CANNOT_MANAGE_ANOTHER_FACULTY);
                }
            }

            String manage;
            switch (roleId) {
                case Constants.RoleIdConstant.DEPUTY, Constants.RoleIdConstant.MANAGER -> manage = user.getDepartment().getId();
                case Constants.RoleIdConstant.DEAN -> manage = user.getDepartment().getFaculty().getId();
                case Constants.RoleIdConstant.ADMIN -> manage = Constants.SchoolNameAbbreviationConstant.SCHOOL_NAME;
                default -> manage = null;
            }

            MasterData degree = masterDataRepository.findById(request.getDegree().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.DEGREE_NOT_FOUND));

            return userRepository.saveAndFlush(
                    User.builder()
                            .id(request.getId())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .degree(degree)
                            .email(request.getEmail())
                            .phoneNumber(request.getPhoneNumber())
                            .department(department)
                            .password(StringUtils.hasText(request.getPassword()) ? encoder.encode(request.getPassword()) : encoder.encode(Constants.UserConstant.DEFAULT_PASSWORD))
                            .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                            .role(role)
                            .manage(manage)
                            .note(request.getNote())
                            .isLock(false)
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(Constants.UserConstant.EMAIL_ALREADY_USE);
        }
    }

    @Override
    public User updateUser(String id, UpdateUserRequest request) {
        try {
            //Lấy thông tin modifier
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User manager = userRepository.getUserById(authentication.getPrincipal().toString());

            //Lấy roleId của modifier
            String managerRoleId = manager.getRole().getId();

            //Không thể sửa chính mình
            if (manager.getId().equals(id)) {
                throw new RuntimeException(Constants.UserConstant.CANNOT_MANAGE_YOURSELF);
            }

            //Lấy thông tin người bị sửa
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

            //Lấy roleId của người bị sửa
            String userRoleId = user.getRole().getId();

            //Bắt lỗi nếu cấp dưới muốn update cấp trên
            if (managerRoleId.equals(Constants.RoleIdConstant.DEAN) &&
                    userRoleId.equals(Constants.RoleIdConstant.ADMIN)) {
                throw new RuntimeException(Constants.UserConstant.CANNOT_MANAGE_SUPERIOR);
            } else if (managerRoleId.equals(Constants.RoleIdConstant.MANAGER) &&
                    (userRoleId.equals(Constants.RoleIdConstant.DEAN) ||
                            userRoleId.equals(Constants.RoleIdConstant.ADMIN))) {
                throw new RuntimeException(Constants.UserConstant.CANNOT_MANAGE_SUPERIOR);
            } else if (managerRoleId.equals(Constants.RoleIdConstant.DEPUTY) &&
                    (userRoleId.equals(Constants.RoleIdConstant.MANAGER) ||
                            userRoleId.equals(Constants.RoleIdConstant.DEAN) ||
                            userRoleId.equals(Constants.RoleIdConstant.ADMIN))) {
                throw new RuntimeException(Constants.UserConstant.CANNOT_MANAGE_SUPERIOR);
            }

            //Lấy role của người bị sửa
            String roleId = request.getRole().getId();
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
            MasterData degree = masterDataRepository.findById(request.getDegree().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.DEGREE_NOT_FOUND));

            //Kiểm tra nếu người bị sửa ở bộ môn khác hoặc khoa khác
            if (user.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) ||
                    user.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY)) {
                if (manager.getDepartment() != user.getDepartment() &&
                        !manager.getRole().getId().equals(Constants.RoleIdConstant.ADMIN) &&
                        !manager.getRole().getId().equals(Constants.RoleIdConstant.DEAN)) {
                    throw new RuntimeException(Constants.UserConstant.CANNOT_MANAGE_ANOTHER_DEPARTMENT);
                }
            } else if (user.getRole().getId().equals(Constants.RoleIdConstant.DEAN)) {
                if (manager.getDepartment().getFaculty() != user.getDepartment().getFaculty() &&
                        !manager.getRole().getId().equals(Constants.RoleIdConstant.ADMIN)) {
                    throw new RuntimeException(Constants.UserConstant.CANNOT_MANAGE_ANOTHER_FACULTY);
                }
            }

            if(request.getDepartment()!=null){
                if(!request.getDepartment().getId().equals(user.getDepartment().getId())) {
                    if (!user.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
                        throw new RuntimeException(Constants.UserConstant.CANNOT_CHANGE_DEPARTMENT);
                    } else {
                        Department department = departmentRepository.findById(request.getDepartment().getId()).orElseThrow(() -> new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_NOT_FOUND));
                        user.setDepartment(department);
                    }
                }
            }

            //Sửa
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setDegree(degree);
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(StringUtils.hasText(request.getPassword()) ? encoder.encode(request.getPassword()) : user.getPassword());
            user.setRole(role);
            user.setNote(request.getNote());


            //Set giá trị cho nơi quản lý (nếu có)
            switch (roleId) {
                case Constants.RoleIdConstant.DEPUTY, Constants.RoleIdConstant.MANAGER -> user.setManage(user.getDepartment().getId());
                case Constants.RoleIdConstant.DEAN -> user.setManage(user.getDepartment().getFaculty().getId());
                case Constants.RoleIdConstant.ADMIN -> user.setManage(Constants.SchoolNameAbbreviationConstant.SCHOOL_NAME);
                default -> user.setManage(null);
            }

            return userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(Constants.UserConstant.EMAIL_ALREADY_USE);
        }
    }

    @Override
    public User updateProfile(UpdateProfileRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User me = userRepository.findById(authentication.getPrincipal().toString()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

            MasterData degree = masterDataRepository.findById(request.getDegree().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.DEGREE_NOT_FOUND));

            me.setFirstName(request.getFirstName());
            me.setLastName(request.getLastName());
            me.setDegree(degree);
            me.setEmail(request.getEmail());
            me.setPhoneNumber(request.getPhoneNumber());

            return userRepository.saveAndFlush(me);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User updateAvatar(MultipartFile pic) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserById(authentication.getPrincipal().toString());

        String avatar = firebaseService.uploadMultipartFile(pic);
        user.setAvatar(avatar);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User transferRole(String id) {
        List<User> users = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User giver = userRepository.getUserById(authentication.getPrincipal().toString());

        User receiver = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        Role lecturer = roleRepository.findById(Constants.RoleIdConstant.LECTURER).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));

        if (receiver.getDepartment() != giver.getDepartment()) {
            throw new RuntimeException(Constants.UserConstant.USER_IN_OTHER_DEPARTMENTS);
        }

        if (!receiver.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            throw new RuntimeException(Constants.UserConstant.USER_ARE_NOT_LECTURER);
        }

        receiver.setRole(giver.getRole());
        receiver.setManage(giver.getManage());
        giver.setRole(lecturer);
        giver.setManage(null);

        users.add(giver);
        users.add(receiver);
        userRepository.saveAllAndFlush(users);

        return receiver;
    }

    @Override
    public User deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        if (!user.getRole().getId().equals(Constants.RoleIdConstant.LECTURER)) {
            throw new RuntimeException(Constants.UserConstant.CANNOT_DELETE_HIGH_ROLE);
        }
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new RuntimeException(Constants.UserConstant.CANNOT_DELETE_USER);
        }
        return user;
    }

    @Override
    public String exportToExcel(ExportUserListRequest request) {
        Specification<User> specification = CustomUserRepository.filterExportUser(request);
        List<User> users = userRepository.findAll(specification);
        return excelService.writeUserToExcel(users);
    }

    @Override
    public User changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = userRepository.findById(authentication.getPrincipal().toString()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

        if (!encoder.matches(request.getCurrentPassword(), me.getPassword())) {
            throw new RuntimeException(Constants.PasswordConstant.WRONG_OLD_PASSWORD);
        }
        Pattern pattern = Pattern.compile(Constants.PasswordConstant.PASSWORD_REGEX);
        if (!pattern.matcher(request.getNewPassword()).matches()) {
            throw new RuntimeException(Constants.PasswordConstant.WRONG_PASSWORD_REGEX);
        }
        if (encoder.matches(request.getNewPassword(), me.getPassword())) {
            throw new RuntimeException(Constants.PasswordConstant.NOT_BE_SAME_OLD_PASSWORD);
        }
        if (!Objects.equals(request.getNewPassword(), request.getConfirmPassword())) {
            throw new RuntimeException(Constants.PasswordConstant.BE_SAME_NEW_PASSWORD);
        }

        me.setPassword(encoder.encode(request.getNewPassword()));
        return userRepository.saveAndFlush(me);
    }

    @Override
    public User lockAccount(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

        if (!user.getIsLock()) {
            user.setIsLock(true);
        } else if (user.getIsLock()) {
            user.setIsLock(false);
        }

        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> importFromExcel(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        return userRepository.saveAll(excelService.readUserFromExcel(file));
    }

    @Override
    public User createAdmin(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        Role role = roleRepository.findById(Constants.RoleIdConstant.ADMIN).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
        user.setRole(role);
        user.setManage(Constants.SchoolNameAbbreviationConstant.SCHOOL_NAME);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User devCreateUser(CreateUserRequest request) {
        try {
            if (userRepository.existsById(request.getId())) {
                throw new RuntimeException(Constants.UserConstant.USER_EXISTED);
            }
            Role role = roleRepository.findById(Constants.RoleIdConstant.LECTURER).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
            Department department = departmentRepository.findById(request.getDepartment().getId()).orElseThrow(() -> new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_NOT_FOUND));
            MasterData degree = masterDataRepository.findById(request.getDegree().getId()).orElseThrow(() -> new RuntimeException(Constants.MasterDataConstant.DEGREE_NOT_FOUND));

            return userRepository.saveAndFlush(
                    User.builder()
                            .id(request.getId())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .degree(degree)
                            .email(request.getEmail())
                            .phoneNumber(request.getPhoneNumber())
                            .department(department)
                            .password(StringUtils.hasText(request.getPassword()) ? encoder.encode(request.getPassword()) : encoder.encode(Constants.UserConstant.DEFAULT_PASSWORD))
                            .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                            .role(role)
                            .note(request.getNote())
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(Constants.UserConstant.EMAIL_ALREADY_USE);
        }
    }
}
