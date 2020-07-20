package com.msheviakou.bundleoauth2.authentication.oauth2;

import com.msheviakou.bundleoauth2.authentication.jwt.JwtUser;
import com.msheviakou.bundleoauth2.common.model.SocialProvider;
import com.msheviakou.bundleoauth2.role.RoleService;
import com.msheviakou.bundleoauth2.user.User;
import com.msheviakou.bundleoauth2.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static org.springframework.util.StringUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (isEmpty(oAuth2UserInfo.getEmail())) {
            throw new com.msheviakou.bundleoauth2.exception.AuthenticationException("Email not found from OAuth2 provider for [oAuth2UserId=" + oAuth2UserInfo.getId() + "]");
        }

        SocialProvider provider = SocialProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElseGet(() -> createNewUser(oAuth2UserInfo, provider));

        return JwtUser.create(user, oAuth2UserInfo.getAttributes());
    }

    private User createNewUser(OAuth2UserInfo oAuth2UserInfo, SocialProvider provider) {
        User user = new User();

        user.setEmail(oAuth2UserInfo.getEmail());
        user.setFirstName(oAuth2UserInfo.getName().split(" ")[0]);
        user.setLastName(oAuth2UserInfo.getName().split(" ")[1]);
        user.setRoles(Collections.singletonList(roleService.getDefaultRole()));
        user.setProvider(provider);

        return userRepository.save(user);
    }
}
