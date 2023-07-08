package com.seproject.account.token.service;

import com.seproject.account.token.domain.JWT;
import com.seproject.account.token.utils.JwtDecoder;
import com.seproject.account.token.utils.JwtProvider;
import com.seproject.account.account.domain.Account;
import com.seproject.account.account.domain.repository.AccountRepository;
import com.seproject.account.token.domain.repository.LogoutLargeRefreshTokenRepository;
import com.seproject.account.token.domain.repository.LogoutRefreshTokenRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import com.seproject.error.exception.CustomUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

import static com.seproject.account.token.controller.dto.TokenDTO.*;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;
    private final JwtDecoder jwtDecoder;

    private final LogoutRefreshTokenRepository logoutRefreshTokenRepository;
    private final LogoutLargeRefreshTokenRepository logoutLargeRefreshTokenRepository;


    private boolean existRefreshToken(String refreshToken) {
        return logoutRefreshTokenRepository.existsById(refreshToken) || logoutLargeRefreshTokenRepository.existsById(refreshToken);
    }

    @Transactional
    public AccessTokenRefreshResponse refresh(String refreshToken){

        JWT jwt = new JWT("empty",refreshToken);
        refreshToken = jwt.getRefreshToken();

        if(existRefreshToken(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.DISABLE_REFRESH_TOKEN,null);
        }


       try {
           String subject = jwtDecoder.getSubject(refreshToken);
           Account account = accountRepository.findByLoginId(subject)
                   .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

           Collection<? extends GrantedAuthority> authorities = account.getAuthorities();
           UsernamePasswordAuthenticationToken newToken
                   = new UsernamePasswordAuthenticationToken(subject,"",authorities);
           JWT token = jwtProvider.createToken(newToken);

           String newAccessToken = token.getAccessToken();
           String newRefreshToken = token.getRefreshToken();

           return AccessTokenRefreshResponse.toDTO(newAccessToken,newRefreshToken);
       } catch (CustomAuthenticationException e) {
           throw new CustomAuthenticationException(ErrorCode.DISABLE_REFRESH_TOKEN,e);
       }
    }

    public JWT createToken(UsernamePasswordAuthenticationToken token){
        JWT jwt = jwtProvider.createToken(token);
        return jwt;
    }

    public JWT createLargeToken(UserDetails userDetails){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(),"",userDetails.getAuthorities());
        return createLargeToken(token);
    }

    public JWT createLargeToken(UsernamePasswordAuthenticationToken token){
        JWT jwt = jwtProvider.createLargeToken(token);
        return jwt;
    }

    public JWT createToken(UserDetails userDetails){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(),"",userDetails.getAuthorities());
        return createToken(token);
    }


}
