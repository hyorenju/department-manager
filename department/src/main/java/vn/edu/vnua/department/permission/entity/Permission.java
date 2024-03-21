package vn.edu.vnua.department.permission.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.vnua.department.role.entity.Role;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(length = 100)
    private String id;

    @Column(length = 200)
    private String name;

    @Column(name = "permission_group", length = 200)
    private String permissionGroup;

    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;
}