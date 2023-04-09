package com.seproject.oauth2.service;

import com.seproject.oauth2.converters.DelegationProviderUserConverter;
import com.seproject.oauth2.converters.ProviderUserRequest;
import lombok.AllArgsConstructor;
import com.seproject.oauth2.model.*;
import com.seproject.oauth2.repository.AccountRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@AllArgsConstructor
@Service
public abstract class AbstractOAuth2UserService {

    protected AccountRepository accountRepository;
    protected AccountService accountService;
    protected DelegationProviderUserConverter providerUserConverter;


    protected ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.convert(providerUserRequest);
    }


    protected void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        Account account = accountRepository.findByLoginId(providerUser.getProvider() + "_" + providerUser.getId());

        if(account == null) {
            // 추가 정보
            accountService.register(userRequest.getClientRegistration().getRegistrationId(),providerUser);
        }
    }
}
