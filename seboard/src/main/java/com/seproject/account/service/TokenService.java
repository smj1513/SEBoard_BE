package com.seproject.account.service;

import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.seproject.account.jwt.JWT;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.jwt.JwtProvider;
import com.seproject.account.model.Account;
import com.seproject.account.repository.AccountRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
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


    @Transactional
    public AccessTokenRefreshResponse refresh(String refreshToken){

        JWT jwt = new JWT("empty",refreshToken);
        refreshToken = jwt.getRefreshToken();

        //TODO : 로그아웃된 토큰인지 확인
        String subject = jwtDecoder.getSubject(refreshToken);
        Account account = accountRepository.findByLoginId(subject);

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

    public JWT createToken(UserDetails userDetails){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(),"",userDetails.getAuthorities());
        return createToken(token);
    }


}
