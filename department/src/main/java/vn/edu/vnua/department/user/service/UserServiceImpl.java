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
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.repository.DepartmentRepository;
import vn.edu.vnua.department.faculty.entity.Faculty;
import vn.edu.vnua.department.faculty.repository.FacultyRepository;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.role.repository.RoleRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.CustomUserRepository;
import vn.edu.vnua.department.user.repository.UserRepository;
import vn.edu.vnua.department.user.request.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final PasswordEncoder encoder;

    @Override
    public Page<User> getUserList(GetUserListRequest request) {
        Specification<User> specification = CustomUserRepository.filterUserList(request);
        return userRepository.findAll(specification, PageRequest.of(request.getPage() - 1, request.getSize()));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findById(authentication.getPrincipal().toString()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

            if (userRepository.existsById(request.getId())) {
                throw new RuntimeException(Constants.UserConstant.USER_ALREADY_EXIST);
            }
            Role role = roleRepository.findById(Constants.RoleIdConstant.LECTURER).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
            Department department = departmentRepository.findById(request.getDepartment().getId()).orElseThrow(() -> new RuntimeException(Constants.DepartmentConstant.DEPARTMENT_NOT_FOUND));

            if (user.getRole().getId().equals(Constants.RoleIdConstant.MANAGER) ||
                    user.getRole().getId().equals(Constants.RoleIdConstant.DEPUTY)){
                department = user.getDepartment();
            } else /*if (user.getRole().getId().equals(Constants.RoleIdConstant.DEAN))*/ {
                if (!user.getDepartment().getFaculty().equals(department.getFaculty())) {
                    throw new RuntimeException(Constants.FacultyConstant.CANNOT_CREATE_USER);
                }
            }

            return userRepository.saveAndFlush(
                    User.builder()
                            .id(request.getId().toUpperCase())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .degree(request.getDegree())
                            .email(request.getEmail())
                            .phoneNumber(request.getPhoneNumber())
                            .department(department)
                            .password(StringUtils.hasText(request.getPassword()) ? encoder.encode(request.getPassword()) : encoder.encode("123"))
                            .role(role)
                            .note(request.getNote())
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(Constants.UserConstant.EMAIL_ALREADY_USE);
        }
    }

    @Override
    public User createDean(String id, CreateDeanRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

        Role lecturer = roleRepository.findById(Constants.RoleIdConstant.LECTURER).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
        if (!user.getRole().equals(lecturer)) {
            throw new RuntimeException(Constants.UserConstant.USER_ARE_NOT_LECTURER);
        }

        Role role = roleRepository.findById(Constants.RoleIdConstant.DEAN).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
        String manage = request.getManage();
        if (!StringUtils.hasText(manage)) {
            throw new RuntimeException(Constants.UserConstant.MANAGE_NOT_BLANK);
        } else {
            Optional<Faculty> facultyOptional = facultyRepository.findById(manage);
            if (facultyOptional.isEmpty()) {
                throw new RuntimeException(Constants.UserConstant.MANAGE_NOT_FOUND);
            }
        }
        if (userRepository.existsByRoleAndManage(role, manage)) {
            throw new RuntimeException(Constants.UserConstant.DEAN_ALREADY_EXIST);
        }

        user.setRole(role);
        user.setManage(manage);
        user.setNote(request.getNote());

        return userRepository.saveAndFlush(user);
    }

    @Override
    public User createManager(String id, CreateManagerRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        Role role = roleRepository.findById(Constants.RoleIdConstant.MANAGER).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));

        Role lecturer = roleRepository.findById(Constants.RoleIdConstant.LECTURER).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
        if (!user.getRole().equals(lecturer)) {
            throw new RuntimeException(Constants.UserConstant.USER_ARE_NOT_LECTURER);
        }

        String manage = user.getDepartment().getId();
        if (userRepository.existsByRoleAndManage(role, manage)) {
            throw new RuntimeException(Constants.UserConstant.MANAGER_ALREADY_EXIST);
        }

        user.setRole(role);
        user.setManage(manage);
        user.setNote(request.getNote());

        return userRepository.saveAndFlush(user);
    }

    @Override
    public User updateUser(String id, UpdateUserRequest request) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
            String roleId = request.getRole().getId();
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));

            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setDegree(request.getDegree());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(StringUtils.hasText(request.getPassword()) ? encoder.encode(request.getPassword()) : user.getPassword());
            user.setRole(role);
            user.setNote(request.getNote());

            switch (roleId) {
                case Constants.RoleIdConstant.LECTURER -> user.setManage(null);
                case Constants.RoleIdConstant.DEPUTY -> user.setManage(user.getDepartment().getId());
                default -> throw new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND);
            }

            return userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(Constants.UserConstant.EMAIL_ALREADY_USE);
        }
    }

    @Override
    public User transferRole(String id) {
        List<User> users = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User giver = userRepository.findById(authentication.getPrincipal().toString()).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));

        User receiver = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        Role lecturer = roleRepository.findById(Constants.RoleIdConstant.LECTURER).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
        if (!receiver.getRole().equals(lecturer)) {
            throw new RuntimeException(Constants.UserConstant.USER_ARE_NOT_LECTURER);
        }

        receiver.setRole(giver.getRole());
        receiver.setManage(giver.getManage());
        giver.setRole(lecturer);
        receiver.setManage(null);

        users.add(giver);
        users.add(receiver);
        userRepository.saveAllAndFlush(users);

        return receiver;
    }

    @Override
    public User deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        userRepository.delete(user);
        return user;
    }

    @Override
    public User createPrincipal(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        Role role = roleRepository.findById(Constants.RoleIdConstant.PRINCIPAL).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
        user.setRole(role);
        user.setManage(Constants.UserConstant.SET_PRINCIPAL_MANAGE);
        return userRepository.saveAndFlush(user);
    }
}
