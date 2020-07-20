package com.msheviakou.bundleoauth2.authentication.api;

import com.msheviakou.bundleoauth2.authentication.api.payload.AuthRequest;
import com.msheviakou.bundleoauth2.authentication.api.payload.RefreshTokenRequest;
import com.msheviakou.bundleoauth2.authentication.api.payload.SignUpRequest;
import com.msheviakou.bundleoauth2.authentication.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Token login(@RequestBody @Valid AuthRequest authRequest) { return authService.login(authRequest); }

    @PostMapping("/sign-up")
    public Token signUp(@RequestBody @Valid SignUpRequest signUpRequest) { return authService.signUp(signUpRequest); }

    @PostMapping("/refresh-token")
    public Token refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) { return authService.refreshToken(refreshTokenRequest); }
}
