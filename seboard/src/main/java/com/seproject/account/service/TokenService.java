package com.seproject.account.service;

import com.seproject.account.jwt.JWT;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.jwt.JwtProvider;
import com.seproject.account.model.account.Account;
import com.seproject.account.repository.AccountRepository;
import com.seproject.account.repository.token.LogoutLargeRefreshTokenRepository;
import com.seproject.account.repository.token.LogoutRefreshTokenRepository;
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

import static com.seproject.account.controller.dto.TokenDTO.*;

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
            throw new CustomAuthenticationException(ErrorCode.TOKEN_EXPIRED,null);
        }


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
