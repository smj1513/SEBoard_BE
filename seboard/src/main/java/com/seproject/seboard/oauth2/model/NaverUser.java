package com.seproject.seboard.oauth2.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverUser extends OAuth2ProviderUser
{
    public NaverUser(OAuth2User oAuth2User, ClientRegistration registration) {
        super(oAuth2User.getAttributes().get("response"),oAuth2User, registration);
        Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
        for (Map.Entry<String, Object> stringObjectEntry : response.entrySet()) {
            System.out.println(stringObjectEntry.getKey() + " : " + stringObjectEntry.getValue());
        }
    }

    @Override
    public String getId() {
        return (String)getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        System.out.println((String)getAttributes().get("name "));
        return (String)getAttributes().get("name"); //email?
    }
}
