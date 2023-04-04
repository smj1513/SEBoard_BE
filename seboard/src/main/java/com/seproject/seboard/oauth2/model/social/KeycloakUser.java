package com.seproject.seboard.oauth2.model.social;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KeycloakUser extends OAuth2ProviderUser {
    public KeycloakUser(OAuth2User oAuth2User, ClientRegistration registration) {
        super(oAuth2User, registration);
    }

    @Override
    public String getId() {
        return (String)getAttributes().get("sub");
    }

    @Override
    public String getUsername() {
        return (String)getAttributes().get("preferred_username");
    }

    @Override
    public String getPicture() {
        return "none";
    }


}
