package com.seproject.account.authorize.category;

import com.seproject.account.service.CategoryAuthorizationService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

public class CategoryResourceFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {


    private final CategoryAuthorizationService categoryAuthorizationService;
    private final LinkedHashMap<RequestMatcher,List<ConfigAttribute>> requestMap;

    public CategoryResourceFactoryBean(CategoryAuthorizationService categoryAuthorizationService) {
        this.categoryAuthorizationService = categoryAuthorizationService;
        this.requestMap = categoryAuthorizationService.getCategoryAuthorization(1L);
    }

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        return requestMap;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
