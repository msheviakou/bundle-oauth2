package com.msheviakou.bundleoauth2.user;

import com.msheviakou.bundleoauth2.authentication.api.payload.SignUpRequest;
import com.msheviakou.bundleoauth2.exception.DuplicateResourceException;
import com.msheviakou.bundleoauth2.exception.PasswordException;
import com.msheviakou.bundleoauth2.role.RoleService;
import com.msheviakou.bundleoauth2.user.payload.UserDTO;
import com.msheviakou.bundleoauth2.user.payload.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static com.msheviakou.bundleoauth2.common.model.SocialProvider.LOCAL;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    abstract public UserDTO toDTO(User user);

    abstract User merge(UserUpdateRequest request, @MappingTarget User user);

    public User fromSignUpRequest(SignUpRequest signUpRequest) {
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new PasswordException("The password and confirm password fields do not match");
        }

        String email = signUpRequest.getEmail();
        if (userService.existsByEmail(email)) {
            throw new DuplicateResourceException("User already exists with [email=" + email + "]");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setRoles(Collections.singletonList(roleService.getDefaultRole()));
        user.setProvider(LOCAL);
        return user;
    }
}
