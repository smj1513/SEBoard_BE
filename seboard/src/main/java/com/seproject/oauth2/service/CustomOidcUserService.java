package com.seproject.oauth2.service;

import com.seproject.oauth2.model.social.KakaoOidcUser;
import com.seproject.oauth2.repository.AccountRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends AbstractOAuth2LoginService implements OAuth2UserService<OidcUserRequest, OidcUser> {


    public CustomOidcUserService(AccountRepository accountRepository,
                                 AccountService accountService) {
        super(accountRepository, accountService);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        clientRegistration = ClientRegistration.withClientRegistration(clientRegistration)
            .userNameAttributeName("sub")
                .build();

        OidcUserRequest oidcUserRequest = new OidcUserRequest(clientRegistration,
                userRequest.getAccessToken(),
                userRequest.getIdToken(),
                userRequest.getAdditionalParameters());

        OidcUserService oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest); // 인가 서버와 통신하여 사용자 정보를 조회 -> OAuth2User 타입 객체에 사용자 정보를 저장
//        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oidcUser);
//        ProviderUser OAuth2ProviderUser = super.providerUser(providerUserRequest); // 조회한 사용자 정보와 레지스트레이션 정보를 이용하여 provider에 맞는 account 객체로 생성

        if(clientRegistration.getRegistrationId().equals("kakao")) {
            return new KakaoOidcUser(oidcUser);
        }
            throw new OAuth2AuthenticationException("지원하지 않는 open id connection");

//        //회원가입 로직
//        super.register(OAuth2ProviderUser,userRequest);


    }
}
