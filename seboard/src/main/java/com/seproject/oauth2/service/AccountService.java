package com.seproject.oauth2.service;

import com.seproject.oauth2.controller.command.AccountRegisterCommand;
import com.seproject.oauth2.controller.command.OAuthAccountCommand;
import com.seproject.oauth2.model.EmailAuthentication;
import com.seproject.oauth2.repository.AccountRepository;
import com.seproject.oauth2.repository.RoleRepository;
import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isExist(String loginId){
        return accountRepository.existsByLoginId(loginId);
    }

    public void register(OAuthAccountCommand accountCommand) {
        Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> authorities = List.of(roleUser.get());

        Account account = Account.builder()
                .loginId(accountCommand.getId())
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

    @Transactional
    public void register(AccountRegisterCommand accountRegisterCommand) {
        Optional<Role> roleUser = roleRepository.findByName("ROLE_KUMOH");
        List<Role> authorities = List.of(roleUser.get());

        Account account = Account.builder()
                .loginId(accountRegisterCommand.getId())
                .provider("se")
                .username(accountRegisterCommand.getName())
                .nickname(accountRegisterCommand.getNickname())
                .password(passwordEncoder.encode(accountRegisterCommand.getPassword()))
                .email(accountRegisterCommand.getId())
                .profile("none")
                .authorities(authorities)
                .build();

        accountRepository.save(account);
    }

}
