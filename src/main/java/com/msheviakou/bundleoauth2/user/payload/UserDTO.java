package com.msheviakou.bundleoauth2.user.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String status;
}
