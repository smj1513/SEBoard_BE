package com.seproject.oauth2.service;

import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.model.ProviderUser;
import com.seproject.oauth2.model.Role;
import com.seproject.oauth2.repository.AccountRepository;
import com.seproject.oauth2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService extends AbstractOAuth2LoginService implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;
    public CustomUserDetailsService(AccountRepository accountRepository, AccountService accountService) {
        super(accountRepository, accountService);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginId(username);
        Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");

        if(account == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자");
        }

        return account;
    }
}
