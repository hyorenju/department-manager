package vn.edu.vnua.department.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.department.entity.DepartmentDTO;
import vn.edu.vnua.department.model.authentication.UserDetailsImpl;
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
                .degree(user.getDegree())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .department(modelMapper.map(user.getDepartment(), DepartmentDTO.class))
                .roleId(user.getRole().getId())
                .manage(user.getManage())
                .note(user.getNote())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }
}
