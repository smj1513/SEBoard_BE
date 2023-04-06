package com.seproject.oauth2.service;

import com.seproject.oauth2.model.*;
import com.seproject.oauth2.repository.AccountRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;


    protected OAuth2ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        String registrationId = clientRegistration.getRegistrationId();

        OAuth2ProviderUser OAuth2ProviderUser;
        if(registrationId.equals("google")) {
            OAuth2ProviderUser = new GoogleUser(oAuth2User,clientRegistration);
        } else if(registrationId.equals("keycloak")) {
            OAuth2ProviderUser = new KeycloakUser(oAuth2User,clientRegistration);
        } else if(registrationId.equals("naver")) {
            OAuth2ProviderUser = new NaverUser(oAuth2User,clientRegistration);
        } else {
            throw new UnsupportedOperationException("지원하지 않는 OAuth2 Provider");
        }

        return OAuth2ProviderUser;
    }


    protected void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        Account account = accountRepository.findByLoginId(providerUser.getId());

        if(account == null) {
            accountService.register(userRequest.getClientRegistration().getRegistrationId(),providerUser);
        } else {
            log.info("account -> {}" , account);
        }


    }
}
