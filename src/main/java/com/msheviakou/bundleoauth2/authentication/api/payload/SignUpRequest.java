package com.msheviakou.bundleoauth2.authentication.api.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class SignUpRequest {

    @Email
    private String email;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
}
