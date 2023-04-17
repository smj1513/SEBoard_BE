package com.seproject.oauth2.service;

import com.seproject.oauth2.controller.command.OAuthAccountCommand;
import com.seproject.oauth2.model.social.AbstractOidcUser;
import com.seproject.oauth2.repository.AccountRepository;
import com.seproject.oauth2.repository.RoleRepository;
import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public boolean isExist(String loginId){
        return accountRepository.existsByLoginId(loginId);
    }

    public void register(OAuthAccountCommand accountCommand) {
        Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> authorities = List.of(roleUser.get());

        Account account = Account.builder()
                .loginId(accountCommand.getProvider() + "_" +accountCommand.getId())
                .provider(accountCommand.getProvider())
                .username(accountCommand.getName())
                .nickname(accountCommand.getNickname())
                .password(UUID.randomUUID().toString())
                .email(accountCommand.getEmail())
                .profile(accountCommand.getProfile())
                .authorities(authorities)
                .build();

        accountRepository.save(account);
    }

    public Account findAccountById(String loginId) {
        return accountRepository.findByLoginId(loginId);
    }

}
