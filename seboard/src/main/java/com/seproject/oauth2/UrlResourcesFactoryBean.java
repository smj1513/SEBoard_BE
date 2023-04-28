package com.seproject.oauth2;

import com.seproject.oauth2.service.AuthorizationService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;


@Component
public class UrlResourcesFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private final AuthorizationService authorizationService;
    private LinkedHashMap<RequestMatcher,List<ConfigAttribute>> requestMap;

    public UrlResourcesFactoryBean(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {

        if(requestMap == null) {
            requestMap = authorizationService.getRequestMap();
        }


        return requestMap;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
