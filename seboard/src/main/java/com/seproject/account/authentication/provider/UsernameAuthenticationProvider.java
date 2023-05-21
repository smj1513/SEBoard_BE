package com.seproject.account.authentication.provider;

import com.seproject.account.model.LoginHistory;
import com.seproject.account.model.LoginPreventUser;
import com.seproject.account.repository.LoginHistoryRepository;
import com.seproject.account.repository.LoginPreventUserRepository;
import com.seproject.account.service.AccountService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import com.seproject.error.exception.PasswordIncorrectException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

    private AccountService accountService;
    private PasswordEncoder passwordEncoder;
    private LoginHistoryRepository loginHistoryRepository;
    private LoginPreventUserRepository loginPreventUserRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = accountService.loadUserByUsername(username);

        if(loginPreventUserRepository.isPreventUser(username,LocalDateTime.now())) {
            throw new CustomAuthenticationException(ErrorCode.LOGIN_PREVENT_USER,null);
        }

        if(passwordEncoder.matches(password,userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username,password, userDetails.getAuthorities());
        }

        loginHistoryRepository.save(new LoginHistory(username));
        if (loginHistoryRepository.countByTime(LocalDateTime.now().minusMinutes(10), LocalDateTime.now()) >= 5) {
            loginPreventUserRepository.save(new LoginPreventUser(username));
        }

        throw new PasswordIncorrectException(ErrorCode.PASSWORD_INCORRECT,null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
