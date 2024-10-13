package vn.edu.vnua.department.model.authentication;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.DepartmentBasicDTO;
import vn.edu.vnua.department.department.entity.DepartmentDTO;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.model.authentication.UserDetailsImpl;
import vn.edu.vnua.department.role.entity.RoleDTO;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.UserConstant.USER_NOT_FOUND));
        return UserDetailsImpl.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .degree(modelMapper.map(user.getDegree(), MasterDataDTO.class))
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .department(modelMapper.map(user.getDepartment(), DepartmentBasicDTO.class))
                .role(modelMapper.map(user.getRole(), RoleDTO.class))
                .manage(user.getManage())
                .note(user.getNote())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .authorities(user.getAuthorities())
                .build();
    }
}
