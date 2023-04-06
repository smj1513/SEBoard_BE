package com.seproject.oauth2.service;

import com.seproject.oauth2.repository.AccountRepository;
import com.seproject.oauth2.model.OAuth2ProviderUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest,OAuth2User> {


    public CustomOAuth2UserService(AccountRepository accountRepository, AccountService accountService) {
        super(accountRepository, accountService);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest,OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest); // 인가 서버와 통신하여 사용자 정보를 조회 -> OAuth2User 타입 객체에 사용자 정보를 저장

        OAuth2ProviderUser OAuth2ProviderUser = super.providerUser(clientRegistration, oAuth2User); // 조회한 사용자 정보와 레지스트레이션 정보를 이용하여 provider에 맞는 account 객체로 생성
        //회원가입 로직
        super.register(OAuth2ProviderUser,userRequest);

        return oAuth2User;
    }
}
