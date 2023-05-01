package com.seproject.account.service;

import com.seproject.account.model.CategoryAuthorization;
import com.seproject.account.repository.CategoryAuthorizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryAuthorizationService {

    private final CategoryAuthorizationRepository categoryAuthorizationRepository;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getCategoryAuthorization(Long categoryId) {
        LinkedHashMap<RequestMatcher,List<ConfigAttribute>> requestMap = new LinkedHashMap<>();
        RequestMatcher requestMatcher = new AntPathRequestMatcher("/posts/test");
        List<ConfigAttribute> configAttributes = List.of(new SecurityConfig("ROLE_USER"));
        requestMap.put(requestMatcher,configAttributes);
        return requestMap;
    }

}
