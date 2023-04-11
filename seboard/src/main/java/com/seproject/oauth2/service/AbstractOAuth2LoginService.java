package com.seproject.oauth2.service;

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
public abstract class AbstractOAuth2LoginService {

    protected AccountRepository accountRepository;
    protected AccountService accountService;

    protected void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        Account account = accountRepository.findByLoginId(providerUser.getProvider() + "_" + providerUser.getId());

        if(account == null) {
            accountService.register(providerUser);
        }
    }
}
