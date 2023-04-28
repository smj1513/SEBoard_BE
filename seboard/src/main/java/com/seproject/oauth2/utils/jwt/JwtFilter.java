package com.seproject.oauth2.utils.jwt;

import antlr.Token;
import com.nimbusds.jose.proc.SecurityContext;
import com.seproject.error.exception.TokenValidateException;
import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.model.AuthorizationMetaData;
import com.seproject.oauth2.model.social.KakaoOidcUser;
import com.seproject.oauth2.repository.AuthorizationMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthorizationMetaDataRepository authorizationMetaDataRepository;
    private final JwtDecoder jwtDecoder;

    private final AuthenticationFailureHandler failureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        List<AuthorizationMetaData> authorizationMetaDatas
//                = authorizationMetaDataRepository.findByMethodSignature(request.getRequestURI());

        String jwt = request.getHeader("Authorization");

        try{
            if(StringUtils.hasText(jwt)) {
                jwtDecoder.validate(jwt);
                Authentication authentication = jwtDecoder.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
//                if(authorizationMetaDatas != null && !authorizationMetaDatas.isEmpty()) {
//                    boolean accessible = false;
//
//                    List<String> authorities = jwtDecoder.getAuthorities(jwt);
//                    for (AuthorizationMetaData authorizationMetaData : authorizationMetaDatas) {
//                        accessible |= authorizationMetaData.matches(authorities);
//                    }
//
//
//                    if(!accessible) throw new AccessDeniedException("접근 권한이 존재하지 않음");
//                }
            }


            filterChain.doFilter(request,response);
        } catch (TokenValidateException e) {
            failureHandler.onAuthenticationFailure(request,response,e);
        }
    }
}
