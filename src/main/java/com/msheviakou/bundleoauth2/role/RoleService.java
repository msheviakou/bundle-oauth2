package com.msheviakou.bundleoauth2.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getDefaultRole() { return roleRepository.getOne(1L); }
}
