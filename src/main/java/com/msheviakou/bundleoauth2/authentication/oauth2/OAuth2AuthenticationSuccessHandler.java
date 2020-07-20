package com.msheviakou.bundleoauth2.authentication.oauth2;

import com.msheviakou.bundleoauth2.authentication.jwt.JwtUser;
import com.msheviakou.bundleoauth2.authentication.token.Token;
import com.msheviakou.bundleoauth2.authentication.token.TokenService;
import com.msheviakou.bundleoauth2.common.service.CookieService;
import com.msheviakou.bundleoauth2.exception.AuthenticationException;
import com.msheviakou.bundleoauth2.exception.ResourceNotFoundException;
import com.msheviakou.bundleoauth2.user.User;
import com.msheviakou.bundleoauth2.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.msheviakou.bundleoauth2.authentication.oauth2.OAuth2CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth2.authorizedRedirectUri}")
    private String authorizedRedirectUri;

    private final TokenService tokenService;
    private final UserService userService;
    private final OAuth2CookieAuthorizationRequestRepository oAuth2CookieAuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieService.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new AuthenticationException("We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String email = ((JwtUser) authentication.getPrincipal()).getUser().getEmail();
        User user = userService.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with [email=" + email + "]"));

        Token token = tokenService.createToken(user);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", token.getAccessToken())
                .queryParam("refresh_token", token.getRefreshToken())
                .queryParam("expires_in", token.getExpiresIn())
                .build().toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oAuth2CookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        return uri.equals(authorizedRedirectUri);
    }
}
