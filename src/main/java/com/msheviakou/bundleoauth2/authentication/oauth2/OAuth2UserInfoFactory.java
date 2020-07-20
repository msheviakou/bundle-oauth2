package com.msheviakou.bundleoauth2.authentication.oauth2;

import com.msheviakou.bundleoauth2.common.model.SocialProvider;
import com.msheviakou.bundleoauth2.exception.AuthenticationException;

import java.util.Map;

class OAuth2UserInfoFactory {

    static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        switch (SocialProvider.valueOf(registrationId.toUpperCase())) {
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            default:
                throw new AuthenticationException("Login with " + registrationId + " is not supported");
        }
    }
}
