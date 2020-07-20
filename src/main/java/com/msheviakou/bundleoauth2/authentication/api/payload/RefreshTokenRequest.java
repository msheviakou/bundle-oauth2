package com.msheviakou.bundleoauth2.authentication.api.payload;

import com.msheviakou.bundleoauth2.authentication.token.Token;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RefreshTokenRequest {

    @NotNull
    private Token token;
}
