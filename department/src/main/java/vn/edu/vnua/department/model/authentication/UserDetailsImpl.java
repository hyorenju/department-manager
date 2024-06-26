package vn.edu.vnua.department.model.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.vnua.department.department.entity.Department;
import vn.edu.vnua.department.department.entity.DepartmentBasicDTO;
import vn.edu.vnua.department.department.entity.DepartmentDTO;
import vn.edu.vnua.department.masterdata.entity.MasterDataDTO;
import vn.edu.vnua.department.role.entity.RoleDTO;

import java.util.Collection;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;

    private String firstName;

    private String lastName;

    private MasterDataDTO degree;

    private String email;

    private String phoneNumber;

    private DepartmentBasicDTO department;

    private RoleDTO role;

    private String manage;

    private String note;

    private String avatar;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
