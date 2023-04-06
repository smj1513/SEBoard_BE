package com.seproject.oauth2.model.social;

import com.seproject.oauth2.model.Attributes;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class NaverUser extends OAuth2ProviderUser
{

    public NaverUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration registration) {
        super(attributes.getSubAttributes(), oAuth2User, registration);
    }

    @Override
    public String getId() {
        return (String)getAttributes().get("id");
    }

    @Override
    public String getUsername() {
//        System.out.println((String)getAttributes().get("name "));
        return (String)getAttributes().get("name"); //email?
    }

    @Override
    public String getPicture() {
        return (String)getAttributes().get("profile_image");
    }
}
