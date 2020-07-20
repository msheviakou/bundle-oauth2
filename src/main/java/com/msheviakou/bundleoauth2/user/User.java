package com.msheviakou.bundleoauth2.user;

import com.msheviakou.bundleoauth2.common.model.BaseEntity;
import com.msheviakou.bundleoauth2.common.model.SocialProvider;
import com.msheviakou.bundleoauth2.role.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(STRING)
    @Column(name = "provider")
    private SocialProvider provider;

    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    @Column(name = "status")
    private String status;
}
