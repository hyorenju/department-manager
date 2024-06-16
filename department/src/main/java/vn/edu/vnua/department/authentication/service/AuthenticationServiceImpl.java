package vn.edu.vnua.department.authentication.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.model.authentication.UserDetailsImpl;
import vn.edu.vnua.department.response.UserLoginResponse;
import vn.edu.vnua.department.security.JwtTokenProvider;
import vn.edu.vnua.department.user.entity.User;
import vn.edu.vnua.department.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtUtils;
    private final PasswordEncoder encoder;

    @Override
    public UserLoginResponse authenticateUser(String id, String password) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(Constants.AuthenticationConstant.CANNOT_LOGIN));
        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException(Constants.AuthenticationConstant.CANNOT_LOGIN);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(id, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateTokenWithAuthorities(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new UserLoginResponse(jwt,
                userDetails.getRole(),
                userDetails.getId(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getDegree(),
                userDetails.getEmail(),
                userDetails.getPhoneNumber(),
                userDetails.getDepartment(),
                userDetails.getManage(),
                userDetails.getAvatar()
        );
    }
}
