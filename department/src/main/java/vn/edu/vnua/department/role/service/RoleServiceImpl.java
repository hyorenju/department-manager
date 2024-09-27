package vn.edu.vnua.department.role.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.role.entity.Role;
import vn.edu.vnua.department.role.repository.RoleRepository;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getRoleSelection() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserById(authentication.getPrincipal().toString());

        switch (user.getRole().getId()) {
            case Constants.RoleIdConstant.ADMIN: {
                List<Role> roleSelection = roleRepository.findAll();
                Role role = roleRepository.findById(Constants.RoleIdConstant.ADMIN).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
                roleSelection.remove(role);
                return roleSelection;
            }
            case Constants.RoleIdConstant.DEAN: {
                List<Role> roleSelection = roleRepository.findAll();
                Role roleAdmin = roleRepository.findById(Constants.RoleIdConstant.ADMIN).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
                Role roleDean = roleRepository.findById(Constants.RoleIdConstant.DEAN).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
                roleSelection.remove(roleAdmin);
                roleSelection.remove(roleDean);
                return roleSelection;
            }
            case Constants.RoleIdConstant.MANAGER: {
                List<Role> roleSelection = new ArrayList<>();
                Role roleAdmin = roleRepository.findById(Constants.RoleIdConstant.LECTURER).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
                Role roleDean = roleRepository.findById(Constants.RoleIdConstant.DEPUTY).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
                roleSelection.add(roleAdmin);
                roleSelection.add(roleDean);
                return roleSelection;
            }
            case Constants.RoleIdConstant.DEPUTY: {
                List<Role> roleSelection = new ArrayList<>();
                Role role = roleRepository.findById(Constants.RoleIdConstant.LECTURER).orElseThrow(() -> new RuntimeException(Constants.RoleConstant.ROLE_NOT_FOUND));
                roleSelection.add(role);
                return roleSelection;
            }
            default:
                return new ArrayList<>();
        }
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
