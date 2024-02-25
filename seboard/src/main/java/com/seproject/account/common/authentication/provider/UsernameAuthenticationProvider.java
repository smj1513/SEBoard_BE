package com.seproject.account.common.authentication.provider;

import com.seproject.account.common.domain.LoginHistory;
import com.seproject.account.common.domain.LoginPreventUser;
import com.seproject.account.common.domain.repository.LoginHistoryRepository;
import com.seproject.account.common.domain.repository.LoginPreventUserRepository;
import com.seproject.account.account.service.AccountService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import com.seproject.error.exception.PasswordIncorrectException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final LoginHistoryRepository loginHistoryRepository;
    private final LoginPreventUserRepository loginPreventUserRepository;

    private final EntityManager em;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = accountService.loadUserByUsername(username);

        if(loginPreventUserRepository.isPreventUser(username,LocalDateTime.now()).isPresent()) {
            throw new CustomAuthenticationException(ErrorCode.LOGIN_PREVENT_USER,null);
        }

        if(passwordEncoder.matches(password,userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username,password, userDetails.getAuthorities());
        }

        loginHistoryRepository.save(new LoginHistory(username));

        if (loginHistoryRepository.countByTime(username, LocalDateTime.now().minusMinutes(10), LocalDateTime.now()) >= 5) {
            loginPreventUserRepository.save(new LoginPreventUser(username));
        }

        throw new PasswordIncorrectException(ErrorCode.PASSWORD_INCORRECT,null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
