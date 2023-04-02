package com.seproject.seboard.oauth2.service;

import com.seproject.seboard.oauth2.model.Account;
import com.seproject.seboard.oauth2.model.ProviderUser;
import com.seproject.seboard.oauth2.model.Role;
import com.seproject.seboard.oauth2.repository.AccountRepository;
import com.seproject.seboard.oauth2.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public void register(String registrationId, ProviderUser providerUser) {
        List<Role> authorities = providerUser.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .map(authority -> {
                    Optional<Role> role = roleRepository.findByName(authority.getAuthority());
                    return role.orElseGet(() -> new Role(authority.getAuthority()));
                })
                .collect(Collectors.toList());


        Account account = Account.builder()
                .registrationId(registrationId)
                .loginId(providerUser.getProvider() + "_" +providerUser.getId())
                .provider(providerUser.getProvider())
                .username(providerUser.getUsername())
                .password(providerUser.getPassword())
                .email(providerUser.getEmail())
                .authorities(authorities)
                .build();

        accountRepository.save(account);
    }

}
