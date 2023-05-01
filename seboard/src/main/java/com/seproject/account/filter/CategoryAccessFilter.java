package com.seproject.account.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.account.authorize.category.CategoryResourceFactoryBean;
import com.seproject.account.authorize.handler.CustomAccessDeniedHandler;
import com.seproject.account.model.CategoryAuthorization;
import com.seproject.account.model.Role;
import com.seproject.account.service.CategoryAuthorizationService;
import lombok.Data;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CategoryAccessFilter extends OncePerRequestFilter {

    @Data
    private static class CategoryRequest {
        private Long categoryId;

        public boolean isNull() {
            return categoryId == null;
        }
    }

    private final ObjectMapper objectMapper;
    private final CategoryResourceFactoryBean categoryResourceFactoryBean;
    private final RequestMatcher requestMatcher;
    private final CustomAccessDeniedHandler accessDeniedHandler;


    public CategoryAccessFilter(CategoryResourceFactoryBean categoryResourceFactoryBean,CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.objectMapper = new ObjectMapper();
        this.categoryResourceFactoryBean = categoryResourceFactoryBean;
        this.requestMatcher = new AntPathRequestMatcher("/posts/**");
        this.accessDeniedHandler = customAccessDeniedHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(requestMatcher.matches(request)) {

            ServletInputStream inputStream = request.getInputStream();
            CategoryRequest categoryRequest = objectMapper.readValue(inputStream, CategoryRequest.class);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean flag = false;

            if(categoryRequest.isNull()) {
                flag = true;
            } else {
               try{
                   LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = categoryResourceFactoryBean.getObject();

                   for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
                       List<ConfigAttribute> configAttributes = entry.getValue();
                       if(entry.getKey().matches(request)) {
                           flag |= hasAuthority(authorities,configAttributes);
                       }
                   }
               } catch (Exception e) {
                   throw new RuntimeException();
               }
            }

            if(flag) {
                filterChain.doFilter(request,response);
            } else {
                accessDeniedHandler.handle(request,response,new AccessDeniedException("카테고리에 접근할 수 있는 권힌이 없음"));
            }


        } else {
            filterChain.doFilter(request,response);
        }
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities,List<ConfigAttribute> configAttributes) {
        for (ConfigAttribute configAttribute : configAttributes) {
            String attribute = configAttribute.getAttribute();
            for (GrantedAuthority authority : authorities) {
                if(authority.getAuthority().equals(attribute)) {
                    return true;
                }
            }
        }

        return false;
    }
}
