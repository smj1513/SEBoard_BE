package com.seproject.oauth2.model.social;

import com.seproject.oauth2.model.Attributes;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleUser extends OAuth2ProviderUser {

    public GoogleUser(OAuth2User oAuth2User, ClientRegistration registration) {
        super(oAuth2User,registration);
    }

    public GoogleUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration registration) {
        super(attributes.getMainAttributes(), oAuth2User, registration);
    }

    @Override
    public String getId() { // 구글은 username == userId
        return (String)getAttributes().get("sub");
    }

    @Override
    public String getUsername() { //username == userId
        return (String)getAttributes().get("name");
    }

    @Override
    public String getPicture() {
        return (String)getAttributes().get("picture");
    }
}
