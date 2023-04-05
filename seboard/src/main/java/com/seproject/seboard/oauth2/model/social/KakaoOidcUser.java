package com.seproject.seboard.oauth2.model.social;

import com.seproject.seboard.oauth2.model.Attributes;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoOidcUser extends OAuth2ProviderUser
{
    public KakaoOidcUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration registration) {
        super(attributes.getMainAttributes(),oAuth2User, registration);
    }


    @Override
    public String getEmail() {
        return (String)getAttributes().get("email");
    }

    @Override
    public String getId() {
        return String.valueOf(getAttributes().get("sub"));
    }

    @Override
    public String getUsername() {
        return (String)getAttributes().get("username"); //email?
    }

    @Override
    public String getPicture() {

        return (String)getAttributes().get("picture");
    }
}
