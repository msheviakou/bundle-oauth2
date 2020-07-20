package com.msheviakou.bundleoauth2.user;

import com.msheviakou.bundleoauth2.exception.ResourceNotFoundException;
import com.msheviakou.bundleoauth2.user.payload.UserDTO;
import com.msheviakou.bundleoauth2.user.payload.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userMapper.toDTO(userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with [id=" + id + "]")));
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        User user = userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with [id=" + id + "]"));
        user = userMapper.merge(userUpdateRequest, user);
        return userMapper.toDTO(userService.save(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.findById(id).ifPresentOrElse(user -> userService.deleteById(user.getId()), () -> { throw new ResourceNotFoundException("User not found with [id=" + id + "]");});
    }
}
