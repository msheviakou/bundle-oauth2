package com.msheviakou.bundleoauth2.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(Long id) { return userRepository.findById(id); }

    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }

    public User save(User user) { return userRepository.save(user); }

    public Boolean existsByEmail(String email) { return userRepository.existsByEmail(email); }

    public void deleteById(Long id) { userRepository.deleteById(id); }
}
