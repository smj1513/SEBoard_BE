package com.seproject.oauth2.utils.jwt;

import com.seproject.oauth2.model.AuthorizationMetaData;
import com.seproject.oauth2.repository.AuthorizationMetaDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private AuthorizationMetaDataRepository authorizationMetaDataRepository;
    private JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<AuthorizationMetaData> authorizationMetaDatas
                = authorizationMetaDataRepository.findByMethodSignature(request.getRequestURI());

        String jwt = request.getHeader("Authorization");

        if(authorizationMetaDatas != null && !authorizationMetaDatas.isEmpty()) {
            boolean accessible = false;
            if(jwt != null) {
                List<String> authorities = jwtDecoder.getAuthorities(jwt);
                for (AuthorizationMetaData authorizationMetaData : authorizationMetaDatas) {
                    accessible |= authorizationMetaData.matches(authorities);
                }
            }

            if(!accessible) throw new AccessDeniedException("접근 권한이 존재하지 않음");
        }

        filterChain.doFilter(request,response);

    }
}
