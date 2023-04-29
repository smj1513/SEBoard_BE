package com.seproject.account.service;

import com.seproject.account.model.Account;
import com.seproject.account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginId(username);

        if(account == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자");
        }

        return account;
    }
}
