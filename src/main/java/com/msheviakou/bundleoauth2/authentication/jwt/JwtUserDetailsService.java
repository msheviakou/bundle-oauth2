package com.msheviakou.bundleoauth2.authentication.jwt;

import com.msheviakou.bundleoauth2.exception.ResourceNotFoundException;
import com.msheviakou.bundleoauth2.role.Role;
import com.msheviakou.bundleoauth2.user.User;
import com.msheviakou.bundleoauth2.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with [email=" + email + "]"));
        return new JwtUser(user, getAuthorities(user.getRoles()));
    }

    public List<String> getRoleNames(List<Role> roles) { return roles.stream().map(Role::getName).collect(toList()); }

    private List<GrantedAuthority> getAuthorities(Collection<Role> roles) { return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()); }
}
