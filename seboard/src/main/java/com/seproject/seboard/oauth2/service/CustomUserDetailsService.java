package com.seproject.seboard.oauth2.service;

import com.seproject.seboard.oauth2.converters.DelegationProviderUserConverter;
import com.seproject.seboard.oauth2.converters.ProviderUserRequest;
import com.seproject.seboard.oauth2.model.Account;
import com.seproject.seboard.oauth2.model.PrincipalUser;
import com.seproject.seboard.oauth2.model.ProviderUser;
import com.seproject.seboard.oauth2.model.Role;
import com.seproject.seboard.oauth2.repository.AccountRepository;
import com.seproject.seboard.oauth2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;
    public CustomUserDetailsService(AccountRepository accountRepository, AccountService accountService, DelegationProviderUserConverter providerUserConverter) {
        super(accountRepository, accountService, providerUserConverter);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginId(username);
        Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");

        if(account == null) {
            account = Account.builder()
                    .accountId(123L)
                    .username("user")
                    .password("{noop}1234")
                    .authorities(List.of(roleUser.get()))
                    .email("alswhd1113@gmail.com")
                    .build();
        }

        ProviderUserRequest request = new ProviderUserRequest(account);
        ProviderUser providerUser = providerUser(request);

        return new PrincipalUser(providerUser);
    }
}
