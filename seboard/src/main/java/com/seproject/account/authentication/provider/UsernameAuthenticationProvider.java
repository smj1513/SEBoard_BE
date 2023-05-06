package com.seproject.account.authentication.provider;

import com.seproject.account.service.AccountService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.PasswordIncorrectException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

    private AccountService accountService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = accountService.loadUserByUsername(username);

        if(passwordEncoder.matches(password,userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username,password, userDetails.getAuthorities());
        }

        throw new PasswordIncorrectException(ErrorCode.PASSWORD_INCORRECT,null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
