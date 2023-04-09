package com.seproject.oauth2.service;

import com.seproject.oauth2.repository.AccountRepository;
import com.seproject.oauth2.repository.RoleRepository;
import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.model.ProviderUser;
import com.seproject.oauth2.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.util.List;
import java.util.Optional;
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
                    return role.orElseThrow(IllegalArgumentException::new);
                })
                .collect(Collectors.toList());


        Account account = Account.builder()
                .registrationId(registrationId)
                .loginId(providerUser.getProvider() + "_" +providerUser.getId())
                .provider(providerUser.getProvider())
                .username(providerUser.getUsername())
                .password(providerUser.getPassword())
                .email(providerUser.getEmail())
                .profile(providerUser.getPicture())
                .authorities(authorities)
                .build();

        accountRepository.save(account);
    }

}
