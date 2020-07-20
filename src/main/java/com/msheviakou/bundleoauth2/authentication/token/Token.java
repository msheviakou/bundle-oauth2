package com.msheviakou.bundleoauth2.authentication.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token {

    private Long expiresIn;

    private String accessToken;

    private String refreshToken;
}
