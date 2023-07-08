package com.seproject.account.filter;

import com.seproject.account.common.authentication.handler.failure.CustomAuthenticationFailureHandler;
import com.seproject.account.token.domain.repository.LogoutTokenRepository;
import com.seproject.account.token.utils.JwtDecoder;
import com.seproject.error.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    private final CustomAuthenticationFailureHandler failureHandler;
    private final LogoutTokenRepository logoutTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = jwtDecoder.getAccessToken(request);

        try {
            if(StringUtils.hasText(jwt)) {

                if(!logoutTokenRepository.existsById(jwt)) {
                    Authentication authentication = jwtDecoder.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request,response);
        } catch (CustomAuthenticationException e) {
            failureHandler.onAuthenticationFailure(request,response,e);
        }
    }
}
